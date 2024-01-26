/**
 * @ (#) InsuranceDAOImpl Oct 21, 2005
 * Project      : TTK HealthCare Services
 * File         : InsuranceCompanyDAOImpl.java
 * Author       : Ravindra
 * Company      : Span Systems Corporation
 * Date Created : Oct 21, 2005
 *
 * @author       :  Ravindra

 * Modified by   :  RamaKrishna K M
 * Modified date :  Dec 13, 2005
 * Reason        :  Modification in log statements
 */

package com.ttk.dao.impl.empanelment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.InsuranceDetailVO;
import com.ttk.dto.empanelment.InsuranceVO;
//added for CR KOC Mail-SMS Notification for Cigna
import com.ttk.dto.empanelment.MailNotificationVO;
import com.ttk.dto.empanelment.ReInsuranceDetailVO;
import com.ttk.dto.insurancepricing.AgeMasterVO;
import com.ttk.dto.insurancepricing.InsPricingVO;
import com.ttk.dto.insurancepricing.PolicyConfigVO;
import com.ttk.dto.insurancepricing.SwFinalQuoteVO;
import com.ttk.dto.insurancepricing.SwPolicyConfigVO;
import com.ttk.dto.insurancepricing.SwPricingSummaryVO;

public class InsuranceDAOImpl  implements BaseDAO, Serializable{

	private static Logger log = Logger.getLogger(InsuranceDAOImpl.class);
	
	private static final int  INS_SEQ_ID = 1 ;
	private static final int  INS_ADDR_SEQ_ID = 2 ;
	private static final int  TPA_OFFICE_SEQ_ID = 3 ;
	private static final int  INS_SECTOR_GENERAL_TYPE_ID = 4;
	private static final int  INS_OFFICE_GENERAL_TYPE_ID = 5;
	private static final int  INS_COMP_NAME = 6 ;
	private static final int  ABBREVATION_CODE   = 7;
	private static final int  INS_COMP_CODE_NUMBER  = 8 ;
	private static final int  EMPANELED_DATE  = 9 ;
	private static final int  DISB_TYPE_ID  = 10 ;
	private static final int  FREQUENCY_TYPE_ID  =11  ;
	private static final int  FLOAT_REPLENISHMENT_PERIOD =12;
	private static final int  ACTIVE_YN  = 13;
	private static final int  INS_PARENT_SEQ_ID  = 14;
	private static final int  ADDRESS_1  = 15 ;
	private static final int  ADDRESS_2  = 16 ;
	private static final int  ADDRESS_3 = 17;
	private static final int  CITY_TYPE_ID  = 18 ;
	private static final int  STATE_TYPE_ID   = 19 ;
	private static final int  PIN_CODE  = 20 ;
	private static final int  COUNTRY_ID   = 21 ;
	private static final int  LANDMARKS  = 22 ;
	private static final int  INS_OFF_SEQ_ID = 23;
	private static final int  VALID_FROM = 24;
	private static final int  VALID_TO = 25;
	private static final int  USER_SEQ_ID = 26;
	
	// Shortfall CR KOC1179
	private static final int  INS_COMPANY_EMAILID  = 27 ;
	
	//added for CR KOC Mail-SMS Notification for Cigna.
	
	private static final int NOTIFY_TYPE_ID = 28;
	private static final int NOTIFY_EMAIL_ID = 29;
	private static final int CC_EMAIL_ID = 30;
	//ended
	private static final int user_group = 31;//KOC Cigna_insurance_resriction

	private static final int  AUTHORITY_STANANDARD = 32;
	private static final int  ISD_CODE = 33;
	private static final int  STD_CODE = 34;
	private static final int  PHONE1 = 35;
	private static final int  PHONE2 = 36;
	private static final int  INSCATEGORY = 37;// test nag
	private static final int  ROW_PROCESSED = 38;

	
	private static final String strPolicyNumberFlag="SELECT P.POLICY_NO,CASE WHEN P.POLICY_NO IS NULL THEN 'N' ELSE 'Y' END AS VALID_POL_YN,GR.GROUP_ID,GR.PRICING_CLIENT_CODE,GR.GROUP_NAME FROM APP.TPA_PRICING_PREV_POL_DATA P  LEFT OUTER JOIN APP.TPA_GROUP_REGISTRATION GR ON (P.CLIENT_CODE = GR.PRICING_CLIENT_CODE) WHERE P.POLICY_NO = ?";
	private static final String strInsuranceList="SELECT * FROM (SELECT INS_SEQ_ID, INS_COMP_NAME, EMPANELED_DATE, ACTIVE_YN, INS_COMP_CODE_NUMBER, DENSE_RANK() OVER (ORDER BY #, ROWNUM) Q FROM TPA_INS_INFO WHERE INS_OFFICE_GENERAL_TYPE_ID = 'IHO' " ;

	private static final String strInsurancelDetail="SELECT INS_OFFICE_GENERAL_TYPE_ID,TPA_INS_INFO.INS_PARENT_SEQ_ID,TPA_INS_INFO.TPA_OFFICE_SEQ_ID,A.INS_COMP_NAME,ACTIVE_YN,INS_COMP_CODE_NUMBER,A.ABBREVATION_CODE,A.INS_SECTOR_GENERAL_TYPE_ID,EMPANELED_DATE,DISB_TYPE_ID,FLOAT_REPLENISHMENT_PERIOD ,FREQUENCY_TYPE_ID,ADDR_SEQ_ID,ADDRESS_1,ADDRESS_2,ADDRESS_3,STATE_TYPE_ID,CITY_TYPE_ID, PIN_CODE,EMAIL_ID, COUNTRY_ID,A.DESCRIPTION, TPA_INS_INFO.NOTIFY_TYPE_ID, TPA_INS_INFO.TO_MAIL_ID, TPA_INS_INFO.CC_MAIL_ID,TPA_INS_INFO.USER_GROUP,TPA_INS_INFO.PAYER_AUTHORITY,TPA_ADDRESS.ISD_CODE,TPA_ADDRESS.STD_CODE,TPA_ADDRESS.OFFICE_PHONE1,TPA_ADDRESS.OFFICE_PHONE2,A.PI_NONPI_INSURER FROM TPA_INS_INFO, TPA_ADDRESS,(SELECT INS_SEQ_ID,INS_COMP_NAME, ABBREVATION_CODE, INS_SECTOR_GENERAL_TYPE_ID,DESCRIPTION,TPA_INS_INFO.PI_NONPI_INSURER FROM TPA_INS_INFO,TPA_GENERAL_CODE  WHERE INS_PARENT_SEQ_ID IS NULL AND TPA_INS_INFO.INS_SECTOR_GENERAL_TYPE_ID = GENERAL_TYPE_ID AND INS_SEQ_ID = ?)A WHERE TPA_INS_INFO.INS_SEQ_ID = TPA_ADDRESS.INS_SEQ_ID AND TPA_INS_INFO.INS_SEQ_ID = ?";	
	
	private static final String strAddUpdateInsuranceInfo="{CALL INSCOMP_EMPANEL_PKG.PR_INS_INFO_SAVE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";	
	private static final String strDeleteInsuranceInfo="{CALL INSCOMP_EMPANEL_PKG.PR_INS_INFO_DELETE(?,?)}";
	//private static final String strGetCompanyDetails = "SELECT * FROM (SELECT A.*,DENSE_RANK() OVER (ORDER BY INS_COMP_NAME,ROWNUM) Q FROM(SELECT INS_SEQ_ID, TPA_OFFICE_SEQ_ID,B.DESCRIPTION, INS_PARENT_SEQ_ID,A.INS_SECTOR_GENERAL_TYPE_ID,A.ABBREVATION_CODE, A.INS_COMP_NAME,CASE WHEN A.INS_OFFICE_GENERAL_TYPE_ID != 'IHO' THEN A.INS_COMP_NAME ||'-'|| A.INS_COMP_CODE_NUMBER ELSE A.INS_COMP_NAME END COMPANY,A.INS_COMP_CODE_NUMBER, INS_OFFICE_GENERAL_TYPE_ID FROM (SELECT  INS_SEQ_ID, TPA_OFFICE_SEQ_ID, INS_PARENT_SEQ_ID,INS_SECTOR_GENERAL_TYPE_ID,INS_COMP_CODE_NUMBER,ABBREVATION_CODE, INS_OFFICE_GENERAL_TYPE_ID ,INS_COMP_NAME FROM TPA_INS_INFO START WITH INS_SEQ_ID = ? ";
	// Shortfall CR KOC1179 added EMAIL_ID
	private static final String strGetCompanyDetails = "SELECT * FROM (SELECT A.*,DENSE_RANK() OVER (ORDER BY INS_COMP_NAME,ROWNUM) Q FROM(SELECT INS_SEQ_ID, TPA_OFFICE_SEQ_ID,B.DESCRIPTION, INS_PARENT_SEQ_ID,A.INS_SECTOR_GENERAL_TYPE_ID,A.ABBREVATION_CODE, A.INS_COMP_NAME,CASE WHEN A.INS_OFFICE_GENERAL_TYPE_ID != 'IHO' THEN A.INS_COMP_NAME ||'-'|| A.INS_COMP_CODE_NUMBER ELSE A.INS_COMP_NAME END COMPANY,A.INS_COMP_CODE_NUMBER, INS_OFFICE_GENERAL_TYPE_ID,EMAIL_ID FROM (SELECT  INS_SEQ_ID, TPA_OFFICE_SEQ_ID, INS_PARENT_SEQ_ID,INS_SECTOR_GENERAL_TYPE_ID,INS_COMP_CODE_NUMBER,ABBREVATION_CODE, INS_OFFICE_GENERAL_TYPE_ID ,INS_COMP_NAME,EMAIL_ID FROM TPA_INS_INFO START WITH INS_SEQ_ID = ? ";
	// Shortfall CR KOC1179 getting EMAIL_ID
	private static final String strSavePricingList = "{CALL group_profile.save_group_profile(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSelectPricingList = "{CALL group_profile.select_group_profile(?,?)}";
	private static final String strProfileIncomeList="select id,val,grp from intx.TPA_GRP_MASTER_CODES ORDER by grp asc";
	private static final String strSaveIncomeProfile="{CALL group_profile.save_group_miltiple_list(?,?,?,?,?,?)}";
	private static final String strProfileIncomeListValue="{CALL group_profile.select_group_miltiple_list(?,?)}";
	private static final String strInsuranceProfileList="{CALL group_profile.select_group_profile_list(?,?)}";
	private static final String strSaveBenefitCoverage="{CALL online_policy_copy_issue.save_benifit_coverage(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetBenefitCoverage="{CALL online_policy_copy_issue.select_benifit_coverage(?,?,?)}";
	
	private static final String strSaveGenerateQuote="{CALL Premium_calculation.p_calculate_premium(?,?,?,?,?,?,?,?,?,?)}";
	
	private static final String strSelectGenerateQuote="{CALL Premium_calculation.select_calculate_premium (?,?,?,?)}";
	private static final String strMasterListValue="{CALL POLICY_COPY_ISSUE_AUTOMATION.GET_IP_MSTR_DETAILS(?,?,?,?)}";//Master Details
	private static final String strswSavePricingList="{CALL POLICY_COPY_ISSUE_AUTOMATION.SAVE_PROFILE_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strswSelectPricingList="{CALL POLICY_COPY_ISSUE_AUTOMATION.SELECT_PROFILE_DETAILS(?,?)}";
	private static final String strSwSaveIncomeProfile="{CALL POLICY_COPY_ISSUE_AUTOMATION.SAVE_BENF_COVR_DETAILS(?,?,?,?,?,?,?)}";
	private static final String strswProfileIncomeListValue="{CALL POLICY_COPY_ISSUE_AUTOMATION.SELECT_BENF_COVR_DETAILS(?,?,?,?,?,?)}";

	private static final String strSaveCalulatePricingList="{CALL POLICY_COPY_ISSUE_AUTOMATION.RATE_CALCULATION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	private static final String strSwInsuranceProfileList="{CALL POLICY_COPY_ISSUE_AUTOMATION.SELECT_CLIENT_PROF_LIST(?,?,?,?,?,?,?,?,?)}";
	private static final String strPricingFlag = " SELECT DISTINCT G.BENF_COV_SVE_YN,G.CAL_CPM_SVE_YN,CAL_LODING_SVE_YN,DMGRPHC_SAVE_YN,TRND_FACTOR_PERC,NVL(F.MODIFY_YN,'Y') AS MODIFY_YN,NVL(CENSUS_SAVE_YN,'N') AS CENSUS_SAVE_YN,G.PAST_POLICY_RUN_DATE, to_char(NVL(nvl(G.PAST_POLICY_RUN_DATE,NVL(D.ADDED_DATE,D.UPDATED_DATE)),(select distinct nvl(d.added_date,d.updated_date) from APP.TPA_PRICING_PREV_POL_DATA d where rownum=1)),'dd-mm-yyyy') AS ADDED_DATE FROM APP.TPA_GROUP_PROFILE_DETAILS G LEFT OUTER JOIN APP.TPA_GROUP_FNL_CPM_DETAILS F ON (G.GRP_PROF_SEQ_ID=F.GRP_PROF_SEQ_ID) LEFT JOIN APP.TPA_PRICING_PREV_POL_DATA D ON (D.POLICY_NO=G.PREV_POLICY_NO) LEFT OUTER JOIN APP.TPA_GROPU_BNF_LIVS_COV_DETAILS H ON (G.GRP_PROF_SEQ_ID =H.GRP_PROF_SEQ_ID)  WHERE G.GRP_PROF_SEQ_ID =?";
	private static final String strSwSaveIncomeNatProfile="{CALL POLICY_COPY_ISSUE_AUTOMATION.SAVE_NATLITS_COVR_DETAILS(?,?,?,?,?)}";
	private static final String strCPMBeforeCal="{CALL POLICY_COPY_ISSUE_AUTOMATION.GET_PST_PROJ_DETAILS(?,?)}";
	private static final String strCPMAfterCal="{CALL POLICY_COPY_ISSUE_AUTOMATION.GET_CPM_ON_CALC(?,?)}";
	private static final String strLoadingAfterCalc="{CALL POLICY_COPY_ISSUE_AUTOMATION.GET_LODING_DETAILS(?,?)}";
	private static final String strSaveLoading="{CALL POLICY_COPY_ISSUE_AUTOMATION.SAVE_LOADINGS(?,?,?,?,?)}";
	private static final String strCPMAfterCalLoading="{CALL POLICY_COPY_ISSUE_AUTOMATION.GET_FINAL_PREMIUM(?,?,?)}";
	private static final String strCalculateLoading="{CALL POLICY_COPY_ISSUE_AUTOMATION.LOADINGS_CAL(?,?,?,?)}";
	private static final String strDemographicOnMasterData="{CALL POLICY_COPY_ISSUE_AUTOMATION.GET_COV_DMGRPHC_DETAILS(?,?)}";
	private static final String strDemographicOnsaveData="{CALL POLICY_COPY_ISSUE_AUTOMATION.GET_DMGRPHC_DTLS_ON_SAVE(?,?)}";
	private static final String strswSaveQuotationDetails="{CALL POLICY_COPY_ISSUE_AUTOMATION.SAVE_POLICY_INFO(?,?,?,?,?,?,?,?,?,?)}";
	private static final String strQuoPolicyDetails="{CALL POLICY_COPY_ISSUE_AUTOMATION.SELECT_POLICY_DETAILS(?,?)}";
	private static final String strSavePolicyCopy="{CALL POLICY_COPY_ISSUE_AUTOMATION.SAVE_POL_COPIES(?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSwPolicyCopyList="{CALL POLICY_COPY_ISSUE_AUTOMATION.SELECT_POL_COPIES(?,?)}";
	private static final String strGetUploadedFile="{CALL POLICY_COPY_ISSUE_AUTOMATION.SELECT_POL_COPY(?,?)}";
	private static final String strswissuePolicy="{CALL POLICY_COPY_ISSUE_AUTOMATION.ISSUE_POLICY(?,?,?,?,?,?,?,?)}";
    private static final String strProductList = "SELECT P.PRODUCT_SEQ_ID,P.PRODUCT_NAME FROM APP.TPA_INS_PRODUCT P JOIN APP.TPA_INS_INFO I ON (P.INS_SEQ_ID=I.INS_SEQ_ID) WHERE I.INS_COMP_CODE_NUMBER = 'ALK01' ORDER BY P.PRODUCT_SEQ_ID ";

	private static final String strSaveDemographicData="{CALL POLICY_COPY_ISSUE_AUTOMATION.SAVE_DMGRPHC_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	private static final String strSavememberXml = "{CALL POLICY_COPY_ISSUE_AUTOMATION.UPLOAD_CENSUS(?,?,?,?,?,?)}";
	private static final String strFetchDetails="{CALL POLICY_COPY_ISSUE_AUTOMATION.FETCH_PAST_POLICY_DETAILS(?,?,?)}";
	private static final String strPolicyStatusInfo="SELECT DISTINCT D.POLICY_NO ,(D.POL_EXPIRE_DATE+1) AS START_DATE,(ADD_MONTHS((D.POL_EXPIRE_DATE+1),12)-1) AS END_DATE, CASE WHEN UPPER(NVL(D.AXA_VIDAL_IND,'AXA'))='AXA' THEN 'Y' ELSE 'N' END AS FLAG, CASE WHEN UPPER(NVL(D.AXA_VIDAL_IND,'AXA'))='AXA' THEN 'FETCH KEY COVERAGES PROVISION IS ONLY AVAILABLE WITH PAST POLICY NUMBERS THAT EXISTS IN THE VIDAL HEALTH SYSTEM.' ELSE NULL END AS ALERT_MSG, INTX.POLICY_COPY_ISSUE_AUTOMATION.GET_PRODUCT_NAME('?') AS POLICY_CATEGORY FROM APP.TPA_PRICING_PREV_POL_DATA D WHERE D.POLICY_NO = ?";
	private static final String strPolicyDateInfo="{CALL policy_copy_issue_automation.get_poliy_dates(?,?,?,?)}";
	private static final String strPolicyViewFile="SELECT G.GRP_PROF_SEQ_ID,G.MEM_TEMPLET_NAME  AS FILE_NAME  FROM  APP.TPA_GROUP_PROFILE_DETAILS  G WHERE G.GRP_PROF_SEQ_ID =?";
	private static final String strSaveReInsuranceData="{CALL REINSURER_EMPANEL_PKG.REINS_INFO_SAVE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSearchReInsuranceData="{CALL REINSURER_EMPANEL_PKG.SELECT_REINSURENCE_LIST(?,?,?,?)}";
	private static final String strGetReInsuranceData="{CALL REINSURER_EMPANEL_PKG.SELECT_REINSURENCE_DETAILS(?,?)}";
	
	

	//private static final String strInsurancelDetail="SELECT INS_OFFICE_GENERAL_TYPE_ID,TPA_INS_INFO.INS_PARENT_SEQ_ID,TPA_INS_INFO.TPA_OFFICE_SEQ_ID,A.INS_COMP_NAME,ACTIVE_YN,INS_COMP_CODE_NUMBER,A.ABBREVATION_CODE,A.INS_SECTOR_GENERAL_TYPE_ID,EMPANELED_DATE,DISB_TYPE_ID,FLOAT_REPLENISHMENT_PERIOD ,FREQUENCY_TYPE_ID,ADDR_SEQ_ID,ADDRESS_1,ADDRESS_2,ADDRESS_3,STATE_TYPE_ID,CITY_TYPE_ID, PIN_CODE,EMAIL_ID, COUNTRY_ID,A.DESCRIPTION, TPA_INS_INFO.NOTIFY_TYPE_ID, TPA_INS_INFO.TO_MAIL_ID, TPA_INS_INFO.CC_MAIL_ID,TPA_INS_INFO.USER_GROUP,TPA_INS_INFO.PAYER_AUTHORITY,TPA_ADDRESS.ISD_CODE,TPA_ADDRESS.STD_CODE,TPA_ADDRESS.OFFICE_PHONE1,TPA_ADDRESS.OFFICE_PHONE2,PI_NONPI_INSURER FROM TPA_INS_INFO, TPA_ADDRESS,(SELECT INS_SEQ_ID,INS_COMP_NAME, ABBREVATION_CODE, INS_SECTOR_GENERAL_TYPE_ID,DESCRIPTION FROM TPA_INS_INFO,TPA_GENERAL_CODE  WHERE INS_PARENT_SEQ_ID IS NULL AND TPA_INS_INFO.INS_SECTOR_GENERAL_TYPE_ID = GENERAL_TYPE_ID AND INS_SEQ_ID = ?)A WHERE TPA_INS_INFO.INS_SEQ_ID = TPA_ADDRESS.INS_SEQ_ID AND TPA_INS_INFO.INS_SEQ_ID = ?"; //test Nag

	
	/**
     * This method returns the ArrayList, which contains the list of InsuranceCompany which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of InsuranceCompany object's which contains the InsuranceCompany details
     * @exception throws TTKException
     */
    public ArrayList getInsuranceCompanyList(ArrayList alSearchObjects) throws TTKException
	{
        StringBuffer sbfDynamicQuery = new StringBuffer();
        String strStaticQuery = strInsuranceList;
        Collection<Object> resultList = new ArrayList<Object>();
        InsuranceVO  insuranceVO = null;
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        if(alSearchObjects != null && alSearchObjects.size() > 0)
        {
            String strEmpaneledDate   = TTKCommon.checkNull(((SearchCriteria)alSearchObjects.get(0)).getValue());
            if (!strEmpaneledDate.equals(""))
            {
                sbfDynamicQuery.append(" AND TRUNC(EMPANELED_DATE)>= TO_DATE('"+strEmpaneledDate+"','DD/MM/YYYY')");
            }//end of if (!strEmpaneledDate.equals(""))
            for(int i=1; i < alSearchObjects.size()-4; i++)
            {
                if(!((SearchCriteria)alSearchObjects.get(i)).getValue().equals(""))
                {
                    sbfDynamicQuery.append(" AND UPPER("+ ((SearchCriteria)alSearchObjects.get(i)).getName()+") LIKE UPPER('"+((SearchCriteria)alSearchObjects.get(i)).getValue()+"%')");
                }//end of if(!((SearchCriteria)alSearchCriteria.get(i)).getValue().equals(""))
            }//end of for()
        }//end of if(alSearchCriteria != null && alSearchCriteria.size() > 0)
        strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"#", (String)alSearchObjects.get(alSearchObjects.size()-4)+" "+(String)alSearchObjects.get(alSearchObjects.size()-3));
        sbfDynamicQuery = sbfDynamicQuery .append( " )A WHERE Q >= "+(String)alSearchObjects.get(alSearchObjects.size()-2)+ " AND Q <= "+(String)alSearchObjects.get(alSearchObjects.size()-1));
        strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();
        try{
            conn = ResourceManager.getConnection();
            pStmt = conn.prepareStatement(strStaticQuery);
            rs = pStmt.executeQuery();
            if(rs != null){
                while(rs.next()){
                    insuranceVO = new InsuranceVO();


                    if(rs.getString("INS_SEQ_ID") != null){
                        insuranceVO.setInsuranceSeqID(new Long(rs.getLong("INS_SEQ_ID")));
                    }//end of if(rs.getString("INS_SEQ_ID") != null)

                    insuranceVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));

                    if(rs.getDate("EMPANELED_DATE") != null){
                        insuranceVO.setEmpanelmentDate(rs.getDate("EMPANELED_DATE"));
                    }//end of if(rs.getDate("EMPANELED_DATE") != null)
                    
                    if(rs.getString("INS_COMP_CODE_NUMBER") != null){
                        insuranceVO.setCompanyCodeNbr(rs.getString("INS_COMP_CODE_NUMBER"));
                    }//end of if(rs.getDate("EMPANELED_DATE") != null)

                    insuranceVO.setCompanyStatus(TTKCommon.checkNull(rs.getString("ACTIVE_YN")));
                    insuranceVO.setActiveInactive(TTKCommon.checkNull(rs.getString("ACTIVE_YN")).equalsIgnoreCase("Y")?"Active":"Inactive");
                    resultList.add(insuranceVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)resultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "insurance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "insurance");
        }//end of catch (Exception exp)
        finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceComapanyList()",sqlExp);
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceComapanyList()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceComapanyList()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getInsuranceComapanyList(ArrayList alSearchObjects)

	/**
	 * This method returns the AraayList of InsuranceVO which contains the details of the HeadOffice and regional office and branch offices and regionael offices
	 * @param alSearchObjects ArrayList object which contains the search criteria
	 * @return AraayList of InsuranceVO which contains the details of the HeadOffice and regional office and branch offices and regionael offices
	 * @exception throws TTKException
	 */
	public ArrayList<Object> getCompanyDetails(ArrayList alSearchObjects) throws TTKException
	{
		String strStaticQuery = strGetCompanyDetails;
		StringBuffer sbfDynamicQuery = new StringBuffer();
		String strParentOfficeSeqID = (String) alSearchObjects.get(0);//INS_SEQ_ID OF THE PARENT OFFICE
		String strOfficeType = (String) alSearchObjects.get(1); //'IHO'  FOR HEAD OFFICE 'IRO'  FOR REGIONAL OFFICES  'IDO'  FOR DIVISIONAL OFFICES  'IBO'  FOR BRANCH OFFICES.
		String strStartRow = (String) alSearchObjects.get(2); //START ROW IN THE CASES PAGINATION NOT REQUIRED   --   REPLACE THE VALUES OF Q WITH Q ITSELF
		String strEndRow = (String) alSearchObjects.get(3); //START ROW IN THE CASES PAGINATION NOT REQUIRED   --   REPLACE THE VALUES OF Q WITH Q ITSELF
		strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"?", strParentOfficeSeqID);
		sbfDynamicQuery.append(" CONNECT BY PRIOR INS_SEQ_ID = INS_PARENT_SEQ_ID ) A JOIN TPA_GENERAL_CODE B ON (A.INS_SECTOR_GENERAL_TYPE_ID = B.GENERAL_TYPE_ID) WHERE INS_OFFICE_GENERAL_TYPE_ID =").append("'"+strOfficeType+"'").append(")A) WHERE Q >= ").append(strStartRow).append("  AND Q <=").append(strEndRow);
		strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();

		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		InsuranceVO insuranceVO = null;
		ArrayList<Object> alInsuranceList = null;
		try
		{
			conn = ResourceManager.getConnection();
			pStmt = conn.prepareStatement(strStaticQuery);

			rs = pStmt.executeQuery();
			if(rs!=null){
				alInsuranceList = new ArrayList<Object>();
				while(rs.next()){
					insuranceVO = new InsuranceVO();
					insuranceVO.setInsuranceSeqID(rs.getString("INS_SEQ_ID")!=null ? new Long(rs.getLong("INS_SEQ_ID")):null);
					insuranceVO.setTTKBranchCode(rs.getString("TPA_OFFICE_SEQ_ID")!=null ? new Long(rs.getString("TPA_OFFICE_SEQ_ID")):null);
					insuranceVO.setSectorTypeDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
					insuranceVO.setParentSeqID(rs.getString("INS_PARENT_SEQ_ID")!=null ? new Long(rs.getString("INS_PARENT_SEQ_ID")):null);
					insuranceVO.setSectorTypeCode(TTKCommon.checkNull(rs.getString("INS_SECTOR_GENERAL_TYPE_ID")));
					insuranceVO.setCompanyAbbreviation(TTKCommon.checkNull(rs.getString("ABBREVATION_CODE")));
					insuranceVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					insuranceVO.setCompanyCodeNbr(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
					insuranceVO.setOfficeType(TTKCommon.checkNull(rs.getString("INS_OFFICE_GENERAL_TYPE_ID")));
					insuranceVO.setBranchName(TTKCommon.checkNull(rs.getString("COMPANY")));
					alInsuranceList.add(insuranceVO);
				}//end of while(rs.next())
			}// End of  if(rs!=null)
			return alInsuranceList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "insurance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "insurance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in InsuranceDAOImpl getCompanyDetails()",sqlExp);
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in InsuranceDAOImpl getCompanyDetails()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in InsuranceDAOImpl getCompanyDetails()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//End of getCompanyDetails(ArrayList alSearchObjects)

	/**
	 * This method returns the InsuranceDetailVO, which contains all the insurancecompany details
	 * @param lInsuranceParentId is Insurance Company Parent Sequence ID
	 * @param lInsuranceCompanyID is  insurance company ID whih is of long type
	 * @return InsuranceDetailVO object which contains all the insuranceComapany details
	 * @exception throws TTKException
	 */
	public InsuranceDetailVO getInsuranceCompanyDetail(Long lInsuranceParentId,Long lInsuranceCompanyID) throws TTKException
	{
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		InsuranceDetailVO  insuranceDetailVO =new InsuranceDetailVO();
		AddressVO addressVO =new AddressVO();
		//added for Mail-SMS Template for Cigna
		MailNotificationVO mailnotificationVO = new MailNotificationVO();
		try
		{
			conn = ResourceManager.getConnection();
			pStmt = conn.prepareStatement(strInsurancelDetail);
			pStmt.setLong(1,lInsuranceParentId);  //head office ins_seq_id
			pStmt.setLong(2,lInsuranceCompanyID); //ins_seq_id of the office to be edited
			rs = pStmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					insuranceDetailVO.setInsuranceSeqID(lInsuranceCompanyID);
					insuranceDetailVO.setOfficeType(TTKCommon.checkNull(rs.getString("INS_OFFICE_GENERAL_TYPE_ID")));
					if (rs.getString("INS_PARENT_SEQ_ID")!=null)
					{
						insuranceDetailVO.setParentInsSeqID(new Long(rs.getLong("INS_PARENT_SEQ_ID")));
					}//end of if (rs.getString("INS_PARENT_SEQ_ID")!=null)
					if (rs.getString("TPA_OFFICE_SEQ_ID")!= null)
					{
						insuranceDetailVO.setTTKBranchCode(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					}//end of if (rs.getString("TPA_OFFICE_SEQ_ID")!= null)
					insuranceDetailVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					insuranceDetailVO.setCompanyStatus(TTKCommon.checkNull(rs.getString("ACTIVE_YN")));
					insuranceDetailVO.setCompanyCode(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
					insuranceDetailVO.setCompanyAbbreviation(TTKCommon.checkNull(rs.getString("ABBREVATION_CODE")));
					// Shortfall CR KOC1179
					insuranceDetailVO.setCompanyEmailID(TTKCommon.checkNull(rs.getString("EMAIL_ID")));
					// End Shortfall CR KOC1179
					insuranceDetailVO.setSectorTypeCode(TTKCommon.checkNull(rs.getString("INS_SECTOR_GENERAL_TYPE_ID")));
					insuranceDetailVO.setSectorTypeDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
					if(rs.getDate("EMPANELED_DATE")!=null)
					{
						insuranceDetailVO.setEmpanelmentDate(new java.util.Date(rs.getTimestamp("EMPANELED_DATE").getTime()));
					}//end of if(rs.getDate("EMPANELED_DATE")!=null)
					insuranceDetailVO.setFundDisbursalCode(TTKCommon.checkNull(rs.getString("DISB_TYPE_ID")));
					if (rs.getString("FLOAT_REPLENISHMENT_PERIOD")!=null)
					{
						insuranceDetailVO.setReplenishmentPeriod(new Long(rs.getLong("FLOAT_REPLENISHMENT_PERIOD")));
					}//end of if (rs.getString("FLOAT_REPLENISHMENT_PERIOD")!=null)
					insuranceDetailVO.setFrequencyCode(TTKCommon.checkNull(rs.getString("FREQUENCY_TYPE_ID")));
					if(rs.getString("ADDR_SEQ_ID") != null)
					{
						addressVO.setAddrSeqId(new Long(rs.getLong("ADDR_SEQ_ID")));
					}//end of if(rs.getString("ADDR_SEQ_ID") != null)
					insuranceDetailVO.setAuthStandard(rs.getString("PAYER_AUTHORITY"));
					addressVO.setAddress1(TTKCommon.checkNull(rs.getString("ADDRESS_1")));
					addressVO.setAddress2(TTKCommon.checkNull(rs.getString("ADDRESS_2")));
					addressVO.setAddress3(TTKCommon.checkNull(rs.getString("ADDRESS_3")));
					addressVO.setStateCode(TTKCommon.checkNull(rs.getString("STATE_TYPE_ID")));
					addressVO.setCityCode(TTKCommon.checkNull(rs.getString("CITY_TYPE_ID")));
					addressVO.setPinCode(TTKCommon.checkNull(rs.getString("PIN_CODE")));
					addressVO.setCountryCode(TTKCommon.checkNull(rs.getString("COUNTRY_ID")));
					addressVO.setIsdCode((Integer)TTKCommon.checkNull(rs.getInt("ISD_CODE")));
					addressVO.setStdCode((Integer)TTKCommon.checkNull(rs.getInt("STD_CODE")));
					addressVO.setPhoneNo1(TTKCommon.checkNull(rs.getString("OFFICE_PHONE1")));
					addressVO.setPhoneNo2(TTKCommon.checkNull(rs.getString("OFFICE_PHONE2")));
					//added for CR KOC Mail-SMS Notification for Cigna
					mailnotificationVO.setNotiTypeId(TTKCommon.checkNull(rs.getString("NOTIFY_TYPE_ID")));
					mailnotificationVO.setNotiEmailId(TTKCommon.checkNull(rs.getString("TO_MAIL_ID")));
					mailnotificationVO.setCcEmailId(TTKCommon.checkNull(rs.getString("CC_MAIL_ID")));
					insuranceDetailVO.setuserRestrictionGroup(TTKCommon.checkNull(rs.getString("user_group")));//KOC Cigna_insurance_resriction
					
					insuranceDetailVO.setInsCategory(TTKCommon.checkNull(rs.getString("PI_NONPI_INSURER")));
					
					insuranceDetailVO.setMailnotification(mailnotificationVO);
					//ended
			
					insuranceDetailVO.setAddress(addressVO);
				}//end of while(rs.next())
			}// End of  if(rs!=null)
			return insuranceDetailVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "insurance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "insurance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getInsuranceCompanyDetail(Long lInsuranceCompanyID)

	/**
	 * This method adds or updates the insurance details
	 * The method also calls other methods on DAO to insert/update the insurance details to the database
	 * @param insuranceCompanyVO InsuranceDetailVO object which contains the hospital details to be added/updated
	 * @return long value, Insurance Seq Id
	 * @exception throws TTKException
	 */
	public long addUpdateInsuranceCompany(InsuranceDetailVO insuranceDetailVO) throws TTKException
	{
		long lResult =0;
		AddressVO  addressVO = null;
		MailNotificationVO mailnotificationVO = null;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			addressVO = insuranceDetailVO.getAddress();
			//added for CR KOC Mail-SMS Notification
			mailnotificationVO = insuranceDetailVO.getMailnotification();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAddUpdateInsuranceInfo);
			if(insuranceDetailVO.getInsuranceSeqID() == null)
			{
				cStmtObject.setLong(INS_SEQ_ID,0);//INS_SEQ_ID
			}//end of if(insuranceDetailVO.getInsuranceSeqID() == null)
			else
			{
				cStmtObject.setLong(INS_SEQ_ID,insuranceDetailVO.getInsuranceSeqID());//INS_SEQ_ID
			}//end of else
			if(addressVO.getAddrSeqId()!=null)
			{
				cStmtObject.setLong(INS_ADDR_SEQ_ID,addressVO.getAddrSeqId());//INS_ADDR_SEQ_ID
			}//end of if(addressVO.getAddrSeqId()!=null)
			else
			{
				cStmtObject.setLong(INS_ADDR_SEQ_ID,0);
			}//end of else
			if (insuranceDetailVO.getTTKBranchCode()!= null)
			{
				cStmtObject.setLong(TPA_OFFICE_SEQ_ID,insuranceDetailVO.getTTKBranchCode());//TPA_OFFICE_SEQ_ID
			}//end of if (insuranceDetailVO.getTTKBranchCode()!= null)
			else
			{
				cStmtObject.setString(TPA_OFFICE_SEQ_ID,null);//TPA_OFFICE_SEQ_ID
			}//end of else
			cStmtObject.setString(INS_SECTOR_GENERAL_TYPE_ID,TTKCommon.checkNull(insuranceDetailVO.getSectorTypeCode()));//INS_SECTOR_GENERAL_TYPE_ID
			cStmtObject.setString(INS_OFFICE_GENERAL_TYPE_ID,TTKCommon.checkNull(insuranceDetailVO.getOfficeType()));//INS_OFFICE_GENERAL_TYPE_ID
			cStmtObject.setString(INS_COMP_NAME,TTKCommon.checkNull(insuranceDetailVO.getCompanyName()));//INS_COMP_NAME
			cStmtObject.setString(ABBREVATION_CODE,TTKCommon.checkNull(insuranceDetailVO.getCompanyAbbreviation()));//ABBREVATION_CODE
			cStmtObject.setString(INS_COMP_CODE_NUMBER,TTKCommon.checkNull(insuranceDetailVO.getCompanyCode()));//INS_COMP_CODE_NUMBER
			// Shortfall CR KOC1179
			cStmtObject.setString(INS_COMPANY_EMAILID,TTKCommon.checkNull(insuranceDetailVO.getCompanyEmailID()));//INS_COMPANY_EMAILID
			// End Shortfall CR KOC1179
			if(insuranceDetailVO.getEmpanelmentDate()!=null && !insuranceDetailVO.getEmpanelmentDate().equals(""))
			{
				cStmtObject.setTimestamp(EMPANELED_DATE,(new Timestamp(insuranceDetailVO.getEmpanelmentDate().getTime())));//EMPANELED_DATE
			}//end of if(insuranceDetailVO.getEmpanelmentDate()!=null && !insuranceDetailVO.getEmpanelmentDate().equals(""))
			else
			{
				cStmtObject.setTimestamp(EMPANELED_DATE,null);//EMPANELED_DATE
			}//end of else
			cStmtObject.setString(FREQUENCY_TYPE_ID,TTKCommon.checkNull(insuranceDetailVO.getFrequencyCode()));//FREQ_TYPE_ID
			cStmtObject.setString(DISB_TYPE_ID,TTKCommon.checkNull(insuranceDetailVO.getFundDisbursalCode()));//DISB_TYPE_ID
			if (insuranceDetailVO.getReplenishmentPeriod()== null)
			{
				cStmtObject.setLong(FLOAT_REPLENISHMENT_PERIOD,0);//FLOAT_REPLENISHMENT_PERIOD
			}//end of if (insuranceDetailVO.getReplenishmentPeriod()== null)
			else
			{
				cStmtObject.setLong(FLOAT_REPLENISHMENT_PERIOD,insuranceDetailVO.getReplenishmentPeriod());
			}//end of else
			cStmtObject.setString(ACTIVE_YN,TTKCommon.checkNull(insuranceDetailVO.getCompanyStatus()));//ACTIVE_YN
			if(insuranceDetailVO.getParentInsSeqID()!=null)
			{
				cStmtObject.setLong(INS_PARENT_SEQ_ID,(insuranceDetailVO.getParentInsSeqID()));//INS_PARENT_SEQ_ID
			}//end of if(insuranceDetailVO.getParentInsSeqID()!=null)
			else
			{
				cStmtObject.setString(INS_PARENT_SEQ_ID,null);//INS_PARENT_SEQ_ID
			}//end of else
			cStmtObject.setString(ADDRESS_1,TTKCommon.checkNull(addressVO.getAddress1()));//ADDRESS_1
			cStmtObject.setString(ADDRESS_2,TTKCommon.checkNull(addressVO.getAddress2()));//ADDRESS_2
			cStmtObject.setString(ADDRESS_3,TTKCommon.checkNull(addressVO.getAddress3()));//ADDRESS_3
			cStmtObject.setString(CITY_TYPE_ID,TTKCommon.checkNull(addressVO.getCityCode()));//CITY_TYPE_ID
			cStmtObject.setString(STATE_TYPE_ID,TTKCommon.checkNull(addressVO.getStateCode()));//STATE_TYPE_ID
			cStmtObject.setString(PIN_CODE,TTKCommon.checkNull(addressVO.getPinCode()));//PIN_CODE
			cStmtObject.setString(COUNTRY_ID,TTKCommon.checkNull(addressVO.getCountryCode()));//COUNTRY_ID
			cStmtObject.setString(LANDMARKS,null);//LANDMARKS
			//added for CR KOC Mail-SMS Notification
			cStmtObject.setString(NOTIFY_EMAIL_ID,mailnotificationVO.getNotiEmailId());	
			if(mailnotificationVO.getNotiTypeId()!=null)
			{
				cStmtObject.setString(NOTIFY_TYPE_ID,mailnotificationVO.getNotiTypeId());
			}
			else
			{
				cStmtObject.setString(NOTIFY_TYPE_ID,null);
			}
			cStmtObject.setString(CC_EMAIL_ID,mailnotificationVO.getCcEmailId());
		
			if (insuranceDetailVO.getOfficeSequenceID()!=null)
			{
				cStmtObject.setLong(INS_OFF_SEQ_ID,insuranceDetailVO.getOfficeSequenceID());//INS_OFF_SEQ_ID
			}//end of if (insuranceDetailVO.getOfficeSequenceID()!=null)
			else
			{
				cStmtObject.setLong(INS_OFF_SEQ_ID,0);
			}//end of else
			if(insuranceDetailVO.getStartDate()!=null && !insuranceDetailVO.getStartDate().equals(""))
			{
				cStmtObject.setTimestamp(VALID_FROM,new Timestamp(insuranceDetailVO.getStartDate().getTime()));//VALID_FROM
			}//end of if(insuranceDetailVO.getStartDate()!=null && !insuranceDetailVO.getStartDate().equals(""))
			else
			{
				cStmtObject.setTimestamp(VALID_FROM,null);
			}//end of else
			if(insuranceDetailVO.getEndDate()!=null && !insuranceDetailVO.getEndDate().equals(""))
			{
				cStmtObject.setTimestamp(VALID_TO,new Timestamp(insuranceDetailVO.getEndDate().getTime()));//VALID_TO
			}//end of if(insuranceDetailVO.getEndDate()!=null && !insuranceDetailVO.getEndDate().equals(""))
			else
			{
				cStmtObject.setTimestamp(VALID_TO,null);
			}//end of else
			cStmtObject.setLong(USER_SEQ_ID,insuranceDetailVO.getUpdatedBy()); //USER_SEQ_ID
			cStmtObject.setString(user_group ,TTKCommon.checkNull(insuranceDetailVO.getuserRestrictionGroup()));//KOC Cigna_insurance_resriction
			cStmtObject.setString(AUTHORITY_STANANDARD ,TTKCommon.checkNull(insuranceDetailVO.getAuthStandard()));//AUTHORITY STANDARD
			cStmtObject.setInt(ISD_CODE ,(Integer) TTKCommon.checkNull(addressVO.getIsdCode()));//ISD_CODE 
			cStmtObject.setInt(STD_CODE ,(Integer) TTKCommon.checkNull(addressVO.getStdCode()));// STD_CODE
			cStmtObject.setLong(PHONE1 ,TTKCommon.getLong(addressVO.getPhoneNo1()));//PHONE1 
			
			if(addressVO.getPhoneNo2().equals("Phone No2") || addressVO.getPhoneNo2().equals(" "))
				cStmtObject.setString(PHONE2,null);// PHONE2
			else
				cStmtObject.setString(PHONE2,TTKCommon.checkNull(addressVO.getPhoneNo2()));// PHONE2
			
			//cStmtObject.setLong(PHONE2,TTKCommon.getLong(TTKCommon.checkNull(addressVO.getPhoneNo2())));// PHONE2
			cStmtObject.setString(INSCATEGORY,TTKCommon.checkNull(insuranceDetailVO.getInsCategory()));// test INS CATEGORY)
			cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);//ROW_PROCESSED
			cStmtObject.registerOutParameter(INS_SEQ_ID,Types.BIGINT);//ROW_PROCESSED
			cStmtObject.execute();
			lResult = cStmtObject.getLong(INS_SEQ_ID);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "insurance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "insurance");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in InsuranceDAOImpl addUpdateInsuranceCompany()",sqlExp);
        			throw new TTKException(sqlExp, "insurance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in InsuranceDAOImpl addUpdateInsuranceCompany()",sqlExp);
        				throw new TTKException(sqlExp, "insurance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "insurance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return  lResult;
	}//  end of addUpdateInsuranceCompany(InsuranceDetailVO insuranceDetailVO) throws TTKException

	/**
	 * This method delete's the insurance records from the database.
	 * @param alInsuranceCompanyList ArrayList object which contains the insurance sequence id's to be deleted
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public  int deleteInsuranceCompany(String strInsSeqID) throws TTKException
	{
		int iResult =0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try
		{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteInsuranceInfo);
			cStmtObject.setString(1, strInsSeqID);//string of sequence id's which are separated with | as separator (Note: String should also end with | at the end)
			cStmtObject.registerOutParameter(2, Types.INTEGER);//out parameter which gives the number of records deleted
			cStmtObject.execute();
			iResult = cStmtObject.getInt(2);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "insurance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "insurance");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
        			throw new TTKException(sqlExp, "insurance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
        				throw new TTKException(sqlExp, "insurance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "insurance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of deleteInsuranceCompany(String strInsSeqID)
	
	public Long savePricingList(InsPricingVO insPricingVO) throws TTKException
	{
		Long iResult=null;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try
		{
			
			

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePricingList);
				
			if(insPricingVO.getGroupProfileSeqID() != null){
				cStmtObject.setLong(1,insPricingVO.getGroupProfileSeqID());
			}
			else{
				cStmtObject.setLong(1,0);
			}//end of else
			cStmtObject.setString(2,insPricingVO.getCurrency());
			cStmtObject.setString(3,insPricingVO.getAverageAge());		
			cStmtObject.setString(4,insPricingVO.getFamilyCoverage());
			cStmtObject.setString(5,insPricingVO.getGroupName());		
			cStmtObject.setString(6,insPricingVO.getNoofEmployees());
			cStmtObject.setString(7,insPricingVO.getEmployeeGender());
			cStmtObject.setString(8,insPricingVO.getAdditionalPremium());		
			cStmtObject.setString(9,insPricingVO.getGlobalCoverge());
			cStmtObject.setString(10,insPricingVO.getCountry());
			cStmtObject.setString(11,insPricingVO.getNameOfInsurer());
			cStmtObject.setString(12,insPricingVO.getNameOfTPA());		
			cStmtObject.setString(13,insPricingVO.getPlanName());
			cStmtObject.setString(14,insPricingVO.getEligibility());
			cStmtObject.setString(15,insPricingVO.getTakafulQuote());
			cStmtObject.setString(16,insPricingVO.getAreaOfCover());
			cStmtObject.setLong(17,insPricingVO.getAddedBy());
			cStmtObject.registerOutParameter(1,Types.INTEGER);
			cStmtObject.execute();	
			iResult = cStmtObject.getLong(1);	
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "insurance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "insurance");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
        			throw new TTKException(sqlExp, "insurance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null)
        					{
        					conn.close();
        					}
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
        				throw new TTKException(sqlExp, "insurance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "insurance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of deleteInsuranceCompany(String strInsSeqID)
	
	public InsPricingVO selectPricingList(Long lpricingSeqId) throws TTKException
	{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		InsPricingVO  insPricingVO =new InsPricingVO();
		AddressVO addressVO =new AddressVO();

		try
		{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectPricingList);
			cStmtObject.setLong(1,lpricingSeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs!=null){
				while(rs.next()){
					insPricingVO.setGroupProfileSeqID(lpricingSeqId);
					insPricingVO.setCurrency(TTKCommon.checkNull(rs.getString("CURRENCY_SEQ_ID")));					
					insPricingVO.setAverageAge(TTKCommon.checkNull(rs.getString("AVG_AGE_SEQ_ID")));
					insPricingVO.setFamilyCoverage(TTKCommon.checkNull(rs.getString("FMLY_COVG_TYPE_ID")));
					insPricingVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					insPricingVO.setNoofEmployees(TTKCommon.checkNull(rs.getString("NO_OF_EMP_ID")));
					insPricingVO.setEmployeeGender(TTKCommon.checkNull(rs.getString("GEN_BRK_MASTR_ID")));
					insPricingVO.setAdditionalPremium(TTKCommon.checkNull(rs.getString("ADD_PREMIUM")));				
					insPricingVO.setGlobalCoverge(rs.getString("GLOB_COVG_TYP_ID"));
					insPricingVO.setCountry(rs.getString("COUNTRY_TYP_ID"));
					
					insPricingVO.setNameOfInsurer(TTKCommon.checkNull(rs.getString("INSURER_TYPE_ID")));
					insPricingVO.setNameOfTPA(TTKCommon.checkNull(rs.getString("TPA_ID")));
					insPricingVO.setPlanName(TTKCommon.checkNull(rs.getString("PLAN_ID")));				
					insPricingVO.setEligibility(rs.getString("ELIGIBLE_ID"));
					insPricingVO.setTakafulQuote(rs.getString("TAKAFUL_YN"));
					insPricingVO.setAreaOfCover(rs.getString("AREA_COVERAGE"));

					/*if (rs.getString("ADDED_BY")!=null)
					{
						insPricingVO.setAddedBy(rs.getLong("ADDED_BY"));
					}
*/
			
			
	
				}//end of while(rs.next())
			}// End of  if(rs!=null)
			return insPricingVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "insurance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "insurance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) {
						rs.close();
					
                					
					}
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)
						{
							cStmtObject.close();
						}
							
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) {
	        					conn.close();
	                					}
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}
	
	
	 public ArrayList getProfileIncomeList(Long lpricingSeqId) throws TTKException {
	    	
	    	Collection<Object> alResultList = new ArrayList<Object>();
	    	Connection conn = null;
	    	PreparedStatement pStmt = null;
			ResultSet rs = null;
			InsPricingVO insPricingVO = null;	
			
	    	try{
	    		
	    		
	    		
	    		
	    		 conn = ResourceManager.getConnection();

		            pStmt = conn.prepareStatement(strProfileIncomeList);
		          
		            
		            rs = pStmt.executeQuery();
		            if(rs != null){
		                while (rs.next()) {
		                	insPricingVO = new InsPricingVO();
		                    
		                	insPricingVO.setProfileID1(TTKCommon.checkNull(rs.getString("id")));
		                	insPricingVO.setProfileValue1(TTKCommon.checkNull(rs.getString("val"))); 
		                	insPricingVO.setProfileGroup1(TTKCommon.checkNull(rs.getString("grp")));
		                    
		                    alResultList.add(insPricingVO);
	    		
	    		

				}
		            }
				return (ArrayList)alResultList;	
	    	}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (pStmt != null)	pStmt.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) {
		        					conn.close();
		                					}
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					pStmt = null;
					conn = null;
				}//end of finally
			}//end of finally
		}
	 
	 public int saveIncomeProfile(InsPricingVO insPricingVO) throws TTKException {
			int iResult = 0; 
			Connection conn = null;
	        CallableStatement cStmtObject=null; 
	        String ProfileID ="";
	        String ProfileValue ="";
	        String ProfileGroup ="";
	        String ProfilePercentage ="";
	        Long TransProfileSeqID= null;
	        try{
	        	conn = ResourceManager.getConnection(); 
	        	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveIncomeProfile);
	        	
	        	if(insPricingVO.getGroupProfileSeqID() != null) {
	                for(int i=0;i<insPricingVO.getProfileID().length;i++){
	                    if(!insPricingVO.getProfileID()[i].equals(""))//to
	                    {
	                    	ProfileID =insPricingVO.getProfileID()[i];
	                    }
	                    else
	                    {
	                    	ProfileID = null;
	                    }
	                    
	                    
	                    if(!insPricingVO.getProfileValue().equals(""))
	                    {
	                    	ProfileValue = insPricingVO.getProfileValue()[i]; 
	                    }
	                    else
	                    {
	                    	ProfileValue = null;
	                    }
	                    if(!insPricingVO.getProfileGroup().equals(""))
	                    {
	                    	ProfileGroup =insPricingVO.getProfileGroup()[i]; 
	                    }
	                    else
	                    {
	                    	ProfileGroup = null;
	                    }
	                    if(!insPricingVO.getProfilePercentage().equals(""))
	                    {
	                    	ProfilePercentage =insPricingVO.getProfilePercentage()[i]; 
	                    }
	                    else
	                    {
	                    	ProfilePercentage = null;
	                    }
	                    if(!insPricingVO.getTransProfileSeqID().equals(""))
	                    {
	                    	TransProfileSeqID =insPricingVO.getTransProfileSeqID()[i]; 
	                    }
	                    else
	                    {
	                    	TransProfileSeqID = null;
	                    }
	                 
	    	        	
	    	            if(TransProfileSeqID != null){
	    					cStmtObject.setLong(1,TransProfileSeqID);
	    				}
	    				else{
	    					cStmtObject.setLong(1,0);
	    				}
	    	            cStmtObject.setLong(2,insPricingVO.getGroupProfileSeqID()); 
	    	            cStmtObject.setString(3,ProfileID);
	    	            cStmtObject.setString(4,ProfileValue); 
	    	            cStmtObject.setString(5,ProfileGroup);
	    	            cStmtObject.setString(6,ProfilePercentage); 
	    	           // cStmtObject.registerOutParameter(1,Types.INTEGER);//ROW_PROCESSED 
	    	           // cStmtObject.registerOutParameter(2,Types.INTEGER);//ROW_PROCESSED 
	    	           
	    	            cStmtObject.addBatch();
	        }}
	        	 cStmtObject.executeBatch(); 
	        	
	        }//end of try
	        catch (SQLException sqlExp)
	        {
	            throw new TTKException(sqlExp, "maintenance");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp)
	        {
	            throw new TTKException(exp, "maintenance");
	        }//end of catch (Exception exp)
	        finally
			{
	        	/* Nested Try Catch to ensure resource closure */
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveGroupSpecificDesc()",sqlExp);
	        			throw new TTKException(sqlExp, "maintenance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null)  {
	        					conn.close();
	                					}
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Statement in MaintenanceDAOImpl saveGroupSpecificDesc()",sqlExp);
	        				throw new TTKException(sqlExp, "maintenance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "maintenance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			return iResult;
	}//end of saveGroupSpecificDesc(CriticalICDDetailVO criticalICDDetailVO)
	 
	 
	 
	 public ArrayList getProfileIncomeListvalue(Long lpricingSeqId) throws TTKException {
	    	
	    	Collection<Object> alResultList = new ArrayList<Object>();
			Connection conn = null;
	    	CallableStatement cStmtObject=null;
	        ResultSet rs = null;
	        Collection<Object> resultList = new ArrayList<Object>();
	        InsPricingVO insPricingVO = null;
	        try{
	        	conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strProfileIncomeListValue);
				cStmtObject.setLong(1,lpricingSeqId);
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
		            if(rs != null){
		                while (rs.next()) {
		                	insPricingVO = new InsPricingVO();
		                    
		                	insPricingVO.setProfileID1(TTKCommon.checkNull(rs.getString("id")));
		                	insPricingVO.setProfileValue1(TTKCommon.checkNull(rs.getString("val"))); 
		                	insPricingVO.setProfileGroup1(TTKCommon.checkNull(rs.getString("grp")));
		                	insPricingVO.setProfileGroupValue1(TTKCommon.checkNull(rs.getString("grp_show")));
		                	insPricingVO.setProfilePercentage1(TTKCommon.checkNull(rs.getString("perc")));
		                	insPricingVO.setTransProfileSeqID1(rs.getLong("tpa_grp_prof_tran_id"));
		                	insPricingVO.setGroupProfileSeqID(rs.getLong("grp_prof_seq_id"));
		                    
		                    alResultList.add(insPricingVO);
	    		
	    		

				}
		            }
				return (ArrayList)alResultList;	
	    	}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null)	cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) {
		        					conn.close();
		                					}
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}
	   
	 
	 public ArrayList getInsuranceProfileList(ArrayList alSearchObjects) throws TTKException {
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			Collection<Object> alResultList = new ArrayList<Object>();
			  InsPricingVO insPricingVO = null;

			try 
			{ 

					conn = ResourceManager.getConnection();				
					cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strInsuranceProfileList);
					cStmtObject.setString(1,(String) alSearchObjects.get(0));

					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs = (java.sql.ResultSet)cStmtObject.getObject(2);
					if(rs != null)
					{
						while (rs.next()) {
							insPricingVO= new InsPricingVO();
							if(rs.getString("GRP_PROF_SEQ_ID")!=null)
							{
								insPricingVO.setGroupProfileSeqID(new Long(rs.getLong("GRP_PROF_SEQ_ID")));
							}
							insPricingVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
							alResultList.add(insPricingVO);
							}//end of if(strIdentifier.equalsIgnoreCase("Administration"))
						
						}// End of   while (rs.next())
			
				
				return (ArrayList)alResultList;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null)	cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) {
		        					conn.close();
		                					}
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}
	 
	 public int savePlanDesignConfig(PolicyConfigVO policyConfigVO)throws TTKException {
			// TODO Auto-generated method stub  
			int iResult = 0;  
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    
	    
	    	try{
	    		conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveBenefitCoverage); 
	              
	              
	            if(policyConfigVO.getGroupProfileSeqID()!= null){
	            	cStmtObject.setLong(1,policyConfigVO.getGroupProfileSeqID());
	            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
	            else{
	            	cStmtObject.setLong(1,0);
	            }//end of else.
	            
/*GROUP LEVEL DESCRIPTION*/
	    		
	    		if(policyConfigVO.getGroupLevelYN() != null){
	            	cStmtObject.setString(2,policyConfigVO.getGroupLevelYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(2,null);
	            }//end of else
	            
				if(policyConfigVO.getInitialWatiingPeriod() != null){
					cStmtObject.setBigDecimal(3,policyConfigVO.getInitialWatiingPeriod());
				}//end of if(bufferDetailVO.getBufferAmt() != null)
				else{
					cStmtObject.setBigDecimal(3,null);
				}//end of else
				
				if(policyConfigVO.getNonNetworkRemCopayGroup() != null){
					cStmtObject.setString(4,policyConfigVO.getNonNetworkRemCopayGroup());
				}//end of if(bufferDetailVO.getBufferAmt() != null)
				else{
					cStmtObject.setString(4,null);
				}//end of else 
		    	
		    	if(policyConfigVO.getElectiveOutsideCover() != null){
					cStmtObject.setString(5,policyConfigVO.getElectiveOutsideCover());
				}//end of if(bufferDetailVO.getBufferAmt() != null)
				else{
					cStmtObject.setString(5,null);
				}//end of else
		    	
		    	if(policyConfigVO.getElectiveOutsideCoverDays() != null){
					cStmtObject.setBigDecimal(6,policyConfigVO.getElectiveOutsideCoverDays());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(6,0);
				}//end of else
		    	
				if(policyConfigVO.getTransportaionOverseasLimit() != null){
					cStmtObject.setBigDecimal(7,policyConfigVO.getTransportaionOverseasLimit());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(7,0);
				}//end of else
				
				if(policyConfigVO.getRepatriationRemainsLimit() != null){
					cStmtObject.setBigDecimal(8,policyConfigVO.getRepatriationRemainsLimit());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(8,0);
				}//end of else
	             
				if(policyConfigVO.getSpecialistConsultationReferal() != null){
	            	cStmtObject.setString(9,policyConfigVO.getSpecialistConsultationReferal());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(9,null);
	            }//end of else
				
				if(policyConfigVO.getEmergencyEvalAML() != null){
					cStmtObject.setBigDecimal(10,policyConfigVO.getEmergencyEvalAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(10,0);
				}//end of else
				
				  
				if(policyConfigVO.getInternationalMedicalAssis() != null){
	            	cStmtObject.setString(11,policyConfigVO.getInternationalMedicalAssis());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(11,null);
	            }//end of else
				
				  
				if(policyConfigVO.getSpecialistConsultationReferalGroup() != null){
	            	cStmtObject.setString(12,policyConfigVO.getSpecialistConsultationReferalGroup());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(12,null);
	            }//end of else 
		    	
		    	if(policyConfigVO.getPerLifeAML() != null){
					cStmtObject.setBigDecimal(13,policyConfigVO.getPerLifeAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(13,0);
				}//end of else
		    	
		    	if(policyConfigVO.getOutpatientCoverage() != null){
	            	cStmtObject.setString(14,policyConfigVO.getOutpatientCoverage());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(14,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getMaternityCopayGroup() != null){
	            	cStmtObject.setString(15,policyConfigVO.getMaternityCopayGroup());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(15,null);
	            }//end of else
		    	
		    	
		    	
		    	if(policyConfigVO.getGroundTransportaionPerc() != null){
					cStmtObject.setString(16,policyConfigVO.getGroundTransportaionPerc());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setString(16,null);
				}//end of else
		    	
		    	if(policyConfigVO.getGroundTransportaionNumeric() != null){
	            	cStmtObject.setBigDecimal(17,policyConfigVO.getGroundTransportaionNumeric());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setInt(17,0);
	            }//end of else
				
				
	
		    	if(policyConfigVO.getInsPatYN() != null){
	            	cStmtObject.setString(18,policyConfigVO.getInsPatYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(18,null);
	            }//end of else
				
		    	if(policyConfigVO.getAnnualMaxLimit() != null){
					cStmtObject.setBigDecimal(19,policyConfigVO.getAnnualMaxLimit());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(19,0);
				}//end of else
				
		    	if(policyConfigVO.getRoomType() != null){
	            	cStmtObject.setString(20,policyConfigVO.getRoomType());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(20,null);
	            }//end of else
				
		    	if(policyConfigVO.getCopay() != null){
	            	cStmtObject.setString(21,policyConfigVO.getCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(21,null);
	            }//end of else
				
		    	if(policyConfigVO.getCompanionBenefit() != null){
	            	cStmtObject.setString(22,policyConfigVO.getCompanionBenefit());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(22,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getCompanionBenefitAMl() != null){
					cStmtObject.setBigDecimal(23,policyConfigVO.getCompanionBenefitAMl());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(23,0);
				}
		    	
		    	if(policyConfigVO.getInpatientCashBenefit() != null){
	            	cStmtObject.setString(24,policyConfigVO.getInpatientCashBenefit());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(24,null);
	            }//end of else
		    	
		    	
		    	if(policyConfigVO.getCashBenefitAML() != null){
					cStmtObject.setBigDecimal(25,policyConfigVO.getCashBenefitAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(25,0);
				}
				if(policyConfigVO.getEmergencyDental() != null){
					cStmtObject.setBigDecimal(26,policyConfigVO.getEmergencyDental());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(26,0);
				}	
				if(policyConfigVO.getEmergencyMaternity() != null){
					cStmtObject.setBigDecimal(27,policyConfigVO.getEmergencyMaternity());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(27,0);
				}
				if(policyConfigVO.getAmbulance() != null){
	            	cStmtObject.setString(28,policyConfigVO.getAmbulance());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(28,null);
	            }//end of else
		    	
				if(policyConfigVO.getInpatientICU() != null){
	            	cStmtObject.setString(29,policyConfigVO.getInpatientICU());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(29,null);
	            }//end of else
				

		    	if(policyConfigVO.getPharmacyYN() != null){
	            	cStmtObject.setString(30,policyConfigVO.getPharmacyYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(30,null);
	            }//end of else
				
				
		    	if(policyConfigVO.getcopaypharmacy() != null){
	            	cStmtObject.setString(31,policyConfigVO.getcopaypharmacy());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(31,null);
	            }//end of else
				
		    	if(policyConfigVO.getAmlPharmacy() != null){
					cStmtObject.setBigDecimal(32,policyConfigVO.getAmlPharmacy());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(32,0);
				}
		    	
		    	if(policyConfigVO.getChronicAML() != null){
					cStmtObject.setBigDecimal(33,policyConfigVO.getChronicAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(33,0);
				}
		    	 
		    	if(policyConfigVO.getChronicPharmacyCopayPerc() != null){
	            	cStmtObject.setString(34,policyConfigVO.getChronicPharmacyCopayPerc());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(34,null);
	            }//end of else
				
		    	if(policyConfigVO.getChronicPharmacyCopayNum() != null){
					cStmtObject.setBigDecimal(35,policyConfigVO.getChronicPharmacyCopayNum());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(35,0);
				}
		    	
		    	
		    	if(policyConfigVO.getPreauthLimitVIP() != null){
					cStmtObject.setBigDecimal(36,policyConfigVO.getPreauthLimitVIP());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(36,0);
				}
				
		    	if(policyConfigVO.getNonNetworkRemCopay() != null){
	            	cStmtObject.setString(37,policyConfigVO.getNonNetworkRemCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(37,null);
	            }//end of else
				
		    	if(policyConfigVO.getPreauthLimitNonVIP() != null){
					cStmtObject.setBigDecimal(38,policyConfigVO.getPreauthLimitNonVIP());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(38,0);
				}
		    	
	
		    	if(policyConfigVO.getLabdiagYN() != null){
	            	cStmtObject.setString(39,policyConfigVO.getLabdiagYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(39,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getCopayLab() != null){
	            	cStmtObject.setString(40,policyConfigVO.getCopayLab());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(40,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getNonNetworkRemCopayLab() != null){
	            	cStmtObject.setString(41,policyConfigVO.getNonNetworkRemCopayLab());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(41,null);
	            }//end of else
		    	
		    	
		    	if(policyConfigVO.getOncologyTest() != null){
	            	cStmtObject.setString(42,policyConfigVO.getOncologyTest());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(42,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getOncologyTestAML() != null){
					cStmtObject.setBigDecimal(43,policyConfigVO.getOncologyTestAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(43,0);
				}
		    	
		    	if(policyConfigVO.getPreventiveScreeningDiabetics() != null){
	            	cStmtObject.setString(44,policyConfigVO.getPreventiveScreeningDiabetics());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(44,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getPreventiveScreenDiabeticsAnnual() != null){
	            	cStmtObject.setString(45,policyConfigVO.getPreventiveScreenDiabeticsAnnual());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(45,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getPreventiveScreeningDiabeticsAge() != null){
	            	cStmtObject.setString(46,policyConfigVO.getPreventiveScreeningDiabeticsAge());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(46,null);
	            }//end of else
		    	
	
		    	if(policyConfigVO.getOpConsultYN() != null){
	            	cStmtObject.setString(47,policyConfigVO.getOpConsultYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(47,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getConsultationAML() != null){
					cStmtObject.setBigDecimal(48,policyConfigVO.getConsultationAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(48,0);
				}
		    	
		    	if(policyConfigVO.getGpConsultationList() != null){
	            	cStmtObject.setString(49,policyConfigVO.getGpConsultationList());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(49,null);
	            }//end of else
		    	
		    	
		    	if(policyConfigVO.getGpConsultationNum() != null){
					cStmtObject.setBigDecimal(50,policyConfigVO.getGpConsultationNum());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(50,0);
				}
		    	
		    	if(policyConfigVO.getSpecConsultationList() != null){
	            	cStmtObject.setString(51,policyConfigVO.getSpecConsultationList());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(51,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getSpecConsultationNum() != null){
					cStmtObject.setBigDecimal(52,policyConfigVO.getSpecConsultationNum());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(52,0);
				}
		    	
		    	if(policyConfigVO.getPhysiotherapySession() != null){
					cStmtObject.setBigDecimal(53,policyConfigVO.getPhysiotherapySession());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(53,0);
				}
		    	
		    	if(policyConfigVO.getPhysiotherapyAMLLimit() != null){
					cStmtObject.setBigDecimal(54,policyConfigVO.getPhysiotherapyAMLLimit());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(54,0);
				}
		    	
		    	if(policyConfigVO.getNoOfmaternityConsults() != null){
					cStmtObject.setBigDecimal(55,policyConfigVO.getNoOfmaternityConsults());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(55,0);
				}
		    	
		    	if(policyConfigVO.getMaternityConsultations() != null){
	            	cStmtObject.setString(56,policyConfigVO.getMaternityConsultations());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(56,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getMaternityConsultationsNum() != null){
					cStmtObject.setBigDecimal(57,policyConfigVO.getMaternityConsultationsNum());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(57,0);
				}
		    	
		    	if(policyConfigVO.getChronicDiseaseConsults() != null){
					cStmtObject.setBigDecimal(58,policyConfigVO.getChronicDiseaseConsults());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(58,0);
				}
		    	
		    	if(policyConfigVO.getChronicDiseaseAML() != null){
					cStmtObject.setBigDecimal(59,policyConfigVO.getChronicDiseaseAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(59,0);
				}
		    	
		    	if(policyConfigVO.getChronicDiseaseCopay() != null){
	            	cStmtObject.setString(60,policyConfigVO.getChronicDiseaseCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(60,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getChronicDiseaseDeductible() != null){
					cStmtObject.setBigDecimal(61,policyConfigVO.getChronicDiseaseDeductible());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(61,0);
				}
		    	
	
		    	if(policyConfigVO.getMaternityYN() != null){
	            	cStmtObject.setString(62,policyConfigVO.getMaternityYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(62,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getNormalDeliveryAML() != null){
					cStmtObject.setBigDecimal(63,policyConfigVO.getNormalDeliveryAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(63,0);
				}
		    	
		    	if(policyConfigVO.getMaternityCsectionAML() != null){
					cStmtObject.setBigDecimal(64,policyConfigVO.getMaternityCsectionAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(64,0);
				}
		    	
		    	if(policyConfigVO.getDayCoverage() != null){
	            	cStmtObject.setString(65,policyConfigVO.getDayCoverage());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(65,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getDayCoverageVaccination() != null){
	            	cStmtObject.setString(66,policyConfigVO.getDayCoverageVaccination());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(66,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getMaternityComplicationAML() != null){
					cStmtObject.setBigDecimal(67,policyConfigVO.getMaternityComplicationAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(67,0);
				}

		    	if(policyConfigVO.getDentalYN() != null){
	            	cStmtObject.setString(68,policyConfigVO.getDentalYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(68,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getDentalAML() != null){
					cStmtObject.setBigDecimal(69,policyConfigVO.getDentalAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(69,0);
				}
		    	
		    	if(policyConfigVO.getDentalCopay() != null){
	            	cStmtObject.setString(70,policyConfigVO.getDentalCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(70,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getDentalDeductible() != null){
					cStmtObject.setBigDecimal(71,policyConfigVO.getDentalDeductible());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(71,0);
				}
		    	
		    	if(policyConfigVO.getCleaningAML() != null){
					cStmtObject.setBigDecimal(72,policyConfigVO.getCleaningAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(72,0);
				}
		    	
		    	if(policyConfigVO.getOrthodonticsAML() != null){
					cStmtObject.setBigDecimal(73,policyConfigVO.getOrthodonticsAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(73,0);
				}
		    	
		    	if(policyConfigVO.getDentalNonNetworkRem() != null){
	            	cStmtObject.setString(74,policyConfigVO.getDentalNonNetworkRem());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(74,null);
	            }//end of else
		    

		    	if(policyConfigVO.getChronicYN() != null){
	            	cStmtObject.setString(75,policyConfigVO.getChronicYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(75,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getChronicAMLNum() != null){
					cStmtObject.setBigDecimal(76,policyConfigVO.getChronicAMLNum());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(76,0);
				}
		    	
		    	if(policyConfigVO.getPharmacyAML() != null){
					cStmtObject.setBigDecimal(77,policyConfigVO.getPharmacyAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(77,0);
				}
		    	
		    	if(policyConfigVO.getPharmacyAMLCopay() != null){
	            	cStmtObject.setString(78,policyConfigVO.getPharmacyAMLCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(78,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getPharmacyAMLAmount() != null){
					cStmtObject.setBigDecimal(79,policyConfigVO.getPharmacyAMLAmount());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(79,0);
				}
		    	
		    	if(policyConfigVO.getChronicLabDiag() != null){
					cStmtObject.setBigDecimal(80,policyConfigVO.getChronicLabDiag());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(80,0);
				}
		    	
		    	if(policyConfigVO.getChronicLabDiagCopay() != null){
	            	cStmtObject.setString(81,policyConfigVO.getChronicLabDiagCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(81,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getChronicLabDiagAmount() != null){
					cStmtObject.setBigDecimal(82,policyConfigVO.getChronicLabDiagAmount());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(82,0);
				}
		    	
		    	if(policyConfigVO.getChronicConsultationAML() != null){
					cStmtObject.setBigDecimal(83,policyConfigVO.getChronicConsultationAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(83,0);
				}
		    	
		    	if(policyConfigVO.getChronicConsultationCopay() != null){
	            	cStmtObject.setString(84,policyConfigVO.getChronicConsultationCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(84,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getChronicConsultation() != null){
					cStmtObject.setBigDecimal(85,policyConfigVO.getChronicConsultation());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(85,0);
				}
		    	

		    	if(policyConfigVO.getPedYN() != null){
	            	cStmtObject.setString(86,policyConfigVO.getPedYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(86,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getCoveredPED() != null){
	            	cStmtObject.setString(87,policyConfigVO.getCoveredPED());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(87,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getPedAML() != null){
					cStmtObject.setBigDecimal(88,policyConfigVO.getPedAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(88,0);
				}
		    	
		    	if(policyConfigVO.getPedCopayDeductible() != null){
	            	cStmtObject.setString(89,policyConfigVO.getPedCopayDeductible());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(89,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getPedDeductible() != null){
					cStmtObject.setBigDecimal(90,policyConfigVO.getPedDeductible());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(90,0);
				}
		    	

		    	if(policyConfigVO.getPsychiatryYN() != null){
	            	cStmtObject.setString(91,policyConfigVO.getPsychiatryYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(91,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getInpatientAML() != null){
					cStmtObject.setBigDecimal(92,policyConfigVO.getInpatientAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(92,0);
				}
		    	
		    	if(policyConfigVO.getNoofInpatientDays() != null){
					cStmtObject.setBigDecimal(93,policyConfigVO.getNoofInpatientDays());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(93,0);
				}
		    	
		    	if(policyConfigVO.getOutpatientAML() != null){
					cStmtObject.setBigDecimal(94,policyConfigVO.getOutpatientAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(94,0);
				}
		    	
		    	if(policyConfigVO.getNoofOutpatientConsul() != null){
		    		  
					cStmtObject.setBigDecimal(95,policyConfigVO.getNoofOutpatientConsul());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					  
					cStmtObject.setInt(95,0);
				}
		    	
		    	if(policyConfigVO.getPsychiatryCopayDeduc() != null){
		    		  
	            	cStmtObject.setString(96,policyConfigVO.getPsychiatryCopayDeduc());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	  
	            	cStmtObject.setString(96,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getPsychiatryDeductible() != null){
		    		  
					cStmtObject.setBigDecimal(97,policyConfigVO.getPsychiatryDeductible());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					  
					cStmtObject.setInt(97,0);
				}
		    	

		    	
		    	if(policyConfigVO.getOthersYN() != null){
		    		  
	            	cStmtObject.setString(98,policyConfigVO.getOthersYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	  
	            	cStmtObject.setString(98,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getSystemOfMedicine() != null){
		    		  
	            	cStmtObject.setString(99,policyConfigVO.getSystemOfMedicine());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	  
	            	cStmtObject.setString(99,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getAlternativeAML() != null){
		    		  
					cStmtObject.setBigDecimal(100,policyConfigVO.getAlternativeAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					  
					cStmtObject.setInt(100,0);
				}
		    	
	/*OPTICAL*/	
		    	
		    	/*   //string
		    	  //s
		    	  //n 
		    	  //s
		    	  //n 
		    	  //s
		    	  */
		    
		    	if(policyConfigVO.getOpticalYN() != null){
	            	cStmtObject.setString(101,policyConfigVO.getOpticalYN());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(101,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getOpticalCopay() != null){
	            	cStmtObject.setString(102,policyConfigVO.getOpticalCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(102,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getFrameContactLensAML() != null){
					cStmtObject.setBigDecimal(103,policyConfigVO.getFrameContactLensAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(103,0);
				}
		    	
		    	if(policyConfigVO.getOpticalConsulCopay() != null){
	            	cStmtObject.setString(104,policyConfigVO.getOpticalConsulCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(104,null);
	            }//end of else
		    	
		    	if(policyConfigVO.getOpticalConsulAmount() != null){
					cStmtObject.setBigDecimal(105,policyConfigVO.getOpticalConsulAmount());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(105,0);
				}
		    	
		    	if(policyConfigVO.getOpticalNonNetworkRem() != null){
	            	cStmtObject.setString(106,policyConfigVO.getOpticalNonNetworkRem());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(106,null);
	            }//end of else
		    	
		    	
				
		    	if(policyConfigVO.getMaternityCopayGroupNumeric() != null){
					cStmtObject.setBigDecimal(107,policyConfigVO.getMaternityCopayGroupNumeric());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(107,0);
				}
		    	
		    	if(policyConfigVO.getInpatientcoverage() != null){
	            	cStmtObject.setString(108,policyConfigVO.getInpatientcoverage());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(108,null);
	            }
	            
		    	
		    	if(policyConfigVO.getInpatientICUAML() != null){
					cStmtObject.setBigDecimal(109,policyConfigVO.getInpatientICUAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(109,0);
				}
		    	if(policyConfigVO.getLabsAndDiagnosticsAML() != null){
					cStmtObject.setBigDecimal(110,policyConfigVO.getLabsAndDiagnosticsAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(110,0);
				}
		    	
		    	if(policyConfigVO.getOpConsultationCopayList() != null){
	            	cStmtObject.setBigDecimal(111,policyConfigVO.getOpConsultationCopayList());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setInt(111,0);
	            }//end of else
		    	
		    	
		    	
		    	if(policyConfigVO.getOpConsultationCopayListNum() != null){
					cStmtObject.setBigDecimal(112,policyConfigVO.getOpConsultationCopayListNum());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(112,0);
				}
		    	
		    
		    	if(policyConfigVO.getEmergencyMaternityAML() != null){
					cStmtObject.setBigDecimal(113,policyConfigVO.getEmergencyMaternityAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(113,0);
				}
		    	
		    	if(policyConfigVO.getMaternityAnteNatalTests() != null){
	            	cStmtObject.setString(114,policyConfigVO.getMaternityAnteNatalTests());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(114,null);
	            }//end of else
		    	
		    	
		    	if(policyConfigVO.getEmergencyDentalAML() != null){
					cStmtObject.setBigDecimal(115,policyConfigVO.getEmergencyDentalAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(115,0);
				}
		    	if(policyConfigVO.getOpticalAML() != null){
					cStmtObject.setBigDecimal(116,policyConfigVO.getOpticalAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(116,0);
				}
		    	if(policyConfigVO.getEmergencyOpticalAML() != null){
					cStmtObject.setBigDecimal(117,policyConfigVO.getEmergencyOpticalAML());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(117,0);
				}
		    	if(policyConfigVO.getChronicCoverage() != null){
	            	cStmtObject.setString(118,policyConfigVO.getChronicCoverage());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(118,null);
	            }
		    	if(policyConfigVO.getChronicCopayDeductibleCopay() != null){
	            	cStmtObject.setBigDecimal(119,policyConfigVO.getChronicCopayDeductibleCopay());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setInt(119,0);
	            }
		    	
		    	if(policyConfigVO.getChronicCopayDeductibleAmount() != null){
					cStmtObject.setBigDecimal(120,policyConfigVO.getChronicCopayDeductibleAmount());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setInt(120,0);
				}
		    	

		  
		    	if(policyConfigVO.getChineseMedicine() != null){
					cStmtObject.setString(121,policyConfigVO.getChineseMedicine());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setString(121,null);
				}
		    	
		    	

		    	
		    	if(policyConfigVO.getOsteopathyMedicine() != null){
					cStmtObject.setString(122,policyConfigVO.getOsteopathyMedicine());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setString(122,null);
				}
		    	if(policyConfigVO.getHomeopathyMedicine() != null){
					cStmtObject.setString(123,policyConfigVO.getHomeopathyMedicine());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setString(123,null);
				}
		    	
		    	if(policyConfigVO.getAyurvedaMedicine() != null){
					cStmtObject.setString(124,policyConfigVO.getAyurvedaMedicine());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setString(124,null);
				}
		    	if(policyConfigVO.getAccupunctureMedicine() != null){
					cStmtObject.setString(125,policyConfigVO.getAccupunctureMedicine());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setString(125,null);
				}
		    	if(policyConfigVO.getNaturopathyMedicine() != null){
					cStmtObject.setString(126,policyConfigVO.getNaturopathyMedicine());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setString(126,null);
				}
		    	
		    	if(policyConfigVO.getUnaniMedicine() != null){
					cStmtObject.setString(127,policyConfigVO.getUnaniMedicine());
				}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
				else{
					cStmtObject.setString(127,null);
				}
		    	
		    	
		    	
		    	
		    	
		    	
				cStmtObject.registerOutParameter(128, Types.INTEGER);
				cStmtObject.execute();
				iResult = cStmtObject.getInt(128);
	            
	            
	        }//end of try
	        catch (SQLException sqlExp) 
	        {
	            throw new TTKException(sqlExp, "policy");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp) 
	        {
	            throw new TTKException(exp, "policy");
	        }//end of catch (Exception exp) 
	        finally
			{
	        	/* Nested Try Catch to ensure resource closure */ 
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in PolicyDAOImpl saveConfigInsuranceApprove()",sqlExp);
	        			throw new TTKException(sqlExp, "policy");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) {
	        					conn.close();
	                			
	                					}
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PolicyDAOImpl saveConfigInsuranceApprove()",sqlExp);
	        				throw new TTKException(sqlExp, "policy");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "policy");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	        return iResult;
		}//end of saveConfigInsuranceApprove
		
	 
	 /**bajaj
		 * This method returns the InsuranceApproveVO which contains Copay Amount configuration Details
		 * @param long lngProdPolicySeqId
		 * @return InsuranceApproveVO which contains Domicilary configuration Details i.e Flag Enr or POL || Product
		 * @exception throws TTKException
		 */
	 
		 
		public PolicyConfigVO getPlanDesignConfigData(long webBoardId)throws TTKException {
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			String strResult = "";
			PolicyConfigVO policyConfigVO = null;
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetBenefitCoverage);
				cStmtObject.setLong(1,webBoardId);
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(3,OracleTypes.VARCHAR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
	
				strResult =(String)cStmtObject.getObject(3);
				policyConfigVO = new PolicyConfigVO(); 
				
			
				if(rs != null)
				{
					while(rs.next())
					{
						//policyConfigVO = new PolicyConfigVO(); 
						
						if(rs.getString("grp_prof_seq_id") != null){ 
							policyConfigVO.setGroupProfileSeqID(rs.getLong("grp_prof_seq_id"));
						}
						 
						//policyConfigVO.setBenefitTypeID(rs.getString("BENEFIT_TYPE_ID"));
						
						if(rs.getString("BENEFIT_TYPE_ID").equals("GRP"))
						{
							 
			
					    	policyConfigVO.setGroupLevelYN(TTKCommon.checkNull(rs.getString("covered_yn"))); 
					    	//policyConfigVO.setInitialWatiingPeriod(new BigDecimal(rs.getString("initial_wat_period_days"))); 
					    	
					    	if(rs.getString("initial_wat_period_days") != null){
					    		policyConfigVO.setInitialWatiingPeriod(new BigDecimal(rs.getString("initial_wat_period_days"))); 
							}
					    	else
					    	{
					    		policyConfigVO.setInitialWatiingPeriod(new BigDecimal("0")); 
					    	}
					    	policyConfigVO.setNonNetworkRemCopayGroup(TTKCommon.checkNull(rs.getString("non_nwk_reimb_copay_perc"))); 
					    	policyConfigVO.setElectiveOutsideCover(TTKCommon.checkNull(rs.getString("ELECTIVE_AREA_COVER"))); 
							
					    	if(rs.getString("ELECTIVE_AREA_COVER_DAYS") != null){
					    		policyConfigVO.setElectiveOutsideCoverDays(new BigDecimal(rs.getString("ELECTIVE_AREA_COVER_DAYS"))); 
							}
					    	else
					    	{
					    		policyConfigVO.setElectiveOutsideCoverDays(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("transp_over_limit") != null){
					    		policyConfigVO.setTransportaionOverseasLimit(new BigDecimal(rs.getString("transp_over_limit"))); 
							}
					    	else
					    	{
					    		policyConfigVO.setTransportaionOverseasLimit(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("repatr_remain_limit") != null){
					    		policyConfigVO.setRepatriationRemainsLimit(new BigDecimal(rs.getString("repatr_remain_limit")));
							}
					    	else
					    	{
					    		policyConfigVO.setRepatriationRemainsLimit(new BigDecimal("0")); 
					    	}
							
					    	policyConfigVO.setSpecialistConsultationReferal(TTKCommon.checkNull(rs.getString("work_rel_accident_cov"))); 
					    	if(rs.getString("emerg_eval_aml") != null){
					    		policyConfigVO.setEmergencyEvalAML(new BigDecimal(rs.getString("emerg_eval_aml")));
							}
					    	else
					    	{
					    		policyConfigVO.setEmergencyEvalAML(new BigDecimal("0")); 
					    	}
					    	policyConfigVO.setInternationalMedicalAssis(TTKCommon.checkNull(rs.getString("inter_med_assistance"))); 
					    	policyConfigVO.setSpecialistConsultationReferalGroup(TTKCommon.checkNull(rs.getString("spec_conslt_by_referal"))); 
					    	
					    	if(rs.getString("PER_LIFE_AML") != null){
					    		policyConfigVO.setPerLifeAML(new BigDecimal(rs.getString("PER_LIFE_AML")));
							}
					    	else
					    	{
					    		policyConfigVO.setPerLifeAML(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setOutpatientCoverage(TTKCommon.checkNull(rs.getString("GRP_OUT_PAT_COVER"))); 
					    	policyConfigVO.setMaternityCopayGroup(TTKCommon.checkNull(rs.getString("GRP_MAT_COPAY_PERC"))); 
					    	policyConfigVO.setGroundTransportaionPerc(TTKCommon.checkNull(rs.getString("GRP_GRND_TRANS_UAE_COPAY_PERC"))); 
					    	
					    	if(rs.getString("GRP_GRND_TRANS_UAE_COPAY_AMNT") != null){
					    		policyConfigVO.setGroundTransportaionNumeric(new BigDecimal(rs.getString("GRP_GRND_TRANS_UAE_COPAY_AMNT")));
							}
					    	else
					    	{
					    		policyConfigVO.setGroundTransportaionNumeric(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("grp_mat_copay_amount") != null){
					    		policyConfigVO.setMaternityCopayGroupNumeric(new BigDecimal(rs.getString("grp_mat_copay_amount")));
							}
					    	else
					    	{
					    		policyConfigVO.setMaternityCopayGroupNumeric(new BigDecimal("0")); 
					    	}
					    	
					    	
					    	 
						}	
						
		/*INPATIENT*/				
						if(rs.getString("BENEFIT_TYPE_ID").equals("IPT"))
						{ 
							
					
					    	policyConfigVO.setInsPatYN(TTKCommon.checkNull(rs.getString("covered_yn"))); 
					    	
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setAnnualMaxLimit(new BigDecimal(rs.getString("annual_max_limit")));
							}
					    	else
					    	{
					    		policyConfigVO.setAnnualMaxLimit(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setRoomType(TTKCommon.checkNull(rs.getString("room_type"))); 
					    	policyConfigVO.setCopay(TTKCommon.checkNull(rs.getString("copay_perc"))); 
					    	policyConfigVO.setCompanionBenefit(TTKCommon.checkNull(rs.getString("comp_benifit"))); 
					    	
					    	if(rs.getString("COMP_BENIFIT_AML_PER_NIGHT") != null){
					    		policyConfigVO.setCompanionBenefitAMl(new BigDecimal(rs.getString("COMP_BENIFIT_AML_PER_NIGHT")));
							}
					    	else
					    	{
					    		policyConfigVO.setCompanionBenefitAMl(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setInpatientCashBenefit(TTKCommon.checkNull(rs.getString("cash_benifit"))); 
					    	
					    	if(rs.getString("cash_benifit_aml") != null){
					    		policyConfigVO.setCashBenefitAML(new BigDecimal(rs.getString("cash_benifit_aml")));
							}
					    	else
					    	{
					    		policyConfigVO.setCashBenefitAML(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("emerg_dental") != null){
					    		policyConfigVO.setEmergencyDental(new BigDecimal(rs.getString("emerg_dental")));
							}
					    	else
					    	{
					    		policyConfigVO.setEmergencyDental(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("emerg_maternity") != null){
					    		policyConfigVO.setEmergencyMaternity(new BigDecimal(rs.getString("emerg_maternity")));
							}
					    	else
					    	{
					    		policyConfigVO.setEmergencyMaternity(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setAmbulance(TTKCommon.checkNull(rs.getString("ambulance"))); 
					    	policyConfigVO.setInpatientICU(TTKCommon.checkNull(rs.getString("icu"))); 
					    	policyConfigVO.setInpatientcoverage(TTKCommon.checkNull(rs.getString("inpatient_coverage")));
					    	if(rs.getString("inpatient_icu_aml") != null){
					    		policyConfigVO.setInpatientICUAML(new BigDecimal(rs.getString("inpatient_icu_aml")));
							}
					    	else
					    	{
					    		policyConfigVO.setInpatientICUAML(new BigDecimal("0")); 
					    	}
					    	 
					    	
						}
				    	
		/*PHARMACY*/				
						if(rs.getString("BENEFIT_TYPE_ID").equals("PHM"))
						{
							 
							
					    	policyConfigVO.setPharmacyYN(TTKCommon.checkNull(rs.getString("covered_yn"))); 
					    	policyConfigVO.setcopaypharmacy(TTKCommon.checkNull(rs.getString("copay_perc"))); 
					    	
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setAmlPharmacy(new BigDecimal(rs.getString("annual_max_limit")));
							}
					    	else
					    	{
					    		policyConfigVO.setAmlPharmacy(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("chronic_aml") != null){
					    		policyConfigVO.setChronicAML(new BigDecimal(rs.getString("chronic_aml")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicAML(new BigDecimal("0")); 
					    	}
					    	policyConfigVO.setChronicPharmacyCopayPerc(TTKCommon.checkNull(rs.getString("CHRONIC_PHM_COPAY_PERC"))); 
					    	
					    	if(rs.getString("CHRONIC_PHM_COPAY_AMOUNT") != null){
					    		policyConfigVO.setChronicPharmacyCopayNum(new BigDecimal(rs.getString("CHRONIC_PHM_COPAY_AMOUNT")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicPharmacyCopayNum(new BigDecimal("0")); 
					    	}
					    	
					    	
					    	if(rs.getString("pre_auth_limit_vip") != null){
					    		policyConfigVO.setPreauthLimitVIP(new BigDecimal(rs.getString("pre_auth_limit_vip")));
							}
					    	else
					    	{
					    		policyConfigVO.setPreauthLimitVIP(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setNonNetworkRemCopay(TTKCommon.checkNull(rs.getString("non_nwk_reimb_copay_perc"))); 
					    	
					    	if(rs.getString("pre_auth_limit_non_vip") != null){
					    		policyConfigVO.setPreauthLimitNonVIP(new BigDecimal(rs.getString("pre_auth_limit_non_vip")));
							}
					    	else
					    	{
					    		policyConfigVO.setPreauthLimitNonVIP(new BigDecimal("0")); 
					    	}
							
						}
						
	/*LAB AND DIAGNOSTICS*/
						if(rs.getString("BENEFIT_TYPE_ID").equals("LAB"))
						{  
					    	
					    	policyConfigVO.setLabdiagYN(TTKCommon.checkNull(rs.getString("covered_yn"))); 
					    	policyConfigVO.setCopayLab(TTKCommon.checkNull(rs.getString("copay_perc"))); 
					    	policyConfigVO.setNonNetworkRemCopayLab(TTKCommon.checkNull(rs.getString("non_nwk_reimb_copay_perc"))); 
					    	policyConfigVO.setOncologyTest(TTKCommon.checkNull(rs.getString("oncology_tests"))); 
					    	
					    	if(rs.getString("oncology_tests_aml") != null){
					    		policyConfigVO.setOncologyTestAML(new BigDecimal(rs.getString("oncology_tests_aml")));
							}
					    	else
					    	{
					    		policyConfigVO.setOncologyTestAML(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setPreventiveScreeningDiabetics(TTKCommon.checkNull(rs.getString("prev_diabetic_cov"))); 
					    	policyConfigVO.setPreventiveScreenDiabeticsAnnual(TTKCommon.checkNull(rs.getString("prev_diabetic_cov_days"))); 
					    	policyConfigVO.setPreventiveScreeningDiabeticsAge(TTKCommon.checkNull(rs.getString("prev_diabetic_age_limit"))); 
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setLabsAndDiagnosticsAML(new BigDecimal(rs.getString("annual_max_limit")));
							}
					    	else
					    	{
					    		policyConfigVO.setLabsAndDiagnosticsAML(new BigDecimal("0")); 
					    	}
					    	 
						}
		
		/*OP CONSULTATIONS*/ 				
						if(rs.getString("BENEFIT_TYPE_ID").equals("CON"))
						{
							 
					    	policyConfigVO.setOpConsultYN(TTKCommon.checkNull(rs.getString("covered_yn"))); 
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setConsultationAML(new BigDecimal(rs.getString("annual_max_limit")));
							}
					    	else
					    	{
					    		policyConfigVO.setConsultationAML(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setGpConsultationList(TTKCommon.checkNull(rs.getString("gp_consult_copay_perc"))); 
					    	
					    	if(rs.getString("gp_consult_copay_amount") != null){
					    		policyConfigVO.setGpConsultationNum(new BigDecimal(rs.getString("gp_consult_copay_amount")));
							}
					    	else
					    	{
					    		policyConfigVO.setGpConsultationNum(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setSpecConsultationList(TTKCommon.checkNull(rs.getString("spec_consult_copay_perc"))); 
					    	
					    	if(rs.getString("spec_consult_copay_amount") != null){
					    		policyConfigVO.setSpecConsultationNum(new BigDecimal(rs.getString("spec_consult_copay_amount")));
							}
					    	else
					    	{
					    		policyConfigVO.setSpecConsultationNum(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("phy_conslt_no_of_session") != null){
					    		policyConfigVO.setPhysiotherapySession(new BigDecimal(rs.getString("phy_conslt_no_of_session")));
							}
					    	else
					    	{
					    		policyConfigVO.setPhysiotherapySession(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("phy_conslt_aml_limit") != null){
					    		policyConfigVO.setPhysiotherapyAMLLimit(new BigDecimal(rs.getString("phy_conslt_aml_limit")));
							}
					    	else
					    	{
					    		policyConfigVO.setPhysiotherapyAMLLimit(new BigDecimal("0")); 
					    	}
					    	
					    	
					    	
					    	if(rs.getString("chronic_dise_no_of_consult") != null){
					    		policyConfigVO.setChronicDiseaseConsults(new BigDecimal(rs.getString("chronic_dise_no_of_consult")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicDiseaseConsults(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("chronic_dise_aml") != null){
					    		policyConfigVO.setChronicDiseaseAML(new BigDecimal(rs.getString("chronic_dise_aml")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicDiseaseAML(new BigDecimal("0")); 
					    	}
					    	
					    	
					    	policyConfigVO.setChronicDiseaseCopay(TTKCommon.checkNull(rs.getString("chronic_dise_copay_perc")));
					    	
					    	if(rs.getString("chronic_dise_copay_amount") != null){
					    		policyConfigVO.setChronicDiseaseDeductible(new BigDecimal(rs.getString("chronic_dise_copay_amount")));
							} 
					    	else
					    	{
					    		policyConfigVO.setChronicDiseaseDeductible(new BigDecimal("0")); 
					    	}
					    	
					    	//policyConfigVO.setOpConsultationCopayList(TTKCommon.checkNull(rs.getString("consultation_copay_perc")));
					    	if(rs.getString("consultation_copay_perc") != null){
					    		policyConfigVO.setOpConsultationCopayList(new BigDecimal(rs.getString("consultation_copay_perc")));
							} 
					    	else
					    	{
					    		policyConfigVO.setOpConsultationCopayList(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("consultation_copay_amount") != null){
					    		policyConfigVO.setOpConsultationCopayListNum(new BigDecimal(rs.getString("consultation_copay_amount")));
							} 
					    	else
					    	{
					    		policyConfigVO.setOpConsultationCopayListNum(new BigDecimal("0")); 
					    	}
					    	
						}
						
		/*MATERNITY*/
						if(rs.getString("BENEFIT_TYPE_ID").equals("MAT"))
						{
							 
					    	policyConfigVO.setMaternityYN(TTKCommon.checkNull(rs.getString("covered_yn")));
					    	
					    	if(rs.getString("nor_delivery_aml") != null){
					    		policyConfigVO.setNormalDeliveryAML(new BigDecimal(rs.getString("nor_delivery_aml")));
							} 
					    	else
					    	{
					    		policyConfigVO.setNormalDeliveryAML(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("c_section_aml") != null){
					    		policyConfigVO.setMaternityCsectionAML(new BigDecimal(rs.getLong("c_section_aml")));
							} 
					    	else
					    	{
					    		policyConfigVO.setMaternityCsectionAML(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setDayCoverage(TTKCommon.checkNull(rs.getString("day_1_coverage")));
					    	policyConfigVO.setDayCoverageVaccination(TTKCommon.checkNull(rs.getString("MAT_DAY_1_VACC_COVER")));
					    	
					    	if(rs.getString("mat_compl_aml") != null){
					    		policyConfigVO.setMaternityComplicationAML(new BigDecimal(rs.getString("mat_compl_aml")));
							} 
					    	else
					    	{
					    		policyConfigVO.setMaternityComplicationAML(new BigDecimal("0")); 
					    	}
					    	if(rs.getString("mat_emer_maternity_aml") != null){
					    		policyConfigVO.setEmergencyMaternityAML(new BigDecimal(rs.getString("mat_emer_maternity_aml")));
							} 
					    	else
					    	{
					    		policyConfigVO.setEmergencyMaternityAML(new BigDecimal("0")); 
					    	}
					    	policyConfigVO.setMaternityAnteNatalTests(TTKCommon.checkNull(rs.getString("maternity_antenatal_tests")));
					    	
					    	if(rs.getString("mat_conslt_no_of_consult") != null){
					    		policyConfigVO.setNoOfmaternityConsults(new BigDecimal(rs.getString("mat_conslt_no_of_consult")));
							}
					    	else
					    	{
					    		policyConfigVO.setNoOfmaternityConsults(new BigDecimal("0")); 
					    	}
					    	
					    	
					    	policyConfigVO.setMaternityConsultations(TTKCommon.checkNull(rs.getString("mat_consult_copay_perc")));
					    	
					    	if(rs.getString("mat_consult_copay_amount") != null){
					    		policyConfigVO.setMaternityConsultationsNum(new BigDecimal(rs.getString("mat_consult_copay_amount")));
							}
					    	else
					    	{
					    		policyConfigVO.setMaternityConsultationsNum(new BigDecimal("0")); 
					    	}
					    	
						}
						
		/*DENTAL*/	    	
				    	
						if(rs.getString("BENEFIT_TYPE_ID").equals("DNT"))
						{
					    	 
					    	policyConfigVO.setDentalYN(TTKCommon.checkNull(rs.getString("covered_yn")));
					    	
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setDentalAML(new BigDecimal(rs.getString("annual_max_limit")));
							} 
					    	else
					    	{
					    		policyConfigVO.setDentalAML(new BigDecimal("0")); 
					    	}
				    	
					    	policyConfigVO.setDentalCopay(TTKCommon.checkNull(rs.getString("copay_perc")));
				    	
					    	if(rs.getString("copay_amount") != null){
					    		policyConfigVO.setDentalDeductible(new BigDecimal(rs.getString("copay_amount")));
							} 
					    	else
					    	{
					    		policyConfigVO.setDentalDeductible(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("cleaning_aml") != null){
					    		policyConfigVO.setCleaningAML(new BigDecimal(rs.getString("cleaning_aml")));
							} 
					    	else
					    	{
					    		policyConfigVO.setCleaningAML(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("oth_aml") != null){
					    		policyConfigVO.setOrthodonticsAML(new BigDecimal(rs.getString("oth_aml")));
							} 
					    	else
					    	{
					    		policyConfigVO.setOrthodonticsAML(new BigDecimal("0")); 
					    	}
				    	
					    	policyConfigVO.setDentalNonNetworkRem(TTKCommon.checkNull(rs.getString("non_nwk_remb")));
					    	
					    	if(rs.getString("emergency_dental_aml") != null){
					    		policyConfigVO.setEmergencyDentalAML(new BigDecimal(rs.getString("emergency_dental_aml")));
							} 
					    	else
					    	{
					    		policyConfigVO.setEmergencyDentalAML(new BigDecimal("0")); 
					    	}
					    	 
				    	
						}
				    	
	/*CHRONIC*/	 
						if(rs.getString("BENEFIT_TYPE_ID").equals("CHO"))
						{ 
					    	policyConfigVO.setChronicYN(TTKCommon.checkNull(rs.getString("covered_yn"))); 
				    	
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setChronicAMLNum(new BigDecimal(rs.getString("annual_max_limit")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicAMLNum(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("pharmacy_aml") != null){
					    		policyConfigVO.setPharmacyAML(new BigDecimal(rs.getString("pharmacy_aml")));
							} 
					    	else
					    	{
					    		policyConfigVO.setPharmacyAML(new BigDecimal("0")); 
					    	}
				    	
					    	policyConfigVO.setPharmacyAMLCopay(TTKCommon.checkNull(rs.getString("pharmacy_copay_perc"))); 
					    	
					    	if(rs.getString("pharmacy_copay_amount") != null){
					    		policyConfigVO.setPharmacyAMLAmount(new BigDecimal(rs.getString("pharmacy_copay_amount")));
							}
					    	else
					    	{
					    		policyConfigVO.setPharmacyAMLAmount(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("lab_diagnostic") != null){
					    		policyConfigVO.setChronicLabDiag(new BigDecimal(rs.getString("lab_diagnostic")));
							} 
					    	else
					    	{
					    		policyConfigVO.setChronicLabDiag(new BigDecimal("0")); 
					    	}
					    	
				    	
					    	policyConfigVO.setChronicLabDiagCopay(TTKCommon.checkNull(rs.getString("lab_diagnostic_copay_perc"))); 
					    	
					    	if(rs.getString("lab_diagnostic_copay_amount") != null){
					    		policyConfigVO.setChronicLabDiagAmount(new BigDecimal(rs.getString("lab_diagnostic_copay_amount")));
							} 
					    	else
					    	{
					    		policyConfigVO.setChronicLabDiagAmount(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("consultation") != null){
					    		policyConfigVO.setChronicConsultationAML(new BigDecimal(rs.getString("consultation")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicConsultationAML(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setChronicConsultationCopay(TTKCommon.checkNull(rs.getString("consultation_copay_perc"))); 
					    	
					    	if(rs.getString("consultation_copay_amount") != null){
					    		policyConfigVO.setChronicConsultation(new BigDecimal(rs.getString("consultation_copay_amount")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicConsultation(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setChronicCoverage(TTKCommon.checkNull(rs.getString("chronic_coverage")));
					    	//policyConfigVO.setChronicCopayDeductibleCopay(TTKCommon.checkNull(rs.getString("copay_perc")));
					    	if(rs.getString("copay_amount") != null){
					    		policyConfigVO.setChronicCopayDeductibleCopay(new BigDecimal(rs.getString("copay_perc")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicCopayDeductibleCopay(new BigDecimal("0")); 
					    	}
					    	
					    	if(rs.getString("copay_amount") != null){
					    		policyConfigVO.setChronicCopayDeductibleAmount(new BigDecimal(rs.getString("copay_amount")));
							}
					    	else
					    	{
					    		policyConfigVO.setChronicCopayDeductibleAmount(new BigDecimal("0")); 
					    	}
					    	
					    	 
						}
				    	
						/*PED*/	   
						if(rs.getString("BENEFIT_TYPE_ID").equals("PED"))
						{ 
					    	
					    	policyConfigVO.setPedYN(TTKCommon.checkNull(rs.getString("covered_yn"))); 
					    	policyConfigVO.setCoveredPED(TTKCommon.checkNull(rs.getString("ped_covered"))); 
				    	
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setPedAML(new BigDecimal(rs.getString("annual_max_limit")));
							} 
					    	else
					    	{
					    		policyConfigVO.setPedAML(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setPedCopayDeductible(TTKCommon.checkNull(rs.getString("copay_perc"))); 
					    	
					    	if(rs.getString("copay_amount") != null){
					    		policyConfigVO.setPedDeductible(new BigDecimal(rs.getString("copay_amount")));
							} 
					    	else
					    	{
					    		policyConfigVO.setPedDeductible(new BigDecimal("0")); 
					    	}
				    	 
						}
						
	/*PSHYCHIATRY*/	    	
						if(rs.getString("BENEFIT_TYPE_ID").equals("PSY"))
						{
				    	policyConfigVO.setPsychiatryYN(TTKCommon.checkNull(rs.getString("covered_yn"))); 
				    	
				    	if(rs.getString("ip_aml") != null){
				    		policyConfigVO.setInpatientAML(new BigDecimal(rs.getString("ip_aml")));
						} 
				    	else
				    	{
				    		policyConfigVO.setInpatientAML(new BigDecimal("0")); 
				    	}
				    	
				    	if(rs.getString("ip_days") != null){
				    		policyConfigVO.setNoofInpatientDays(new BigDecimal(rs.getString("ip_days")));
						} 
				    	else
				    	{
				    		policyConfigVO.setNoofInpatientDays(new BigDecimal("0")); 
				    	}
				    	
				    	if(rs.getString("op_aml") != null){
				    		policyConfigVO.setOutpatientAML(new BigDecimal(rs.getString("op_aml")));
						}
				    	else
				    	{
				    		policyConfigVO.setOutpatientAML(new BigDecimal("0")); 
				    	}
				    	
				    	if(rs.getString("op_no_of_consult") != null){
				    		policyConfigVO.setNoofOutpatientConsul(new BigDecimal(rs.getString("op_no_of_consult")));
						} 
				    	else
				    	{
				    		policyConfigVO.setNoofOutpatientConsul(new BigDecimal("0")); 
				    	}
				    	
				    	policyConfigVO.setPsychiatryCopayDeduc(TTKCommon.checkNull(rs.getString("copay_perc"))); 
				    	
				    	if(rs.getString("copay_amount") != null){
				    		policyConfigVO.setPsychiatryDeductible(new BigDecimal(rs.getString("copay_amount")));
						} 
				    	else
				    	{
				    		policyConfigVO.setPsychiatryDeductible(new BigDecimal("0")); 
				    	}
				    	
				   
						}
	/*ALTERNATIVE MEDICINES*/			    	
				    	if(rs.getString("BENEFIT_TYPE_ID").equals("ALT"))
						{
				    		 
					    	policyConfigVO.setOthersYN(TTKCommon.checkNull(rs.getString("covered_yn")));
					    	policyConfigVO.setSystemOfMedicine(TTKCommon.checkNull(rs.getString("ALTR_MED_CHIROPCTRY")));
					    	policyConfigVO.setOsteopathyMedicine(TTKCommon.checkNull(rs.getString("ALTR_MED_OSTEPTHY")));
					    	policyConfigVO.setHomeopathyMedicine(TTKCommon.checkNull(rs.getString("ALTR_MED_HOMEOPATHY")));
					    	policyConfigVO.setAyurvedaMedicine(TTKCommon.checkNull(rs.getString("ALTR_MED_AYRVED")));
					    	policyConfigVO.setAccupunctureMedicine(TTKCommon.checkNull(rs.getString("ALTR_MED_ACCUPUNTURE")));
					    	policyConfigVO.setNaturopathyMedicine(TTKCommon.checkNull(rs.getString("ALTR_MED_NATUROPTHY")));
					    	policyConfigVO.setUnaniMedicine(TTKCommon.checkNull(rs.getString("ALTR_MED_UNANI")));
					    	policyConfigVO.setChineseMedicine(TTKCommon.checkNull(rs.getString("ALTR_MED_CHINSE")));
					    	
				
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setAlternativeAML(new BigDecimal(rs.getString("annual_max_limit")));
							}  
					    	else
					    	{
					    		policyConfigVO.setAlternativeAML(new BigDecimal("0")); 
					    	}
				    		
						}
				    	
	/*OPTICAL*/	
				    	if(rs.getString("BENEFIT_TYPE_ID").equals("OPT"))
						{
					    	
					    	
					    	policyConfigVO.setOpticalYN(TTKCommon.checkNull(rs.getString("covered_yn")));
					    	policyConfigVO.setOpticalCopay(TTKCommon.checkNull(rs.getString("copay_perc")));
					    	
					    	
					    	if(rs.getString("frm_contact_lense_aml") != null){
					    		policyConfigVO.setFrameContactLensAML(new BigDecimal(rs.getString("frm_contact_lense_aml")));
							}  
					    	else
					    	{
					    		policyConfigVO.setFrameContactLensAML(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setOpticalConsulCopay(TTKCommon.checkNull(rs.getString("consultation_copay_perc")));
					    	
					    	
					    	if(rs.getString("consultation_copay_amount") != null){
					    		policyConfigVO.setOpticalConsulAmount(new BigDecimal(rs.getString("consultation_copay_amount")));
							}  
					    	else
					    	{
					    		policyConfigVO.setOpticalConsulAmount(new BigDecimal("0")); 
					    	}
					    	
					    	policyConfigVO.setOpticalNonNetworkRem(TTKCommon.checkNull(rs.getString("non_nwk_remb"))); 
					    	if(rs.getString("annual_max_limit") != null){
					    		policyConfigVO.setOpticalAML(new BigDecimal(rs.getString("annual_max_limit")));
							}  
					    	else
					    	{
					    		policyConfigVO.setOpticalAML(new BigDecimal("0")); 
					    	}
					    	if(rs.getString("emergency_optical_aml") != null){
					    		policyConfigVO.setEmergencyOpticalAML (new BigDecimal(rs.getString("emergency_optical_aml")));
							}  
					    	else
					    	{
					    		policyConfigVO.setEmergencyOpticalAML(new BigDecimal("0")); 
					    	}
					    	
						} 
				    	
						
						 
						//denial process
					}//end of while(rs.next())
				}//end of (rs != null)
				policyConfigVO.setShowflag(strResult);
				
				return policyConfigVO;	// TODO Auto-generated method stub
		 }//end of try
		catch (SQLException sqlExp) 
		{
			throw new TTKException(sqlExp, "policy");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp) 
		{
			throw new TTKException(exp, "policy");
		}//end of catch (Exception exp) 
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PolicyDAOImpl getPlanDesignConfigData()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getPlanDesignConfigData()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) {
								conn.close();
										}
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getPlanDesignConfigData()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getDomicilaryConfig(long lngProdPolicySeqId)
		
		public PolicyConfigVO saveGenerateQuote(PolicyConfigVO policyConfigVO)throws TTKException
		{
			Connection conn = null;
			CallableStatement cStmtObject=null;
			try
			{


				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveGenerateQuote);
					
				if(policyConfigVO.getGroupProfileSeqID() != null){
					cStmtObject.setLong(1,policyConfigVO.getGroupProfileSeqID());
				}
				else{
					cStmtObject.setLong(1,0);
				}//end of else
				cStmtObject.setBigDecimal(2,policyConfigVO.getAdminCost());
				cStmtObject.setBigDecimal(3,policyConfigVO.getManagementExpenses());		
				cStmtObject.setBigDecimal(4,policyConfigVO.getCommission());
				cStmtObject.setBigDecimal(5,policyConfigVO.getCostOfCapital());		
				cStmtObject.setBigDecimal(6,policyConfigVO.getProfitMargin());
				cStmtObject.setString(9,policyConfigVO.getReinSwitch());
				cStmtObject.setString(10,policyConfigVO.getSignatory());
				cStmtObject.registerOutParameter(7,OracleTypes.VARCHAR);
				cStmtObject.registerOutParameter(8,OracleTypes.VARCHAR);
				//cStmtObject.registerOutParameter(9,OracleTypes.CURSOR);
				cStmtObject.execute();
				String finalamount=(String)cStmtObject.getString(7);
				policyConfigVO.setFinalAmount(finalamount);
				policyConfigVO.setGenerateflag(cStmtObject.getString(8));
				
			
				
				//policyConfigVO=new PolicyConfigVO();
				
				return policyConfigVO;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
	        	/* Nested Try Catch to ensure resource closure */ 
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        			throw new TTKException(sqlExp, "insurance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        				throw new TTKException(sqlExp, "insurance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "insurance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			
		}//end of deleteInsuranceCompany(String strInsSeqID)
		
	
		public  Object[] selectGenerateQuote(long webBoardId)throws TTKException
		{
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			ResultSet rs1 = null;
			ResultSet rs2 = null;
			//PolicyConfigVO policyConfigVO =null;
			PolicyConfigVO policyConfigVO =new PolicyConfigVO();
			Object[] premiumProposal=new Object[3];
			ArrayList<PolicyConfigVO> PremiumFirst=new ArrayList<PolicyConfigVO>();
			ArrayList<PolicyConfigVO> PremiumSecond=new ArrayList<PolicyConfigVO>();

			try
			{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectGenerateQuote);
				cStmtObject.setLong(1,webBoardId);
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
				rs1 = (java.sql.ResultSet)cStmtObject.getObject(4);
				rs2 = (java.sql.ResultSet)cStmtObject.getObject(3);
				if(rs!=null){
				
					while(rs.next()){
						/*policyConfigVO.setGroupProfileSeqID(webBoardId);
						policyConfigVO.setAgeQuote(TTKCommon.checkNull(rs.getString("age_band")));					
						policyConfigVO.setCensusQuote(TTKCommon.checkNull(rs.getString("mid_emp")));
						policyConfigVO.setPremiumQuote(TTKCommon.checkNull(rs.getString("final_amt")));
						policyConfigVO.setTotalPremiumQuote(TTKCommon.checkNull(rs.getString("Premium")));
						policyConfigVO.setTotalPremiumQuote(TTKCommon.checkNull(rs.getString("Premium")));*/
						String ageQuote=rs.getString("age_band")==null?"":rs.getString("age_band");
						String censusQuote=rs.getString("mid_emp")==null?"":rs.getString("mid_emp");
						String premiumQuote=rs.getString("final_amt")==null?"":rs.getString("final_amt");
						String totalPremiumQuote=rs.getString("Premium")==null?"":rs.getString("Premium");
						String memberType=rs.getString("member_type")==null?"":rs.getString("member_type");
						
						


						PremiumFirst.add(new PolicyConfigVO(ageQuote,censusQuote,premiumQuote,totalPremiumQuote,memberType));
						
		
					}//end of while(rs.next())
				}// End of  if(rs!=null)
				if(rs1!=null){
					
					while(rs1.next()){
						
						policyConfigVO.setGroupProfileSeqID(webBoardId);
						policyConfigVO.setAdminCost(TTKCommon.getBigDecimal(rs1.getString("admin_perc")));
						policyConfigVO.setManagementExpenses(TTKCommon.getBigDecimal(rs1.getString("mangmnt_perc")));	
						policyConfigVO.setCommission(TTKCommon.getBigDecimal(rs1.getString("comission_perc")));
						policyConfigVO.setCostOfCapital(TTKCommon.getBigDecimal(rs1.getString("cost_cap_perc")));
						policyConfigVO.setProfitMargin(TTKCommon.getBigDecimal(rs1.getString("profit_margin")));
						policyConfigVO.setReinSwitch(TTKCommon.checkNull(rs1.getString("rein_swtch")));
						policyConfigVO.setLessFourthousand(TTKCommon.checkNull(rs1.getString("less_4000")));
						policyConfigVO.setMoreFourthousand(TTKCommon.checkNull(rs1.getString("more_4000")));
						policyConfigVO.setSignatory(TTKCommon.checkNull(rs1.getString("sign_name_id")));
						
						
		
					}//end of while(rs.next())
				}// End of  if(rs!=null)
				
				if(rs2!=null){
					while(rs2.next()){
						
								
						/*policyConfigVO.setGroupProfileSeqID(webBoardId);
						policyConfigVO.setAgeQuoteFamily(TTKCommon.checkNull(rs2.getString("age_band")));					
						policyConfigVO.setCensusQuoteFamily(TTKCommon.checkNull(rs2.getString("mid_emp")));
						policyConfigVO.setPremiumQuoteFamily(TTKCommon.checkNull(rs2.getString("final_amt")));
						policyConfigVO.setTotalPremiumQuoteFamily(TTKCommon.checkNull(rs2.getString("Premium")));*/
						String ageQuoteFamily=rs2.getString("age_band")==null?"":rs2.getString("age_band");
						String censusQuoteFamily=rs2.getString("mid_emp")==null?"":rs2.getString("mid_emp");
						String premiumQuoteFamily=rs2.getString("final_amt")==null?"":rs2.getString("final_amt");
						String totalPremiumQuoteFamily=rs2.getString("Premium")==null?"":rs2.getString("Premium");
						String memberTypeFamily=rs2.getString("member_type")==null?"":rs2.getString("member_type");
						String generateflag="y";
						
						


						PremiumSecond.add(new PolicyConfigVO(ageQuoteFamily,censusQuoteFamily,premiumQuoteFamily,totalPremiumQuoteFamily,memberTypeFamily,generateflag));
				
					
		
					}//end of while(rs.next())
				}// End of  if(rs!=null)
				
				premiumProposal[0]=policyConfigVO;
				premiumProposal[1]=PremiumFirst;
				premiumProposal[2]=PremiumSecond;
				
				return premiumProposal;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) 
							{
							rs.close();
							}
						if (rs1 != null) {
							rs1.close();
						}
						if (rs2 != null){ rs2.close();
						
						}
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null)
								{
								cStmtObject.close();
								}
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) 	{
									conn.close();
											}
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					rs1 = null;
					rs2 = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}
		
		
		//Sw Pricing Started
		public Long swSavePricingList(InsPricingVO insPricingVO) throws TTKException
		{
			Long iResult=null;
			Connection conn = null;
			CallableStatement cStmtObject=null;
			try
			{
				

				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strswSavePricingList);
					
				if(insPricingVO.getGroupProfileSeqID() != null){
					cStmtObject.setLong(1,insPricingVO.getGroupProfileSeqID());
				}
				else{
					cStmtObject.setLong(1,0);
				}//end of else
				cStmtObject.setString(2,insPricingVO.getRenewalYN());
				cStmtObject.setString(3,insPricingVO.getClientCode());		
				cStmtObject.setString(4,insPricingVO.getPreviousPolicyNo());
			
				if(insPricingVO.getCoverStartDate() != null){
					cStmtObject.setTimestamp(5,new Timestamp(insPricingVO.getCoverStartDate().getTime()));//LETTER_DATE
	            }//end of if(batchVO.getLetterDate() != null)
				else{
					cStmtObject.setTimestamp(5, null);//LETTER_DATE
	            }//end of else
				if(insPricingVO.getCoverEndDate() != null){
					cStmtObject.setTimestamp(6,new Timestamp(insPricingVO.getCoverEndDate().getTime()));//LETTER_DATE
	            }//end of if(batchVO.getLetterDate() != null)
				else{
					cStmtObject.setTimestamp(6, null);//LETTER_DATE
	            }//end of else
				cStmtObject.setString(7,insPricingVO.getTotalCovedLives());
			
				if(!("").equals(insPricingVO.getTotalLivesMaternity()))
				{
				cStmtObject.setString(8,insPricingVO.getTotalLivesMaternity());
				}else{
				cStmtObject.setString(8,null);
				}
				
				cStmtObject.setString(9,insPricingVO.getTrendFactor());
				
				cStmtObject.setString(10,insPricingVO.getMaternityYN());
				cStmtObject.setString(11,insPricingVO.getDentalYN());
				cStmtObject.setString(12,insPricingVO.getDentalLivesYN());		
				cStmtObject.setString(13,insPricingVO.getOpticalYN());
				cStmtObject.setString(14,insPricingVO.getOpticalLivesYN());
				cStmtObject.setString(15,insPricingVO.getPhysiocoverage());
				cStmtObject.setString(16,insPricingVO.getVitDcoverage());
				cStmtObject.setString(17,insPricingVO.getVaccImmuCoverage());
				cStmtObject.setString(18,insPricingVO.getMatComplicationCoverage());
				cStmtObject.setString(19,insPricingVO.getPsychiatrycoverage());
				cStmtObject.setString(20,insPricingVO.getDeviatedNasalSeptum());
				cStmtObject.setString(21,insPricingVO.getObesityTreatment());
				
			
				cStmtObject.setString(22,insPricingVO.getAreaOfCoverList());
				cStmtObject.setString(23,insPricingVO.getNetworkList());		
				cStmtObject.setString(24,insPricingVO.getMaxBenifitList());
				if(!("").equals(insPricingVO.getMaxBeneLimitOth()))
				{
				cStmtObject.setString(25,insPricingVO.getMaxBeneLimitOth());
				}else{
				cStmtObject.setLong(25,0);
				}
				
				cStmtObject.setString(26,insPricingVO.getDentalLimitList());
				
				if(!("").equals(insPricingVO.getDentalLimitOth())){
					cStmtObject.setString(27,insPricingVO.getDentalLimitOth());
				}
				else{
					cStmtObject.setLong(27,0);
				}
				cStmtObject.setString(28,insPricingVO.getMaternityLimitList());
				
				if(!insPricingVO.getMaternityLimitOth().equals("")){
				cStmtObject.setString(29,insPricingVO.getMaternityLimitOth());
				}else{
					cStmtObject.setLong(29,0);

				}
				cStmtObject.setString(30,insPricingVO.getOpticalLimitList());
				
				if(!insPricingVO.getOpticalLimitOth().equals("")){
				cStmtObject.setString(31,insPricingVO.getOpticalLimitOth());
				}else{
					cStmtObject.setLong(31,0);

				}
				cStmtObject.setString(32,insPricingVO.getOpCopayList());
				cStmtObject.setString(33,insPricingVO.getOpCopayListDesc());
				cStmtObject.setString(34,insPricingVO.getDentalcopayList());
				cStmtObject.setString(35,insPricingVO.getDentalcopayListDesc());
				
				cStmtObject.setString(36,insPricingVO.getOpticalCopayList());
				cStmtObject.setString(36,insPricingVO.getOpticalCopayList());
				
				cStmtObject.setString(37,insPricingVO.getOpticalCopayListDesc());
				cStmtObject.setString(38,insPricingVO.getOpDeductableList());
			
				cStmtObject.setString(39,insPricingVO.getOpDeductableListDesc());
				cStmtObject.setLong(40,insPricingVO.getAddedBy());
				
				cStmtObject.setString(41,insPricingVO.getComments());
				
				//System.out.println("attachement name----"+insPricingVO.getAttachmentname1());
				cStmtObject.setString(42,insPricingVO.getAttachmentname1());
				byte[] iStream1	=	insPricingVO.getSourceAttchments1().getFileData();
				cStmtObject.setBytes(43, iStream1);
				
				cStmtObject.setString(44,insPricingVO.getAttachmentname2());
				byte[] iStream2	=	insPricingVO.getSourceAttchments2().getFileData();
				cStmtObject.setBytes(45, iStream2);
				
				cStmtObject.setString(46,insPricingVO.getAttachmentname3());
				byte[] iStream3	=	insPricingVO.getSourceAttchments3().getFileData();
				cStmtObject.setBytes(47, iStream3);
				
				cStmtObject.setString(48,insPricingVO.getAttachmentname4());
				byte[] iStream4	=	insPricingVO.getSourceAttchments4().getFileData();
				cStmtObject.setBytes(49, iStream4);
				
				cStmtObject.setString(50,insPricingVO.getAttachmentname5());
				byte[] iStream5	=	insPricingVO.getSourceAttchments5().getFileData();
				cStmtObject.setBytes(51, iStream5);
				//2nd phase
				cStmtObject.setString(52,insPricingVO.getInpatientBenefit());
				cStmtObject.setString(53,insPricingVO.getOutpatientBenefit());
				cStmtObject.setString(54,insPricingVO.getGastricBinding());
				cStmtObject.setString(55,insPricingVO.getHealthScreen());
				cStmtObject.setString(56,insPricingVO.getOrthodonticsCopay());
				cStmtObject.setString(57,insPricingVO.getOrthodonticsCopayDesc());
				
				cStmtObject.setString(58,insPricingVO.getOpdeductableserviceYN());
				cStmtObject.setString(59,insPricingVO.getOpCopaypharmacy());
				
				cStmtObject.setString(60,insPricingVO.getOpCopaypharmacyDesc());
				cStmtObject.setString(61,insPricingVO.getOpCopyconsultn());
				cStmtObject.setString(62,insPricingVO.getOpCopyconsultnDesc());
				cStmtObject.setString(63,insPricingVO.getOpInvestigation());
				cStmtObject.setString(64,insPricingVO.getOpInvestigationDesc());
				cStmtObject.setString(65,insPricingVO.getOpCopyothers());
				cStmtObject.setString(66,insPricingVO.getOpCopyothersDesc());					
				cStmtObject.setString(67,insPricingVO.getAlAhlihospital());
				cStmtObject.setString(68,insPricingVO.getAlAhlihospOPservices());
				
				cStmtObject.setString(69,insPricingVO.getOpCopyalahlihosp());
				cStmtObject.setString(70,insPricingVO.getOpCopyalahlihospDesc());				
				cStmtObject.setString(71,insPricingVO.getOpPharmacyAlAhli());
				cStmtObject.setString(72,insPricingVO.getOpPharmacyAlAhliDesc());
				cStmtObject.setString(73,insPricingVO.getOpConsultAlAhli());
				cStmtObject.setString(74,insPricingVO.getOpConsultAlAhliDesc());				
				cStmtObject.setString(75,insPricingVO.getOpInvestnAlAhli());
				cStmtObject.setString(76,insPricingVO.getOpInvestnAlAhliDesc());
				cStmtObject.setString(77,insPricingVO.getOpothersAlAhli());
				cStmtObject.setString(78,insPricingVO.getOpothersAlAhliDesc());
				cStmtObject.setString(79,insPricingVO.getOpticalFrameLimitList());
				cStmtObject.setString(80,insPricingVO.getIpCopay());
				cStmtObject.setString(81,insPricingVO.getIpCopayPerc());
				cStmtObject.setString(82,insPricingVO.getIpCopayAtAlAhli());
				cStmtObject.setString(83,insPricingVO.getIpCopayAtAlAhliPerc());
				cStmtObject.setString(84,insPricingVO.getMaternityCopay());
				cStmtObject.setString(85,insPricingVO.getMaternityCopayPerc());
				cStmtObject.registerOutParameter(86,Types.INTEGER);
				cStmtObject.registerOutParameter(1,Types.INTEGER);
				cStmtObject.execute();	
				iResult = cStmtObject.getLong(1);
				
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
	        	/* Nested Try Catch to ensure resource closure */ 
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        			throw new TTKException(sqlExp, "insurance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null)
	        					{
	        					conn.close();
	        					}
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        				throw new TTKException(sqlExp, "insurance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "insurance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			return iResult;
		}//end of deleteInsuranceCompany(String strInsSeqID)
		
		public InsPricingVO swSelectPricingList(Long lpricingSeqId) throws TTKException
		{
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			InsPricingVO  insPricingVO =new InsPricingVO();
			InputStream iStream1	=	new ByteArrayInputStream(new String("").getBytes());
			InputStream iStream2	=	new ByteArrayInputStream(new String("").getBytes());
			InputStream iStream3	=	new ByteArrayInputStream(new String("").getBytes());
			InputStream iStream4	=	new ByteArrayInputStream(new String("").getBytes());
			InputStream iStream5	=	new ByteArrayInputStream(new String("").getBytes());

			Blob blob1	=	null;Blob blob2	=	null;Blob blob3	=	null;Blob blob4	=	null;Blob blob5	=	null;
			
			try
			{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strswSelectPricingList);
				cStmtObject.setLong(1,lpricingSeqId);
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
				if(rs!=null){
					while(rs.next()){
						insPricingVO.setGroupProfileSeqID(lpricingSeqId);
						insPricingVO.setRenewalYN(TTKCommon.checkNull(rs.getString("RENEWAL_YN")));	
						insPricingVO.setPricingRefno(TTKCommon.checkNull(rs.getString("REF_NO")));	
						insPricingVO.setClientCode(TTKCommon.checkNull(rs.getString("CLIENT_CODE")));
						insPricingVO.setClientName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
						insPricingVO.setPreviousPolicyNo(TTKCommon.checkNull(rs.getString("PREV_POLICY_NO")));
					
						insPricingVO.setCoverStartDate(rs.getString("COVRG_START_DATE")!=null ? new Date(rs.getTimestamp("COVRG_START_DATE").getTime()):null);
						insPricingVO.setCoverEndDate(rs.getString("COVRG_END_DATE")!=null ? new Date(rs.getTimestamp("COVRG_END_DATE").getTime()):null);

						//insPricingVO.setCoverStartDate(new Date(rs.getTimestamp("COVRG_START_DATE").getTime()));
						//insPricingVO.setCoverEndDate(new Date(rs.getTimestamp("COVRG_END_DATE").getTime()));
						insPricingVO.setTotalCovedLives(TTKCommon.checkNull(rs.getString("TOT_COV_LIVES")));
												
						insPricingVO.setTotalLivesMaternity(TTKCommon.checkNull(rs.getString("TOT_COV_LIVES_MATRNTY")));	
						

						insPricingVO.setTrendFactor(TTKCommon.checkNull(rs.getString("TRND_FACTOR_PERC")));
						insPricingVO.setMaternityYN(TTKCommon.checkNull(rs.getString("MAT_COV_YN")));						
						insPricingVO.setDentalYN(TTKCommon.checkNull(rs.getString("DNTL_COV_YN")));
						insPricingVO.setDentalLivesYN(TTKCommon.checkNull(rs.getString("DNTL_COV_ALL_LIVES_YN")));
						insPricingVO.setOpticalYN(TTKCommon.checkNull(rs.getString("OPTCL_COV_YN")));				
						insPricingVO.setOpticalLivesYN(TTKCommon.checkNull(rs.getString("OPTCL_COV_ALL_LIVES_YN")));
						insPricingVO.setPhysiocoverage(TTKCommon.checkNull(rs.getString("PSYTHRPY_COV_YN")));
						insPricingVO.setVitDcoverage(TTKCommon.checkNull(rs.getString("VITAMIN_D_COV_YN")));
						
						insPricingVO.setVaccImmuCoverage(TTKCommon.checkNull(rs.getString("VACC_IMMUN_COV_YN")));
						insPricingVO.setMatComplicationCoverage(TTKCommon.checkNull(rs.getString("MAT_COMPLCTION_COV_YN")));
						insPricingVO.setPsychiatrycoverage(TTKCommon.checkNull(rs.getString("PSYCHRTRY_COV_YN")));				
						insPricingVO.setDeviatedNasalSeptum(TTKCommon.checkNull(rs.getString("DEVTD_NASL_SEPTM_COV_YN")));
						insPricingVO.setObesityTreatment(TTKCommon.checkNull(rs.getString("OBSTY_TRTMNT_COV_YN")));
						insPricingVO.setAreaOfCoverList(TTKCommon.checkNull(rs.getString("AREA_TYPE_SEQ_ID")));
						
						insPricingVO.setNetworkList(TTKCommon.checkNull(rs.getString("NWK_TYPE_SEQ_ID")));
						insPricingVO.setMaxBenifitList(TTKCommon.checkNull(rs.getString("MAX_BENF_LIMIT_SEQ_ID")));
						insPricingVO.setMaxBeneLimitOth(TTKCommon.checkNull(rs.getString("MAX_BENF_OTH_LIMIT")));				
						insPricingVO.setDentalLimitList(TTKCommon.checkNull(rs.getString("DENTL_LIMIT_SEQ_ID")));
						
						insPricingVO.setDentalLimitOth(TTKCommon.checkNull(rs.getString("DENTL_OTH_LIMIT")));
						insPricingVO.setMaternityLimitList(TTKCommon.checkNull(rs.getString("MAT_LIMIT_SEQ_ID")));
						insPricingVO.setMaternityLimitOth(TTKCommon.checkNull(rs.getString("MAT_OTH_LIMIT")));
					
						insPricingVO.setOpticalLimitList(TTKCommon.checkNull(rs.getString("OPTCL_LIMIT_SEQ_ID")));
						insPricingVO.setOpticalLimitOth(TTKCommon.checkNull(rs.getString("OPTCL_OTH_LIMIT")));				
				
						insPricingVO.setOpCopayList(TTKCommon.checkNull(rs.getString("OPT_COPAY_TYPE_SEQ_ID")));
						insPricingVO.setOpCopayListDesc(TTKCommon.checkNull(rs.getString("OPT_COPAY_PERC")));
						insPricingVO.setDentalcopayList(TTKCommon.checkNull(rs.getString("DNT_COPAY_TYPE_SEQ_ID")));
						insPricingVO.setDentalcopayListDesc(TTKCommon.checkNull(rs.getString("DNT_COPAY_PERC")));				
						insPricingVO.setOpticalCopayList(TTKCommon.checkNull(rs.getString("OPTCL_COPAY_TYPE_SEQ_ID")));
						insPricingVO.setOpticalCopayListDesc(TTKCommon.checkNull(rs.getString("OPTCL_COPAY_PERC")));
						insPricingVO.setOpDeductableList(TTKCommon.checkNull(rs.getString("OPT_DEDUCT_TYPE_SEQ_ID")));
						insPricingVO.setOpDeductableListDesc(TTKCommon.checkNull(rs.getString("OPT_DEDUCTBLE")));
						insPricingVO.setComments(rs.getString("COVRG_REMARKS"));
						
						/*if(rs.getBlob("INPT_SRC_DOC1") != null){
							insPricingVO.setPricingDocs("Y");
							
						}else{
							insPricingVO.setPricingDocs("N");
						}*/
						
						blob1	=	rs.getBlob("INPT_SRC_DOC1") ;
						if(blob1!= null){
							iStream1	=	blob1.getBinaryStream();
							insPricingVO.setInputstreamdoc1(iStream1);
						}
						blob2	=	rs.getBlob("INPT_SRC_DOC2") ;
						if(blob2!= null){
							iStream2	=	blob2.getBinaryStream();
							insPricingVO.setInputstreamdoc2(iStream2);
						}
						
						blob3	=	rs.getBlob("INPT_SRC_DOC3") ;
						if(blob3!= null){
							iStream3	=	blob3.getBinaryStream();
							insPricingVO.setInputstreamdoc3(iStream3);
						}
						
						
						blob4	=	rs.getBlob("INPT_SRC_DOC4") ;
						if(blob4!= null){
							iStream4	=	blob4.getBinaryStream();
							insPricingVO.setInputstreamdoc4(iStream4);
						}
						
						blob5	=	rs.getBlob("INPT_SRC_DOC5") ;
						if(blob5!= null){
							iStream5	=	blob5.getBinaryStream();
							insPricingVO.setInputstreamdoc5(iStream5);
						}
						
						insPricingVO.setAttachmentname1(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC1_NAME")));
						insPricingVO.setAttachmentname2(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC2_NAME")));
						insPricingVO.setAttachmentname3(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC3_NAME")));
						insPricingVO.setAttachmentname4(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC4_NAME")));
						insPricingVO.setAttachmentname5(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC5_NAME")));
						insPricingVO.setCompleteSaveYN(TTKCommon.checkNull(rs.getString("COMP_SAVE_YN")));//complete save = Y ; else N
						//2nd phase
						insPricingVO.setPolicycategory(TTKCommon.checkNull(rs.getString("PREV_POL_PRODUCT_NAME")));
						insPricingVO.setInpatientBenefit(TTKCommon.checkNull(rs.getString("IP_COV_YN")));
						insPricingVO.setOutpatientBenefit(TTKCommon.checkNull(rs.getString("OP_COV_YN")));
						insPricingVO.setGastricBinding(TTKCommon.checkNull(rs.getString("GASTRC_COV_YN")));
						insPricingVO.setHealthScreen(TTKCommon.checkNull(rs.getString("HLTH_COV_YN")));
						insPricingVO.setOrthodonticsCopay(TTKCommon.checkNull(rs.getString("ORTHO_COPAY_TYPE_SEQ_ID")));
						insPricingVO.setOrthodonticsCopayDesc(TTKCommon.checkNull(rs.getString("ORTHO_COPAY_PERC")));
						insPricingVO.setOpdeductableserviceYN(TTKCommon.checkNull(rs.getString("OP_CPY_DEDCT_ALL_OP_YN")));
						insPricingVO.setOpCopaypharmacy(TTKCommon.checkNull(rs.getString("OP_COPAY_PHM_TYPE_SEQ_ID")));
						insPricingVO.setOpCopaypharmacyDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_PHM_PERC")));
						insPricingVO.setOpCopyconsultn(TTKCommon.checkNull(rs.getString("OP_COPAY_CON_TYPE_SEQ_ID")));
						insPricingVO.setOpCopyconsultnDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_CON_PERC")));
						insPricingVO.setOpInvestigation(TTKCommon.checkNull(rs.getString("OP_COPAY_INVST_TYPE_SEQ_ID")));
						insPricingVO.setOpInvestigationDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_INVST_PERC")));
						insPricingVO.setOpCopyothers(TTKCommon.checkNull(rs.getString("OP_COPAY_OTH_TYPE_SEQ_ID")));
						insPricingVO.setOpCopyothersDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_OTH_PERC")));
						insPricingVO.setAlAhlihospital(TTKCommon.checkNull(rs.getString("ALHALLI_COV_YN")));
						insPricingVO.setAlAhlihospOPservices(TTKCommon.checkNull(rs.getString("OP_CPY_ALHALLI_ALL_OP_YN")));
						insPricingVO.setOpCopyalahlihosp(TTKCommon.checkNull(rs.getString("OP_COPAY_ALH_TYPE_SEQ_ID")));
						insPricingVO.setOpCopyalahlihospDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_ALH_PERC")));
						insPricingVO.setOpPharmacyAlAhli(TTKCommon.checkNull(rs.getString("OP_COPAY_PHM_ALH_TYPE_SEQ_ID")));
						insPricingVO.setOpPharmacyAlAhliDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_PHM_ALH_PERC")));
						insPricingVO.setOpConsultAlAhli(TTKCommon.checkNull(rs.getString("OP_COPAY_CON_ALH_TYPE_SEQ_ID")));
						insPricingVO.setOpConsultAlAhliDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_CON_ALH_PERC")));
						insPricingVO.setOpInvestnAlAhli(TTKCommon.checkNull(rs.getString("OP_COPAY_INVST_ALH_TYPE_SEQ_ID")));
						insPricingVO.setOpInvestnAlAhliDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_INVST_ALH_PERC")));
						insPricingVO.setOpothersAlAhli(TTKCommon.checkNull(rs.getString("OP_COPAY_OTH_ALH_TYPE_SEQ_ID")));
						insPricingVO.setOpothersAlAhliDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_OTH_ALH_PERC")));
						insPricingVO.setAlertmsgscreen1(TTKCommon.checkNull(rs.getString("POL_CNT_MSG")));
						insPricingVO.setOpticalFrameLimitList(TTKCommon.checkNull(rs.getString("OPTCL_FRAMES_LIMIT_SEQ_ID")));
						insPricingVO.setIpCopay(TTKCommon.checkNull(rs.getString("IP_COPAY_TYPE_SEQ_ID")));
						insPricingVO.setIpCopayPerc(TTKCommon.checkNull(rs.getString("IP_COPAY_PERC")));
						insPricingVO.setIpCopayAtAlAhli(TTKCommon.checkNull(rs.getString("IP_COPAY_ALH_TYPE_SEQ_ID")));
						insPricingVO.setIpCopayAtAlAhliPerc(TTKCommon.checkNull(rs.getString("IP_COPAY_ALH_PERC")));
						insPricingVO.setMaternityCopay(TTKCommon.checkNull(rs.getString("MAT_COPAY_TYPE_SEQ_ID")));
						insPricingVO.setMaternityCopayPerc(TTKCommon.checkNull(rs.getString("MAT_COPAY_PERC")));
					}//end of while(rs.next())
				}// End of  if(rs!=null)
				return insPricingVO;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) {
							rs.close();
						
	                					
						}
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null)
							{
								cStmtObject.close();
							}
								
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) {
		        					conn.close();
		                					}
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}
		
		
		public ArrayList getBenefitvalueBefore(Long lpricingSeqId) throws TTKException {
	    	
	    	Collection<Object> alResultList1 = new ArrayList<Object>();
	    	Collection<Object> alResultList2 = new ArrayList<Object>();
	    	Collection<Object> alFinalResultList = new ArrayList<Object>();
			Connection conn = null;
	    	CallableStatement cStmtObject=null;
	        ResultSet rs1 = null;
	        ResultSet rs2 = null;
	        ResultSet rs3 = null;
	        ResultSet rs4 = null;

	        Collection<Object> resultList = new ArrayList<Object>();
	        InsPricingVO insPricingVO = null;
	        InsPricingVO insPricingVOtotal = null;
	        try{
	        	conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strMasterListValue);
				//cStmtObject.setLong(1,lpricingSeqId);
				cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);// loading details
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);// IPOP sum details
				
				cStmtObject.execute();
				rs1 = (java.sql.ResultSet)cStmtObject.getObject(1);
				rs2 = (java.sql.ResultSet)cStmtObject.getObject(2);
				rs3 = (java.sql.ResultSet)cStmtObject.getObject(3);
				rs4 = (java.sql.ResultSet)cStmtObject.getObject(4);

		            if(rs1 != null){
		                while (rs1.next()) {
		                	insPricingVO = new InsPricingVO();
		                	insPricingVO.setBenf_typeseqid1(rs1.getLong("BENF_TYPE_SEQ_ID"));
		                	insPricingVO.setBenfdesc1(TTKCommon.checkNull(rs1.getString("BENF_DESC"))); 
		                	insPricingVO.setGndrtypeseqid1(rs1.getLong("GNDR_TYPE_SEQ_ID"));
		                	insPricingVO.setGndrdesc1(TTKCommon.checkNull(rs1.getString("GNDR_DESC")));
		                	insPricingVO.setAge_rngseqid1(rs1.getLong("AGE_RNG_SEQ_ID"));
		                	insPricingVO.setAge_range1(TTKCommon.checkNull(rs1.getString("AGE_RANGE")));
		                	insPricingVO.setOvrprtflio_dstr1(TTKCommon.checkNull(rs1.getString("OVR_PRTFLIO_DSTR")));
		                	alResultList1.add(insPricingVO);
				}
		            }
		            if(rs2 != null){
		                while (rs2.next()) {
		                	insPricingVO = new InsPricingVO();
		                	insPricingVO.setNatl_typeseqid1(rs2.getLong("NATL_TYPE_SEQ_ID"));
		                	insPricingVO.setNatl_name1(TTKCommon.checkNull(rs2.getString("NATL_NAME"))); 
		                	insPricingVO.setNatovrprtflio_dstr1(TTKCommon.checkNull(rs2.getString("OVR_PRTFLIO_DSTR"))); 

		                	alResultList2.add(insPricingVO);
		                	
				}
		            }
		            
		            if(rs4 != null){
		                while (rs4.next()) {
		                	insPricingVOtotal = new InsPricingVO();
		                	
		                	insPricingVOtotal.setSumTotalLives(TTKCommon.checkNull(rs4.getString("IOPT_TOT_LIVES"))); 
		                	insPricingVOtotal.setSumTotalLivesOptical(TTKCommon.checkNull(rs4.getString("OPL_TOT_LIVES"))); 
		                	insPricingVOtotal.setSumTotalLivesDental(TTKCommon.checkNull(rs4.getString("DNT_TOT_LIVES"))); 
		                	insPricingVOtotal.setSumTotalLivesMaternity(TTKCommon.checkNull(rs4.getString("MAT_TOT_LIVES"))); 
		                 	insPricingVOtotal.setSumNationalityLives(TTKCommon.checkNull(rs4.getString("NAT_TOT_LIVES"))); 
		                	//altotalLives.add(insPricingVOtotal);
		                }
		            }
		         
		            
		            alFinalResultList.add(alResultList1);
		            alFinalResultList.add(alResultList2);
		            alFinalResultList.add(insPricingVOtotal);
		            
				return (ArrayList)alFinalResultList;	
	    	}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (rs1 != null) rs1.close();
						if (rs2 != null) rs2.close();
						if (rs3 != null) rs3.close();
						if (rs4 != null) rs4.close();

					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null)	cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) {
		        					conn.close();
		                					}
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					
					rs4 = null;
					rs3 = null;
					rs2 = null;
					rs1 = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}
		
		 public int swSaveIncomeProfile(InsPricingVO insPricingVO) throws TTKException {
				int iResult = 0; 
				Connection conn = null;
		        CallableStatement cStmtObject=null; 
		       
		      
		        Long gndrtypeseqid= null;
		        Long benf_typeseqid= null;
		        String totalCoverdLives = "";
		        Long  benf_lives_seq_id = null;
		        Long  age_range_seq_id = null;
		        
		        
		        
		        try{
		        	conn = ResourceManager.getConnection(); 
		        	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSwSaveIncomeProfile);
		        	int j = 0;
		        	
		        	if(insPricingVO.getGroupProfileSeqID() != null) {
		                for(int i=0;i<insPricingVO.getGndrtypeseqid().length;i++){
		                	j++;
		                    if(!insPricingVO.getBenf_typeseqid()[i].equals(""))//to
		                    {
		                    	benf_typeseqid =insPricingVO.getBenf_typeseqid()[i];
			                   

		                    }
		                    else
		                    {
		                    	benf_typeseqid = null;
			                   

		                    }
		                  
		                    if(!insPricingVO.getBenf_lives_seq_id().equals(""))
		                    {
		                    	benf_lives_seq_id =insPricingVO.getBenf_lives_seq_id()[i]; 
		                    }
		                    else
		                    {
		                    	benf_lives_seq_id = null;
		                    }
		                
		                    
		                    if(!insPricingVO.getGndrtypeseqid().equals(""))
		                    {
		                    	gndrtypeseqid =insPricingVO.getGndrtypeseqid()[i]; 
		                    }
		                    else
		                    {
		                    	gndrtypeseqid = null;
		                    }
		                    
		                    if(!insPricingVO.getAge_rngseqid().equals(""))
		                    {
		                    	age_range_seq_id =insPricingVO.getAge_rngseqid()[i]; 
		                    }
		                    else
		                    {
		                    	age_range_seq_id = null;
		                    }
		                    
		                    
		                    if(!insPricingVO.getTotalCoverdLives().equals(""))
		                    {
		                    	totalCoverdLives =insPricingVO.getTotalCoverdLives()[i]; 
		                    }
		                    else
		                    {
		                    	totalCoverdLives = null;
		                    }
		                    

		    	        	
		    	            if(benf_lives_seq_id != null){
		    					cStmtObject.setLong(1,benf_lives_seq_id);
		    				}
		    				else{
		    					cStmtObject.setLong(1,0);
		    				}
		                    
		           
		    	            cStmtObject.setLong(2,insPricingVO.getGroupProfileSeqID()); 
		    	            
		    	            
		    	            if(benf_typeseqid != null){
		    					cStmtObject.setLong(3,benf_typeseqid);
		    				}
		    				else{
		    					cStmtObject.setLong(3,0);
		    				}
		    	            
		    	            
		    	            if(gndrtypeseqid != null){
		    					cStmtObject.setLong(4,gndrtypeseqid);
		    				}
		    				else{
		    					cStmtObject.setLong(4,0);
		    				}
		    	     
		    	            
		    	            if(age_range_seq_id != null){
		    					cStmtObject.setLong(5,age_range_seq_id);
		    				}
		    				else{
		    					cStmtObject.setLong(5,0);
		    				}
		    	     
		    	            if(totalCoverdLives != null){
		    					cStmtObject.setString(6,totalCoverdLives);
		    				}
		    				else{
		    					cStmtObject.setString(6,null);
		    				}
		    	     
		    	           
		    	            cStmtObject.setLong(7,insPricingVO.getAddedBy()); 
		    	          
		    	         
		    	           
		    	            cStmtObject.addBatch();
		        }}
		        	  
		        	iResult = 1;
		        	 cStmtObject.executeBatch(); 
		        	
	        }//end of try
		        catch (BatchUpdateException btsqlExp)
		        {
		            throw new TTKException(btsqlExp, "maintenance");
		        }//end of catch (SQLException sqlExp)
		        catch (SQLException sqlExp)
		        {
		            throw new TTKException(sqlExp, "maintenance");
		        }//end of catch (SQLException sqlExp)
		     
		        catch (Exception exp)
		        {
		            throw new TTKException(exp, "maintenance");
		        }//end of catch (Exception exp)
		        finally
				{
		        	/* Nested Try Catch to ensure resource closure */
		        	try // First try closing the Statement
		        	{
		        		try
		        		{
		        			if (cStmtObject != null) cStmtObject.close();
		        		}//end of try
		        		catch (SQLException sqlExp)
		        		{
		        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveGroupSpecificDesc()",sqlExp);
		        			throw new TTKException(sqlExp, "maintenance");
		        		}//end of catch (SQLException sqlExp)
		        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		        		{
		        			try
		        			{
		        				if(conn != null)  {
		        					conn.close();
		                					}
		        			}//end of try
		        			catch (SQLException sqlExp)
		        			{
		        				log.error("Error while closing the Statement in MaintenanceDAOImpl saveGroupSpecificDesc()",sqlExp);
		        				throw new TTKException(sqlExp, "maintenance");
		        			}//end of catch (SQLException sqlExp)
		        		}//end of finally Connection Close
		        	}//end of try
		        	catch (TTKException exp)
		        	{
		        		throw new TTKException(exp, "maintenance");
		        	}//end of catch (TTKException exp)
		        	finally // Control will reach here in anycase set null to the objects
		        	{
		        		cStmtObject = null;
		        		conn = null;
		        	}//end of finally
				}//end of finally
				return iResult;
		}//end of saveGroupSpecificDesc(CriticalICDDetailVO criticalICDDetailVO)
		
	
			
			 public int swSaveIncomeNatProfile(InsPricingVO insPricingVO) throws TTKException {
					int iResult = 0; 
					Connection conn = null;
			        CallableStatement cStmtObject=null; 
			    
			        Long nationality_seqId= null;
			        Long nationality_TypeseqId= null;
			        String nationCoverdLives = null;
			        
			   
			        
			        try{
			        	conn = ResourceManager.getConnection(); 
			        	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSwSaveIncomeNatProfile);
			        	int j = 0;
			        	
			        	if(insPricingVO.getGroupProfileSeqID() != null) {
			               for(int i=0;i<insPricingVO.getNatl_typeseqid().length;i++){
			                	j++;
			                    if(!insPricingVO.getNatl_typeseqid()[i].equals(""))//to
			                    {
				                	nationality_TypeseqId =insPricingVO.getNatl_typeseqid()[i]; 

			                    }
			                    else
			                    {
			                    	nationality_TypeseqId = null;

			                    }
			                    
			                    if(!insPricingVO.getNatl_seqid().equals(""))
			                    {
			                    	nationality_seqId =insPricingVO.getNatl_seqid()[i]; 
			                    }
			                    else
			                    {
			                    	nationality_seqId = null;
			                    }
			                    
			                    if(!insPricingVO.getNatCoverdLives().equals(""))
			                    {
			                    	nationCoverdLives =insPricingVO.getNatCoverdLives()[i]; 
			                    }
			                    else
			                    {
			                    	nationCoverdLives = null;
			                    }
			                    
			    	        	
			    	            if(nationality_seqId != null){
			    					cStmtObject.setLong(1,nationality_seqId);
			    				}
			    				else{
			    					cStmtObject.setLong(1,0);
			    				}
			                    
			           
			    	            cStmtObject.setLong(2,insPricingVO.getGroupProfileSeqID()); 
			    	            
			    	            
			    	            if(nationality_TypeseqId != null){
			    					cStmtObject.setLong(3,nationality_TypeseqId);
			    				}
			    				else{
			    					cStmtObject.setLong(3,0);
			    				}
			    	            
			    	     
			    	            if(nationCoverdLives != null){
			    					cStmtObject.setString(4,nationCoverdLives);
			    				}
			    				else{
			    					cStmtObject.setLong(4,0);
			    				}
			    	     
			    	           
			    	            cStmtObject.setLong(5,insPricingVO.getAddedBy()); 
			    	          
			    	           
			    	            cStmtObject.addBatch();
			        }
			        	}
			        	 cStmtObject.executeBatch(); 
			        	  
			        	iResult = 1;
			        	
			        	
		        }//end of try
			        catch (SQLException sqlExp)
			        {
			            throw new TTKException(sqlExp, "maintenance");
			        }//end of catch (SQLException sqlExp)
			        catch (Exception exp)
			        {
			            throw new TTKException(exp, "maintenance");
			        }//end of catch (Exception exp)
			        finally
					{
			        	/* Nested Try Catch to ensure resource closure */
			        	try // First try closing the Statement
			        	{
			        		try
			        		{
			        			if (cStmtObject != null) cStmtObject.close();
			        		}//end of try
			        		catch (SQLException sqlExp)
			        		{
			        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveGroupSpecificDesc()",sqlExp);
			        			throw new TTKException(sqlExp, "maintenance");
			        		}//end of catch (SQLException sqlExp)
			        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			        		{
			        			try
			        			{
			        				if(conn != null)  {
			        					conn.close();
			                					}
			        			}//end of try
			        			catch (SQLException sqlExp)
			        			{
			        				log.error("Error while closing the Statement in MaintenanceDAOImpl saveGroupSpecificDesc()",sqlExp);
			        				throw new TTKException(sqlExp, "maintenance");
			        			}//end of catch (SQLException sqlExp)
			        		}//end of finally Connection Close
			        	}//end of try
			        	catch (TTKException exp)
			        	{
			        		throw new TTKException(exp, "maintenance");
			        	}//end of catch (TTKException exp)
			        	finally // Control will reach here in anycase set null to the objects
			        	{
			        		cStmtObject = null;
			        		conn = null;
			        	}//end of finally
					}//end of finally
					return iResult;
			}//end of saveGroupSpecificDesc(CriticalICDDetailVO criticalICDDetailVO)
		 
		 public ArrayList getBenefitvalueAfter(Long lpricingSeqId) throws TTKException {
		    	
		    	Collection<Object> alResultList = new ArrayList<Object>();

		    	Collection<Object> alProfileGroupList = new ArrayList<Object>();
		    	Collection<Object> alnationalityList = new ArrayList<Object>();
		    	Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs = null;
		        ResultSet rs2 = null;
		        ResultSet rs3 = null;
		        ResultSet rs4 = null;
		        ResultSet rs5 = null;
		        InsPricingVO insPricingVO = null;
		        InsPricingVO insPricingVOtotal = null;
		        try{
		        	conn = ResourceManager.getConnection();
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strswProfileIncomeListValue);
					cStmtObject.setLong(1,lpricingSeqId);
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs = (java.sql.ResultSet)cStmtObject.getObject(2);
					rs2 = (java.sql.ResultSet)cStmtObject.getObject(3);
					rs3 = (java.sql.ResultSet)cStmtObject.getObject(4);
					rs4 = (java.sql.ResultSet)cStmtObject.getObject(5);
					rs5=(java.sql.ResultSet)cStmtObject.getObject(6);
			            if(rs != null){
			                while (rs.next()) {
			                	insPricingVO = new InsPricingVO();
			                    
			                	insPricingVO.setBenf_lives_seq_id1(rs.getLong("BENF_LIVES_SEQ_ID"));
			                	insPricingVO.setGroupProfileSeqID(rs.getLong("GRP_PROF_SEQ_ID")); 
			                	insPricingVO.setBenfdesc1(TTKCommon.checkNull(rs.getString("BENF_DESC"))); 
			                	insPricingVO.setBenf_typeseqid1(rs.getLong("BENF_TYPE_SEQ_ID"));
			                	insPricingVO.setGndrtypeseqid1(rs.getLong("GNDR_TYPE_SEQ_ID"));
			                  	insPricingVO.setGndrdesc1(TTKCommon.checkNull(rs.getString("GNDR_DESC")));
			                	insPricingVO.setAge_rngseqid1(rs.getLong("AGE_RNG_SEQ_ID"));
			                	insPricingVO.setAge_range1(TTKCommon.checkNull(rs.getString("AGE_RANGE")));
			                	insPricingVO.setTotalCoverdLives1(TTKCommon.checkNull(rs.getString("TOT_COV_LIVES")));
			                	insPricingVO.setOvrprtflio_dstr1(TTKCommon.checkNull(rs.getString("OVR_PRTFLIO_DSTR")));
			                	
			                	
			                	alProfileGroupList.add(insPricingVO);
		    							}
			            }
			            
			            if(rs2 != null){
			                while (rs2.next()) {
			                	insPricingVO = new InsPricingVO();
			                
			                	insPricingVO.setNatl_seqid1(rs2.getLong("GRP_NAT_SEQ_ID"));
			                	insPricingVO.setNatl_typeseqid1(rs2.getLong("NATL_TYPE_SEQ_ID"));
			                	insPricingVO.setNatl_name1(TTKCommon.checkNull(rs2.getString("NATL_NAME"))); 
			                	insPricingVO.setNatCoverdLives1(TTKCommon.checkNull(rs2.getString("TOT_LIVES_COV"))); 
			                	insPricingVO.setNatovrprtflio_dstr1(TTKCommon.checkNull(rs2.getString("OVR_PRTFLIO_DSTR"))); 
			                	alnationalityList.add(insPricingVO);
			                
			                	
			                }
			            }
			          if(rs3 != null){
			                while (rs3.next()) {
			                	insPricingVOtotal = new InsPricingVO();
			                	
			                	insPricingVOtotal.setSumTotalLives(TTKCommon.checkNull(rs3.getString("IOPT_TOT_LIVES"))); 
			                	insPricingVOtotal.setSumTotalLivesOptical(TTKCommon.checkNull(rs3.getString("OPL_TOT_LIVES"))); 
			                	insPricingVOtotal.setSumTotalLivesDental(TTKCommon.checkNull(rs3.getString("DNT_TOT_LIVES"))); 
			                	insPricingVOtotal.setSumTotalLivesMaternity(TTKCommon.checkNull(rs3.getString("MAT_TOT_LIVES"))); 
			                	//altotalLives.add(insPricingVOtotal);
			                }
			            }
			            if(rs4 != null){
			                while (rs4.next()) {
			                	//insPricingVOtotal = new InsPricingVO();
			                	insPricingVOtotal.setSumNationalityLives(TTKCommon.checkNull(rs4.getString(1))); 
			                	//almaternityTotalLives.add(insPricingVO);
			                }
			            }
			            if(rs5!=null){
			            	while (rs5.next()) {
			                	insPricingVO.setTotalCovedLives(TTKCommon.checkNull(rs5.getString("TOTALNOOFLIVES")));
			                	insPricingVO.setTotalLivesMaternity(TTKCommon.checkNull(rs5.getString("TOTALMATERNITYLIVES")));
			                }
			            }
			            
			            alResultList.add(alProfileGroupList);
			            alResultList.add(alnationalityList);
			            alResultList.add(insPricingVOtotal);
			            alResultList.add(insPricingVO);
			            
					return (ArrayList)alResultList;	
		    	}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
							if (rs != null) rs.close();
							if (rs2 != null) rs2.close();
							if (rs3 != null) rs3.close();
							if (rs4 != null) rs4.close();
							
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						
						rs4 = null;
						rs3 = null;
						rs2 = null;
						rs = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}
		   
		 
		 
		 public ArrayList getcpmBeforeCalcultion(InsPricingVO insPricingVO)  throws TTKException {
		    	
			    Collection<Object> alResultList = new ArrayList<Object>();
		    	
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs1 = null;
		        ResultSet rs2 = null;
		      
		        SwPolicyConfigVO swPolicyConfigVO = null;
		        try{
		        	conn = ResourceManager.getConnection();
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strCPMBeforeCal);
					cStmtObject.setLong(1,insPricingVO.getGroupProfileSeqID());
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs1 = (java.sql.ResultSet)cStmtObject.getObject(2);
				
				
			            if(rs1 != null){
			                while (rs1.next()) {
			                	swPolicyConfigVO = new SwPolicyConfigVO();
			                	
			                	swPolicyConfigVO.setLngGroupProfileSeqID(insPricingVO.getGroupProfileSeqID());
			                	if(rs1.getString("CPM_SEQ_ID") != null){
			                	swPolicyConfigVO.setCpmSeqID(rs1.getLong("CPM_SEQ_ID"));
			                	}else{
			                		swPolicyConfigVO.setCpmSeqID(null);
			                	}
			                	
			                	swPolicyConfigVO.setAddedBy(insPricingVO.getAddedBy());
			                	
			                	swPolicyConfigVO.setSlNo(TTKCommon.checkNull(rs1.getString("SL_NO")));
			                	swPolicyConfigVO.setDataType(TTKCommon.checkNull(rs1.getString("DATA_TYPE")));
			                	swPolicyConfigVO.setPolicyNo(TTKCommon.checkNull(rs1.getString("POLICY_NO")));
			                	swPolicyConfigVO.setClientCode(TTKCommon.checkNull(rs1.getString("CLIENT_CODE")));
			                	swPolicyConfigVO.setEffectiveYear(TTKCommon.checkNull(rs1.getString("EFFECTIVE_YEAR"))); 
			                	if(rs1.getDate("POL_EFFECTIVE_DATE") != null){
/*				                swPolicyConfigVO.setPolicyEffDate(new Date(TTKCommon.getFormattedDate(rs1.getDate("POL_EFFECTIVE_DATE"))));
*/			                	
				                swPolicyConfigVO.setPolicyEffDate(new Date(rs1.getTimestamp("POL_EFFECTIVE_DATE").getTime()));
				                swPolicyConfigVO.setStrpolicyEffDate(TTKCommon.getFormattedDate(rs1.getDate("POL_EFFECTIVE_DATE")));
			              

			                	}
			                	if(rs1.getDate("POL_EXPIRE_DATE") != null){
			                	swPolicyConfigVO.setPolicyExpDate(new Date(rs1.getTimestamp("POL_EXPIRE_DATE").getTime()));
				                swPolicyConfigVO.setStrpolicyExpDate(TTKCommon.getFormattedDate(rs1.getDate("POL_EXPIRE_DATE")));

			                	}
			                	swPolicyConfigVO.setPolicyDurationPerMonth(rs1.getLong("POL_DURATION_MONTH"));
			                	swPolicyConfigVO.setNoOfLives(rs1.getLong("NO_LIVES"));
			                	if(rs1.getString("IP_CPM") != null){
			                		swPolicyConfigVO.setInPatientCPM(TTKCommon.checkNull(rs1.getString("IP_CPM"))); 
			    				}
			                	if(rs1.getString("OP_CPM") != null){
			                		swPolicyConfigVO.setOutPatientCPM(TTKCommon.checkNull(rs1.getString("OP_CPM"))); 
			    				}
			                	
			                	if(rs1.getString("OPL_CPM") != null){
			                		swPolicyConfigVO.setOpticalCPM(TTKCommon.checkNull(rs1.getString("OPL_CPM"))); 
			    				}
			                	if(rs1.getString("DNT_CPM") != null){
			                		swPolicyConfigVO.setDentalCPM(TTKCommon.checkNull(rs1.getString("DNT_CPM"))); 
			    				}
			                	if(rs1.getString("ALL_EXCL_MAT") != null){
			                		swPolicyConfigVO.setAllExlMaternity(TTKCommon.checkNull(rs1.getString("ALL_EXCL_MAT"))); 
			    				}
			                	if(rs1.getString("MAT_CPM") != null){
				                	swPolicyConfigVO.setMaternityCPM(TTKCommon.checkNull(rs1.getString("MAT_CPM"))); 
			    				}
			              
			                	
			                	if(rs1.getString("ALRT_MSG") != null){
			                		swPolicyConfigVO.setAlertMsg(TTKCommon.checkNull(rs1.getString("ALRT_MSG"))); 
			                		
			    				}
			                	
			                	alResultList.add(swPolicyConfigVO);
			                	
		    							}
			            }
			          
			           
			     
					return (ArrayList)alResultList;	
		    	}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
						
							if (rs2 != null) rs2.close();
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs2 = null;
						rs1 = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}

	
		
		public ArrayList calculatePlanDesignConfig(SwPolicyConfigVO swpolicyConfigVO) throws TTKException
		{
			int iResult=0;
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ArrayList arraylist = new ArrayList();
			ArrayList alPastdatalist = new ArrayList();
			try
			{
			
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveCalulatePricingList);
				alPastdatalist = 	swpolicyConfigVO.getAlPastData();
				int i= 0;
				    if(!(alPastdatalist.isEmpty())){
		                for(Object object:alPastdatalist){
		                i++;
		            swpolicyConfigVO = (SwPolicyConfigVO)object;
		           
		            if(swpolicyConfigVO.getCpmSeqID()!= null)
		            {
		            cStmtObject.setLong(1,swpolicyConfigVO.getCpmSeqID());
		         
		            }else{
		            cStmtObject.setLong(1,0);
		            }
		            
		            if(swpolicyConfigVO.getLngGroupProfileSeqID()!= null)
		            {
		            cStmtObject.setLong(2,swpolicyConfigVO.getLngGroupProfileSeqID());
		         
		            }else{
		            cStmtObject.setLong(2,0);
		            }
		  
		            cStmtObject.setString(3,swpolicyConfigVO.getDataType()); 
		            cStmtObject.setString(4,swpolicyConfigVO.getPolicyNo());

		            if(swpolicyConfigVO.getEffectiveYear() != null){
		            cStmtObject.setString(5,swpolicyConfigVO.getEffectiveYear());
		            }else{
		            	cStmtObject.setLong(5,0);
		            }
		           
		            if(!swpolicyConfigVO.getDataType().equalsIgnoreCase("USR_IP_YR")){
			            if(swpolicyConfigVO.getPolicyEffDate() != null){
						cStmtObject.setTimestamp(6,new Timestamp(swpolicyConfigVO.getPolicyEffDate().getTime()));//LETTER_DATE
			            }else{
			            	cStmtObject.setTimestamp(6,null);
			            }
			    
			            if(swpolicyConfigVO.getPolicyExpDate() != null){
						cStmtObject.setTimestamp(7,new Timestamp(swpolicyConfigVO.getPolicyExpDate().getTime()));//LETTER_DATE
				        }else{
				        cStmtObject.setTimestamp(7,null);
				        }
			            
		            }else if (swpolicyConfigVO.getDataType().equalsIgnoreCase("USR_IP_YR")){
		           
		            	if(swpolicyConfigVO.getStrpolicyEffDate() != "" && swpolicyConfigVO.getStrpolicyEffDate() != null){
						cStmtObject.setTimestamp(6,TTKCommon.getConvertStringToDateTimestamp(swpolicyConfigVO.getStrpolicyEffDate()));//LETTER_DATE
						 
			            }else{
			            	cStmtObject.setTimestamp(6,null);
			            }
						
			            if(swpolicyConfigVO.getStrpolicyExpDate() != "" && swpolicyConfigVO.getStrpolicyExpDate() != null){
						cStmtObject.setTimestamp(7,TTKCommon.getConvertStringToDateTimestamp(swpolicyConfigVO.getStrpolicyExpDate()));//LETTER_DATE
				        }else{
				        cStmtObject.setTimestamp(7,null);
				        }
		            }
		            
		            if(swpolicyConfigVO.getPolicyDurationPerMonth() != null){
			            cStmtObject.setLong(8,swpolicyConfigVO.getPolicyDurationPerMonth());
			            }else{
			            	cStmtObject.setLong(8,0);
			            }
		            
		            if(swpolicyConfigVO.getNoOfLives() != null){
			            cStmtObject.setLong(9,swpolicyConfigVO.getNoOfLives());
			            }else{
			            	cStmtObject.setLong(9,0);
			            }
		          
		            if(swpolicyConfigVO.getInPatientCPM() != null  && swpolicyConfigVO.getInPatientCPM() !=""){
			            cStmtObject.setBigDecimal(10, new BigDecimal(swpolicyConfigVO.getInPatientCPM()));
			            }else{
			            	cStmtObject.setBigDecimal(10,null);
			            }
			
		            if(swpolicyConfigVO.getOutPatientCPM() != null && swpolicyConfigVO.getOutPatientCPM() !=""){
			            cStmtObject.setBigDecimal(11,new BigDecimal(swpolicyConfigVO.getOutPatientCPM()));
			            }else{
			            	cStmtObject.setBigDecimal(11,null);
			            }
		            
		            if(swpolicyConfigVO.getMaternityCPM() != null && swpolicyConfigVO.getMaternityCPM() !=""){
			            cStmtObject.setBigDecimal(12,new BigDecimal(swpolicyConfigVO.getMaternityCPM()));
			            }else{
			            	cStmtObject.setBigDecimal(12,null);
			            }
		            
		            
		            if(swpolicyConfigVO.getOpticalCPM() != null && swpolicyConfigVO.getOpticalCPM() !=""){
			            cStmtObject.setBigDecimal(13,new BigDecimal(swpolicyConfigVO.getOpticalCPM()));
			            }else{
			            	cStmtObject.setBigDecimal(13,null);
			            }
		            
		            
		            if(swpolicyConfigVO.getDentalCPM() != null && swpolicyConfigVO.getDentalCPM() !=""){
			            cStmtObject.setBigDecimal(14,new BigDecimal(swpolicyConfigVO.getDentalCPM()));
			            }else{
			            	cStmtObject.setBigDecimal(14,null);
			            }
		            
		           /* if(swpolicyConfigVO.getFinalweightage() != null &&  swpolicyConfigVO.getFinalweightage()!= ""){
			          //  cStmtObject.setBigDecimal(15,swpolicyConfigVO.getFinalweightage());
		            	cStmtObject.setBigDecimal(15,new BigDecimal(swpolicyConfigVO.getFinalweightage()));
			            }else{
			            	cStmtObject.setBigDecimal(15,null);
			            }*/
		            
		            if(swpolicyConfigVO.getInpatientcrediblty() != null &&  swpolicyConfigVO.getInpatientcrediblty()!= ""){
			            	cStmtObject.setBigDecimal(15,new BigDecimal(swpolicyConfigVO.getInpatientcrediblty()));
				            }else{
				            	cStmtObject.setBigDecimal(15,null);
				            }
		            if(swpolicyConfigVO.getOutpatientcrediblty() != null &&  swpolicyConfigVO.getOutpatientcrediblty()!= ""){
			            	cStmtObject.setBigDecimal(16,new BigDecimal(swpolicyConfigVO.getOutpatientcrediblty()));
				            }else{
				            	cStmtObject.setBigDecimal(16,null);
				            }
		            if(swpolicyConfigVO.getMaternitycrediblty() != null &&  swpolicyConfigVO.getMaternitycrediblty()!= ""){
			            	cStmtObject.setBigDecimal(17,new BigDecimal(swpolicyConfigVO.getMaternitycrediblty()));
				            }else{
				            	cStmtObject.setBigDecimal(17,null);
				            }
		            if(swpolicyConfigVO.getOpticalcrediblty() != null &&  swpolicyConfigVO.getOpticalcrediblty()!= ""){
			            	cStmtObject.setBigDecimal(18,new BigDecimal(swpolicyConfigVO.getOpticalcrediblty()));
				            }else{
				            	cStmtObject.setBigDecimal(18,null);
				            }
		            if(swpolicyConfigVO.getDentalcrediblty() != null &&  swpolicyConfigVO.getDentalcrediblty()!= ""){
			            	cStmtObject.setBigDecimal(19,new BigDecimal(swpolicyConfigVO.getDentalcrediblty()));
				            }else{
				            	cStmtObject.setBigDecimal(19,null);
				            }
		            
		            cStmtObject.setLong(20,swpolicyConfigVO.getAddedBy());
		            cStmtObject.setString(21,swpolicyConfigVO.getSlNo());
		            cStmtObject.addBatch();
				}
		                cStmtObject.executeBatch(); 
				}//end of try
				     iResult=i;
				     arraylist.add(iResult);
				     
			}
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
	        	
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        			throw new TTKException(sqlExp, "insurance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null)
	        					{
	        					conn.close();
	        					}
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        				throw new TTKException(sqlExp, "insurance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "insurance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			return arraylist;
		}//end of deleteInsuranceCompany(String strInsSeqID)
		
		 public ArrayList getdemographicData(InsPricingVO insPricingVO,String demographicDataFlag)  throws TTKException {
		    	
			    Collection<Object> alResultList = new ArrayList<Object>();
		    	
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs1 = null;
		        ResultSet rs2 = null;
		        SwPolicyConfigVO swPolicyConfigVO = null;
		        try{
		        	conn = ResourceManager.getConnection();
		        	
		        	if(demographicDataFlag.equalsIgnoreCase("Y"))
		        	{
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strDemographicOnsaveData);
		        	}else{
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strDemographicOnMasterData);
					}
					cStmtObject.setLong(1,insPricingVO.getGroupProfileSeqID());
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs1 = (java.sql.ResultSet)cStmtObject.getObject(2);
				
				
			            if(rs1 != null){
			                while (rs1.next()) {
			                	swPolicyConfigVO = new SwPolicyConfigVO();
			                	swPolicyConfigVO.setLngGroupProfileSeqID(insPricingVO.getGroupProfileSeqID());
			                	swPolicyConfigVO.setAddedBy(insPricingVO.getAddedBy());
			                	swPolicyConfigVO.setDemoSlNo(TTKCommon.checkNull(rs1.getString("SL_NO")));
			                	swPolicyConfigVO.setDemographicSeqId(rs1.getLong("DMGRPHCS_SEQ_ID"));
			                	swPolicyConfigVO.setDemodataType(TTKCommon.checkNull(rs1.getString("DATA_TYPE")));
			                	
			                	swPolicyConfigVO.setClientCode(TTKCommon.checkNull(rs1.getString("CLIENT_CODE")));
			                  	swPolicyConfigVO.setDemoPolicyNo(TTKCommon.checkNull(rs1.getString("POLICY_NO")));	                
			                	swPolicyConfigVO.setDemoEffectiveYear(TTKCommon.checkNull(rs1.getString("EFFECTIVE_YEAR"))); 
			                	if(rs1.getString("POL_EFFECTIVE_DATE") != null){
						        swPolicyConfigVO.setDemoPolicyEffDate(TTKCommon.checkNull(rs1.getString("POL_EFFECTIVE_DATE")));
					             }
					             if(rs1.getString("POL_EXPIRE_DATE") != null){
						         swPolicyConfigVO.setDemoPolicyExpDate(TTKCommon.checkNull(rs1.getString("POL_EXPIRE_DATE")));
					             }
					            
					          
					             if(rs1.getString("POL_DURATION_MONTH") != null){
				                		swPolicyConfigVO.setDemoPolicyDurationPerMonth(TTKCommon.checkNull(rs1.getString("POL_DURATION_MONTH"))); 
				    				}
					             if(rs1.getString("NO_LIVES") != null){
				                		swPolicyConfigVO.setDemoNoOfLives(TTKCommon.checkNull(rs1.getString("NO_LIVES"))); 
				    				}
				             
			                	if(rs1.getString("ALRT_MSG") != null){
			                		swPolicyConfigVO.setDemoAlertMsg(TTKCommon.checkNull(rs1.getString("ALRT_MSG"))); 
			    				}
			                	
			                	if(rs1.getString("AVG_AGE") != null){
			                		swPolicyConfigVO.setDemoAverageAge(TTKCommon.checkNull(rs1.getString("AVG_AGE"))); 
			    				}
			                	if(rs1.getString("NATIONALITY") != null){
			                		swPolicyConfigVO.setDemoNationality(TTKCommon.checkNull(rs1.getString("NATIONALITY"))); 
			    				}
			                	
			                	if(rs1.getString("AREA_COV") != null){
			                		swPolicyConfigVO.setDemoAreaOfCover(TTKCommon.checkNull(rs1.getString("AREA_COV"))); 
			    				}
			                	if(rs1.getString("NWK_TYPE") != null){
			                		swPolicyConfigVO.setDemoNetwork(TTKCommon.checkNull(rs1.getString("NWK_TYPE"))); 
			    				}
			                	
			                	if(rs1.getString("MBL") != null){
			                		swPolicyConfigVO.setDemoMaximumBenfitLimit(TTKCommon.checkNull(rs1.getString("MBL"))); 
			    				}
			                	if(rs1.getString("OP_COPAY") != null){
				                	swPolicyConfigVO.setDemoOpCopay(TTKCommon.checkNull(rs1.getString("OP_COPAY"))); 
			    				}
			                	if(rs1.getString("IP_COPAY") != null){
				                	swPolicyConfigVO.setDemoOPDeductable(TTKCommon.checkNull(rs1.getString("IP_COPAY"))); 
			    				}
			              
			                	if(rs1.getString("MAT_COV") != null){
			                		swPolicyConfigVO.setDemoMaternityCoverage(TTKCommon.checkNull(rs1.getString("MAT_COV"))); 
			    				}
			                	if(rs1.getString("MAT_LIMIT") != null){
			                		swPolicyConfigVO.setDemoMaternityLimit(TTKCommon.checkNull(rs1.getString("MAT_LIMIT"))); 
			    				}
			                	if(rs1.getString("OPTL_COV") != null){
			                		swPolicyConfigVO.setDemoOpticalCoverage(TTKCommon.checkNull(rs1.getString("OPTL_COV"))); 
			    				}
			                	if(rs1.getString("OPTL_LIMIT") != null){
			                		swPolicyConfigVO.setDemoOpticalLimit(TTKCommon.checkNull(rs1.getString("OPTL_LIMIT"))); 
			    				}
			                	if(rs1.getString("OPTL_COPAY") != null){
			                		swPolicyConfigVO.setDemoOpticalCopay(TTKCommon.checkNull(rs1.getString("OPTL_COPAY"))); 
			    				}
			                	if(rs1.getString("DNT_COV") != null){
			                		swPolicyConfigVO.setDemoDentalCoverage(TTKCommon.checkNull(rs1.getString("DNT_COV"))); 
			    				}
			                	if(rs1.getString("DNT_LIMIT") != null){
			                		swPolicyConfigVO.setDemoDentalLimit(TTKCommon.checkNull(rs1.getString("DNT_LIMIT"))); 
			    				}
			                	if(rs1.getString("DNT_COPAY") != null){
			                		swPolicyConfigVO.setDemoDentalCopay(TTKCommon.checkNull(rs1.getString("DNT_COPAY"))); 
			    				}
			                   
			                	if(rs1.getString("ALHALLI_COV") != null){
			                		swPolicyConfigVO.setDemoAlahli(TTKCommon.checkNull(rs1.getString("ALHALLI_COV"))); 
			    				}
//			                	if(rs1.getString("MAT_COPAY") != null){
			                		swPolicyConfigVO.setMaternityCopay(TTKCommon.checkNull(rs1.getString("MAT_COPAY"))); 
//			                	}
			                   
			                	alResultList.add(swPolicyConfigVO);
		    				}
			            }
			          
			           
			     
					return (ArrayList)alResultList;	
		    	}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
						
							if (rs2 != null) rs2.close();
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs2 = null;
						rs1 = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}

		 public ArrayList getcpmAfterCalcultion(InsPricingVO insPricingVO)  throws TTKException {
		    	
			    Collection<Object> alResultList = new ArrayList<Object>();
		    	
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs1 = null;
		        ResultSet rs2 = null;
		        SwPolicyConfigVO swPolicyConfigVO = null;
		        try{
		        	conn = ResourceManager.getConnection();
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strCPMAfterCal);
					cStmtObject.setLong(1,insPricingVO.getGroupProfileSeqID());
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs1 = (java.sql.ResultSet)cStmtObject.getObject(2);
				
				
			            if(rs1 != null){
			                while (rs1.next()) {
			                	swPolicyConfigVO = new SwPolicyConfigVO();
			                	
			                	
			                	swPolicyConfigVO.setLngGroupProfileSeqID(rs1.getLong("GRP_PROF_SEQ_ID"));
			                	swPolicyConfigVO.setCpmSeqID(rs1.getLong("CPM_SEQ_ID"));
			                	swPolicyConfigVO.setAddedBy(insPricingVO.getAddedBy());
			                	swPolicyConfigVO.setSlNo(TTKCommon.checkNull(rs1.getString("SL_NO")));
			                	
			                	swPolicyConfigVO.setDataType(TTKCommon.checkNull(rs1.getString("DATA_TYPE")));
			                	swPolicyConfigVO.setPolicyNo(TTKCommon.checkNull(rs1.getString("POLICY_NO")));
			                	swPolicyConfigVO.setClientCode(TTKCommon.checkNull(rs1.getString("CLIENT_CODE")));
			                	swPolicyConfigVO.setEffectiveYear(TTKCommon.checkNull(rs1.getString("EFFECTIVE_YEAR"))); 
	                		  
			                	if(rs1.getDate("POL_EFFECTIVE_DATE") != null){
/*				                swPolicyConfigVO.setPolicyEffDate(new Date(TTKCommon.getFormattedDate(rs1.getDate("POL_EFFECTIVE_DATE"))));
*/			                
			                	swPolicyConfigVO.setPolicyEffDate(new Date(rs1.getTimestamp("POL_EFFECTIVE_DATE").getTime()));
				                swPolicyConfigVO.setStrpolicyEffDate(TTKCommon.getFormattedDate(rs1.getDate("POL_EFFECTIVE_DATE")));

			                	}
			                	if(rs1.getDate("POL_EXPIRE_DATE") != null){
			                	swPolicyConfigVO.setPolicyExpDate(new Date(rs1.getTimestamp("POL_EXPIRE_DATE").getTime()));
				                swPolicyConfigVO.setStrpolicyExpDate(TTKCommon.getFormattedDate(rs1.getDate("POL_EXPIRE_DATE")));
			                	}
			                	
			                	swPolicyConfigVO.setPolicyDurationPerMonth(rs1.getLong("POL_DURATION_MONTH"));
			                	swPolicyConfigVO.setNoOfLives(rs1.getLong("NO_LIVES"));
			                	if(rs1.getString("IP_CPM") != null){
			                		swPolicyConfigVO.setInPatientCPM(TTKCommon.checkNull(rs1.getString("IP_CPM"))); 
			    				}
			                	if(rs1.getString("OP_CPM") != null){
			                		swPolicyConfigVO.setOutPatientCPM(TTKCommon.checkNull(rs1.getString("OP_CPM"))); 
			    				}
			                	
			                	if(rs1.getString("OPL_CPM") != null){
			                		swPolicyConfigVO.setOpticalCPM(TTKCommon.checkNull(rs1.getString("OPL_CPM"))); 
			    				}
			                	if(rs1.getString("DNT_CPM") != null){
			                		swPolicyConfigVO.setDentalCPM(TTKCommon.checkNull(rs1.getString("DNT_CPM"))); 
			    				}
			                	if(rs1.getString("EXCPT_MAT_CPM") != null){
				                	swPolicyConfigVO.setAllExlMaternity(TTKCommon.checkNull(rs1.getString("EXCPT_MAT_CPM"))); 
			    				}
			                	if(rs1.getString("MAT_CPM") != null){
				                	swPolicyConfigVO.setMaternityCPM(TTKCommon.checkNull(rs1.getString("MAT_CPM"))); 
			    				}
			              
			                	/*if(rs1.getString("WEIGHTAGE") != null){
				                	//swPolicyConfigVO.setFinalweightage(new BigDecimal(rs1.getString("WEIGHTAGE"))); 
			                		swPolicyConfigVO.setFinalweightage(TTKCommon.checkNull(rs1.getString("WEIGHTAGE"))); 
			    				}*/
			            
			                	
			                	if(rs1.getString("IP_WEIGHTAGE") != null){
			                		swPolicyConfigVO.setInpatientcrediblty(TTKCommon.checkNull(rs1.getString("IP_WEIGHTAGE"))); 
			    				}
			                	if(rs1.getString("OP_WEIGHTAGE") != null){
			                		swPolicyConfigVO.setOutpatientcrediblty(TTKCommon.checkNull(rs1.getString("OP_WEIGHTAGE"))); 
			    				}
			                	if(rs1.getString("MAT_WEIGHTAGE") != null){
			                		swPolicyConfigVO.setMaternitycrediblty(TTKCommon.checkNull(rs1.getString("MAT_WEIGHTAGE"))); 
			    				}
			                	if(rs1.getString("OPTL_WEIGHTAGE") != null){
			                		swPolicyConfigVO.setOpticalcrediblty(TTKCommon.checkNull(rs1.getString("OPTL_WEIGHTAGE"))); 
			    				}
			                	if(rs1.getString("DNT_WEIGHTAGE") != null){
			                		swPolicyConfigVO.setDentalcrediblty(TTKCommon.checkNull(rs1.getString("DNT_WEIGHTAGE"))); 
			    				}
			                   
			                	alResultList.add(swPolicyConfigVO);
		    				}
			            }
			          
			           
			     
					return (ArrayList)alResultList;	
		    	}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
						
							if (rs2 != null) rs2.close();
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs2 = null;
						rs1 = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}
		 
		 public ArrayList getSwInsuranceProfileList(ArrayList alSearchObjects) throws TTKException {
				Connection conn = null;
				CallableStatement cStmtObject=null;
				ResultSet rs = null;
				Collection<Object> alResultList = new ArrayList<Object>();
				  InsPricingVO insPricingVO = null;

				try 
				{ 

						conn = ResourceManager.getConnection();				
						cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSwInsuranceProfileList);
					
						
						    cStmtObject.setString(1,(String) alSearchObjects.get(0));
						    cStmtObject.setString(2,(String) alSearchObjects.get(1));
						    cStmtObject.setString(3,(String) alSearchObjects.get(2));
						    cStmtObject.setString(4,(String)alSearchObjects.get(3));
						    cStmtObject.setString(5,(String)alSearchObjects.get(4));// group name
							cStmtObject.setString(6,(String)alSearchObjects.get(5));
							cStmtObject.setString(7,(String)alSearchObjects.get(6));
							cStmtObject.setString(8,(String)alSearchObjects.get(7));

						cStmtObject.registerOutParameter(9,OracleTypes.CURSOR);
						cStmtObject.execute();
						rs = (java.sql.ResultSet)cStmtObject.getObject(9);
						if(rs != null)
						{
							while (rs.next()) {
								insPricingVO= new InsPricingVO();
								insPricingVO.setClientCode(TTKCommon.checkNull(rs.getString("client_code")));
								insPricingVO.setPricingRefno(TTKCommon.checkNull(rs.getString("ref_no")));
								insPricingVO.setPreviousPolicyNo(TTKCommon.checkNull(rs.getString("prev_policy_no")));
								if(rs.getString("GRP_PROF_SEQ_ID")!=null)
								{
									insPricingVO.setGroupProfileSeqID(new Long(rs.getLong("GRP_PROF_SEQ_ID")));
								}
								alResultList.add(insPricingVO);
								}//end of if(strIdentifier.equalsIgnoreCase("Administration"))
							
							}// End of   while (rs.next())
				
					
					return (ArrayList)alResultList;
				}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
							if (rs != null) rs.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}

	
		
		 public InsPricingVO getfalgPricingvalue(long lpricingSeqId) throws TTKException{
		    	Connection conn = null;
		        PreparedStatement pStmt = null;
		        ResultSet rs = null;
				InsPricingVO  insPricingVO =new InsPricingVO();

		        try{
		            conn = ResourceManager.getConnection();
		            pStmt=conn.prepareStatement(strPricingFlag);
		            pStmt.setLong(1,lpricingSeqId);
		            rs = pStmt.executeQuery();
		            if(rs != null){
		                while(rs.next()){
		                	insPricingVO =  new InsPricingVO();
		                	insPricingVO.setGroupProfileSeqID(lpricingSeqId);
		                	insPricingVO.setBenecoverFlagYN(TTKCommon.checkNull(rs.getString("BENF_COV_SVE_YN")));
		                	insPricingVO.setCalCPMFlagYN(TTKCommon.checkNull(rs.getString("CAL_CPM_SVE_YN")));
		                	insPricingVO.setLoadingFlagYN(TTKCommon.checkNull(rs.getString("CAL_LODING_SVE_YN")));
		                	insPricingVO.setDemographicflagYN(TTKCommon.checkNull(rs.getString("DMGRPHC_SAVE_YN")));
		                	insPricingVO.setPricingmodifyYN(TTKCommon.checkNull(rs.getString("MODIFY_YN")));
		                	insPricingVO.setCompleteSaveYNInSc2(TTKCommon.checkNull(rs.getString("CENSUS_SAVE_YN")));
		                	insPricingVO.setRiskPremiumDate(TTKCommon.checkNull(rs.getString("ADDED_DATE")));
		                	int trendfactor =  rs.getInt("TRND_FACTOR_PERC");
		                	
		                	if(trendfactor < 6){
		                		insPricingVO.setTrendFactor("Y");
		                	}else{
		                		insPricingVO.setTrendFactor("N");
		                	}
		              
		                
		                }//end of while(rs.next())
		            }//end of if(rs != null)
		            return insPricingVO;
		        }//end of try
		        catch (SQLException sqlExp) 
		        {
		            throw new TTKException(sqlExp, "policy");
		        }//end of catch (SQLException sqlExp)
		        catch (Exception exp) 
		        {
		            throw new TTKException(exp, "policy");
		        }//end of catch (Exception exp) 
		        finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
							if (rs != null) rs.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in PolicyDAOImpl getGroup()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (pStmt != null) pStmt.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in PolicyDAOImpl getGroup()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in PolicyDAOImpl getGroup()",sqlExp);
									throw new TTKException(sqlExp, "policy");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "policy");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs = null;
						pStmt = null;
						conn = null;
					}//end of finally
				}//end of finally
		    }//end of getGroup(long lngOfficeSeqID)


			
		 public ArrayList getAfterLoadingData(InsPricingVO insPricingVO)  throws TTKException {
		    	
			    Collection<Object> alResultList = new ArrayList<Object>();
		    	
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs1 = null;
		        ResultSet rs2 = null;
		      
		  
		        SwPricingSummaryVO swPricingSummaryVO = null;
		        try{
		        	conn = ResourceManager.getConnection();
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strLoadingAfterCalc);
					cStmtObject.setLong(1,insPricingVO.getGroupProfileSeqID());
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs1 = (java.sql.ResultSet)cStmtObject.getObject(2);
				
			            if(rs1 != null){
			                while (rs1.next()) {
			                	swPricingSummaryVO = new SwPricingSummaryVO();
			              
			                	swPricingSummaryVO.setGrp_load_SeqId(rs1.getLong("grp_ld_seq_id"));
			                	swPricingSummaryVO.setLngGroupProfileSeqID(rs1.getLong("grp_prof_seq_id"));
			                	swPricingSummaryVO.setAddedBy(insPricingVO.getAddedBy());
			                	
			                	swPricingSummaryVO.setLoad_DeductTypeSeqId(rs1.getLong("LOADING_TYPE_SEQ_ID"));
			                	swPricingSummaryVO.setLoad_DeductType(TTKCommon.checkNull(rs1.getString("LOATING_DEDUCT_TYPE")));
			                	if(rs1.getString("loading_perc") !=null){
			                		swPricingSummaryVO.setLoad_DeductTypePercentage(TTKCommon.checkNull(rs1.getString("LOADING_PERC")));
			                	}
			                	swPricingSummaryVO.setLoadComments(TTKCommon.checkNull(rs1.getString("LODING_REMARKS")));
			                	
			                	alResultList.add(swPricingSummaryVO);
		    							}
			            }
			          
			           
			     
					return (ArrayList)alResultList;	
		    	}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
						
							if (rs2 != null) rs2.close();
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs2 = null;
						rs1 = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}
		 
		 
		 public ArrayList getBeforeLoadingData(InsPricingVO insPricingVO)  throws TTKException {
		    	
			    Collection<Object> alResultList = new ArrayList<Object>();
		    	
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs1 = null;
		        ResultSet rs2 = null;
		      
		        SwPricingSummaryVO swPricingSummaryVO = null;
		        try{
		        	conn = ResourceManager.getConnection();
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strMasterListValue);
					cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);//loading
					cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);//IP OP sum

					cStmtObject.execute();
					rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
				
			            if(rs1 != null){
			                while (rs1.next()) {
			                	swPricingSummaryVO = new SwPricingSummaryVO();
			                
			        
			                	
			                	swPricingSummaryVO.setGrp_load_SeqId(null);
			                	swPricingSummaryVO.setLngGroupProfileSeqID(insPricingVO.getGroupProfileSeqID());
			                	swPricingSummaryVO.setAddedBy(insPricingVO.getAddedBy());
			            
			                	swPricingSummaryVO.setLoad_DeductTypeSeqId(rs1.getLong("LOADING_TYPE_SEQ_ID"));
			                	swPricingSummaryVO.setLoad_DeductType(TTKCommon.checkNull(rs1.getString("LOATING_DEDUCT_TYPE")));
			                	swPricingSummaryVO.setLoad_DeductTypePercentage(rs1.getString("PERC"));//default value is 10
			                	swPricingSummaryVO.setLoadComments("");
			                  	alResultList.add(swPricingSummaryVO);
		    							}
			            }
			          
			           
			     
					return (ArrayList)alResultList;	
		    	}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
						
							if (rs2 != null) rs2.close();
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs2 = null;
						rs1 = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}

		public int saveLoading(SwPricingSummaryVO swPricingSummaryVO) throws TTKException
		{
			int iResult=0;
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ArrayList alLoadinglist = new ArrayList();
			try
			{
			
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveLoading);
				alLoadinglist = 	swPricingSummaryVO.getAlLoadingGrosspremium();
				int i= 0;
				    if(!(alLoadinglist.isEmpty())){
		                for(Object object:alLoadinglist){
		                i++;//condition is required to write
		            swPricingSummaryVO = (SwPricingSummaryVO)object;
		          
		            if(swPricingSummaryVO.getGrp_load_SeqId()!= null)
		            {
		            cStmtObject.setLong(1,swPricingSummaryVO.getGrp_load_SeqId());
		         
		            }else{
		            cStmtObject.setLong(1,0);
		            }
		            
		            if(swPricingSummaryVO.getLngGroupProfileSeqID()!= null)
		            {
		            cStmtObject.setLong(2,swPricingSummaryVO.getLngGroupProfileSeqID());
		         
		            }else{
		            cStmtObject.setLong(2,0);
		            }
		            
		            cStmtObject.setLong(3,swPricingSummaryVO.getLoad_DeductTypeSeqId());
		            
		            if(swPricingSummaryVO.getLoad_DeductTypePercentage() != null && swPricingSummaryVO.getLoad_DeductTypePercentage() != ""){
		               cStmtObject.setBigDecimal(4,new BigDecimal(swPricingSummaryVO.getLoad_DeductTypePercentage())); 
		            }else{
			            	cStmtObject.setBigDecimal(4,null);
			         }
		            
		           cStmtObject.setLong(5,swPricingSummaryVO.getAddedBy());
		            cStmtObject.addBatch();
				}
		                cStmtObject.executeBatch(); 
				}//end of try
				     iResult=i;
				  
				     
			}
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
	        	
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        			throw new TTKException(sqlExp, "insurance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null)
	        					{
	        					conn.close();
	        					}
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        				throw new TTKException(sqlExp, "insurance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "insurance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			return iResult;
		}//end of deleteInsuranceCompany(String strInsSeqID)

		
		public int calculateLoading(SwPricingSummaryVO swPricingSummaryVO) throws TTKException
		{
			int iResult=0;
			Connection conn = null;
			CallableStatement cStmtObject=null;
			try
			{
			
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCalculateLoading);
				
				 if(swPricingSummaryVO.getFinaldataSeqID()!= null)
		            {
		            cStmtObject.setLong(1,swPricingSummaryVO.getFinaldataSeqID());
		         
		            }else{
		            cStmtObject.setLong(1,0);
		            }
				
				
				  if(swPricingSummaryVO.getLngGroupProfileSeqID()!= null)
		            {
		            cStmtObject.setLong(2,swPricingSummaryVO.getLngGroupProfileSeqID());
		         
		            }else{
		            cStmtObject.setLong(2,0);
		            }
		           cStmtObject.setLong(3,swPricingSummaryVO.getAddedBy());
		           cStmtObject.setString(4,swPricingSummaryVO.getLoadComments());
				   cStmtObject.execute(); 
				   iResult = 1; 
				     
			}
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
	        	
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        			throw new TTKException(sqlExp, "insurance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null)
	        					{
	        					conn.close();
	        					}
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        				throw new TTKException(sqlExp, "insurance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "insurance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			return iResult;
		}//end of deleteInsuranceCompany(String strInsSeqID)

	
		 @SuppressWarnings("null")
		public SwPolicyConfigVO getcpmAfterLoading(InsPricingVO insPricingVO)  throws TTKException {
		    	
		    	
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs1 = null;
		        ResultSet rs2 = null;
		      
		        SwPolicyConfigVO swPolicyConfigVO = null;
		        try{
		        	conn = ResourceManager.getConnection();
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strCPMAfterCalLoading);
					 if(insPricingVO.getFinaldataSeqID()!= null)
			            {
			            cStmtObject.setLong(1,insPricingVO.getFinaldataSeqID());
			         
			            }else{
			            cStmtObject.setLong(1,0);
			            }
					
					cStmtObject.setLong(1,insPricingVO.getGroupProfileSeqID());
					cStmtObject.setLong(2,insPricingVO.getAddedBy());
					cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
				
				
			            if(rs1 != null){
			                if (rs1.next()) {
			                	swPolicyConfigVO = new SwPolicyConfigVO();
			                	
			                	swPolicyConfigVO.setLngGroupProfileSeqID(rs1.getLong("GRP_PROF_SEQ_ID"));
			                	swPolicyConfigVO.setFinaldataSeqID(rs1.getLong("FNL_CPM_SEQ_ID"));
			                	swPolicyConfigVO.setAddedBy(insPricingVO.getAddedBy());
			                	
			                	swPolicyConfigVO.setFindataType(TTKCommon.checkNull(rs1.getString("DATA_TYPE")));
			                	swPolicyConfigVO.setFinpolicyNo(TTKCommon.checkNull(rs1.getString("POLICY_NO")));
			                	swPolicyConfigVO.setFinclientCode(TTKCommon.checkNull(rs1.getString("CLIENT_CODE")));
			                	swPolicyConfigVO.setFineffectiveYear(TTKCommon.checkNull(rs1.getString("EFFECTIVE_YEAR"))); 
			                	if(rs1.getDate("POL_EFFECTIVE_DATE") != null){
			                	swPolicyConfigVO.setFinpolicyEffDate(new Date(rs1.getTimestamp("POL_EFFECTIVE_DATE").getTime()));
			                	}
			                	if(rs1.getDate("POL_EXPIRE_DATE") != null){
			                	swPolicyConfigVO.setFinpolicyExpDate(new Date(rs1.getTimestamp("POL_EXPIRE_DATE").getTime()));
			                	}
			                	swPolicyConfigVO.setFinpolicyDurationPerMonth(rs1.getLong("POL_DURATION_MONTH"));
			                	swPolicyConfigVO.setFinnoOfLives(rs1.getLong("NO_LIVES"));
			                	if(rs1.getString("MODIFY_YN").equalsIgnoreCase("N")){
			                	
			                	if(rs1.getString("IP_CPM") != null){
			                		swPolicyConfigVO.setFininPatientCPM(TTKCommon.checkNull(rs1.getString("IP_CPM"))); 
			    				}
			                	
			                	if(rs1.getString("OP_CPM") != null){
			                		swPolicyConfigVO.setFinoutPatientCPM(TTKCommon.checkNull(rs1.getString("OP_CPM"))); 
			    				}
			                	
			                	if(rs1.getString("OPL_CPM") != null){
			                		swPolicyConfigVO.setFinopticalCPM(TTKCommon.checkNull(rs1.getString("OPL_CPM"))); 
			    				}
			                	if(rs1.getString("DNT_CPM") != null){
			                		swPolicyConfigVO.setFindentalCPM(TTKCommon.checkNull(rs1.getString("DNT_CPM"))); 
			    				}
			                	if(rs1.getString("EXCPT_MAT_CPM") != null){
				                	swPolicyConfigVO.setFinallExlMaternity(TTKCommon.checkNull(rs1.getString("EXCPT_MAT_CPM"))); 
			    				}
			                	if(rs1.getString("MAT_CPM") != null){
				                	swPolicyConfigVO.setFinmaternityCPM(TTKCommon.checkNull(rs1.getString("MAT_CPM"))); 
			    				}
		                	
			                	
			                	}else if(rs1.getString("MODIFY_YN").equalsIgnoreCase("Y")){
			                		swPolicyConfigVO.setFininPatientCPM(""); 
			                		swPolicyConfigVO.setFinoutPatientCPM(""); 
			                		swPolicyConfigVO.setFinopticalCPM(""); 
			                		swPolicyConfigVO.setFindentalCPM(""); 
			                		swPolicyConfigVO.setFinallExlMaternity(""); 
			                		swPolicyConfigVO.setFinmaternityCPM(""); 
			                		swPolicyConfigVO.setFinfinalweightage("");  
			                
				                	}
			                	
			                }// end if next() loop
			           
		                	
		                }
			          
			           
			     
					return swPolicyConfigVO;	
		    	}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
						
							if (rs2 != null) rs2.close();
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs2 = null;
						rs1 = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}

	
		 /**
		     * This method returns the RuleOPAVO, which contains the Flag for execution of OPA Rule.
		     * @param claimSeqID long value contains seq id  
		     * @return RuleOPAVO object which contains the Flag for execution of OPA Rule.
		     * @exception throws TTKException
		     */  
		    
		    public InsPricingVO getPolicyNumberStatus(String policyNumber) throws TTKException {
		    	
				Connection conn = null;
				PreparedStatement pStmt = null;
				ResultSet rs1 = null; 
				InsPricingVO insPricingVO = new InsPricingVO();
				  
				try{
					conn = ResourceManager.getConnection();
					pStmt = conn.prepareStatement(strPolicyNumberFlag);
					pStmt.setString(1,policyNumber); 
					rs1 = pStmt.executeQuery();
					if(rs1!=null){
						if(rs1.next()){
							insPricingVO.setPolicyNumberFlag(TTKCommon.checkNull(rs1.getString("VALID_POL_YN")));  
							insPricingVO.setClientCode(TTKCommon.checkNull(rs1.getString("GROUP_NAME")));  
						
						}//end of while(rs.next())
						else{
							insPricingVO.setPolicyNumberFlag("N");  
						}
					}// End of  if(rs!=null)
					
					return insPricingVO;
				}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "claim");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "claim");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */
					try // First try closing the result set
					{
						try
						{
							 
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in ClaimDAOImpl getClaimsExecutionStatusData()",sqlExp);
							throw new TTKException(sqlExp, "claim");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (pStmt != null) pStmt.close(); 
								
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in ClaimDAOImpl getClaimsExecutionStatusData()",sqlExp);
								throw new TTKException(sqlExp, "claim");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in ClaimDAOImpl getClaimsExecutionStatusData()",sqlExp);
									throw new TTKException(sqlExp, "claim");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "claim");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects
					{
						rs1 = null; 
						pStmt = null;
						conn = null;
					}//end of finally
				}//end of finally 
			}////End of getClaimsExecutionStatusData(Long claimSeqID)
		    
		 public SwPricingSummaryVO getcpmAfterLoadingPricing(InsPricingVO insPricingVO)  throws TTKException {
		    	
			    Collection<Object> alResultList = new ArrayList<Object>();
		    	
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs1 = null;
		        ResultSet rs2 = null;
		      
		        SwPricingSummaryVO swPricingSummaryVO = null;
		        try{
		        	conn = ResourceManager.getConnection();
					cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strCPMAfterCalLoading);
					 if(insPricingVO.getFinaldataSeqID()!= null)
			            {
			            cStmtObject.setLong(1,insPricingVO.getFinaldataSeqID());
			         
			            }else{
			            cStmtObject.setLong(1,0);
			            }
					
					cStmtObject.setLong(1,insPricingVO.getGroupProfileSeqID());

					cStmtObject.setLong(2,insPricingVO.getAddedBy());
					cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
					swPricingSummaryVO = new SwPricingSummaryVO();
				
			            if(rs1 != null){
			                if (rs1.next()) {
			                	
			                	
			          
			                	
			                	swPricingSummaryVO.setLngGroupProfileSeqID(rs1.getLong("GRP_PROF_SEQ_ID"));
			                	swPricingSummaryVO.setFinaldataSeqID(rs1.getLong("FNL_CPM_SEQ_ID"));
			                	swPricingSummaryVO.setAddedBy(insPricingVO.getAddedBy());
			              
			                	
			                	swPricingSummaryVO.setFindataType(TTKCommon.checkNull(rs1.getString("DATA_TYPE")));
			                	swPricingSummaryVO.setFinpolicyNo(TTKCommon.checkNull(rs1.getString("POLICY_NO")));
			                	swPricingSummaryVO.setFinclientCode(TTKCommon.checkNull(rs1.getString("CLIENT_CODE")));
			                	swPricingSummaryVO.setFineffectiveYear(TTKCommon.checkNull(rs1.getString("EFFECTIVE_YEAR"))); 
			                	if(rs1.getDate("POL_EFFECTIVE_DATE") != null){
			                		swPricingSummaryVO.setFinpolicyEffDate(new Date(rs1.getTimestamp("POL_EFFECTIVE_DATE").getTime()));
			                	}
			                	if(rs1.getDate("POL_EXPIRE_DATE") != null){
			                		swPricingSummaryVO.setFinpolicyExpDate(new Date(rs1.getTimestamp("POL_EXPIRE_DATE").getTime()));
			                	}
			                	swPricingSummaryVO.setFinpolicyDurationPerMonth(rs1.getLong("POL_DURATION_MONTH"));
			                	swPricingSummaryVO.setFinnoOfLives(rs1.getLong("NO_LIVES"));
			                	
			                	if(rs1.getString("MODIFY_YN").equalsIgnoreCase("N")){

			                		
			                
			                	if(rs1.getString("IP_CPM") != null){
			                		swPricingSummaryVO.setFininPatientCPM(TTKCommon.checkNull(rs1.getString("IP_CPM"))); 
			    				}
			                	
			                	if(rs1.getString("OP_CPM") != null){
			                		swPricingSummaryVO.setFinoutPatientCPM(TTKCommon.checkNull(rs1.getString("OP_CPM"))); 
			    				}
			                	
			                	if(rs1.getString("OPL_CPM") != null){
			                		swPricingSummaryVO.setFinopticalCPM(TTKCommon.checkNull(rs1.getString("OPL_CPM"))); 
			    				}
			                	if(rs1.getString("DNT_CPM") != null){
			                		swPricingSummaryVO.setFindentalCPM(TTKCommon.checkNull(rs1.getString("DNT_CPM"))); 
			    				}
			                	if(rs1.getString("EXCPT_MAT_CPM") != null){
			                		swPricingSummaryVO.setFinallExlMaternity(TTKCommon.checkNull(rs1.getString("EXCPT_MAT_CPM"))); 
			    				}
			                	if(rs1.getString("MAT_CPM") != null){
			                		swPricingSummaryVO.setFinmaternityCPM(TTKCommon.checkNull(rs1.getString("MAT_CPM"))); 
			    				}
			              
			                	/*if(rs1.getString("WEIGHTAGE") != null){
			                		swPricingSummaryVO.setFinfinalweightage(TTKCommon.checkNull(rs1.getString("WEIGHTAGE"))); 
			    				}*/
			                	/*if(rs1.getString("IP_WEGHT") != null){
			                		swPricingSummaryVO.setFininpatientcrediblty(TTKCommon.checkNull(rs1.getString("IP_WEGHT"))); 
			    				}
		                	if(rs1.getString("OP_WEGHT") != null){
		                		swPricingSummaryVO.setFinoutpatientcrediblty(TTKCommon.checkNull(rs1.getString("OP_WEGHT"))); 
		    				}
		                	if(rs1.getString("MAT_WEGHT") != null){
		                		swPricingSummaryVO.setFinmaternitycrediblty(TTKCommon.checkNull(rs1.getString("MAT_WEGHT"))); 
		    				}
		                	if(rs1.getString("OPTL_WEGHT") != null){
		                		swPricingSummaryVO.setFinopticalcrediblty(TTKCommon.checkNull(rs1.getString("OPTL_WEGHT"))); 
		    				}
		                	if(rs1.getString("DNT_WEGHT") != null){
		                		swPricingSummaryVO.setFindentalcrediblty(TTKCommon.checkNull(rs1.getString("DNT_WEGHT"))); 
		    				}*/
			                	
			                	
			                	
			                	}else if(rs1.getString("MODIFY_YN").equalsIgnoreCase("Y")){
			                	swPricingSummaryVO.setFininPatientCPM(""); 
			                	swPricingSummaryVO.setFinoutPatientCPM(""); 
			                	swPricingSummaryVO.setFinopticalCPM(""); 
			                	swPricingSummaryVO.setFindentalCPM(""); 
			                	swPricingSummaryVO.setFinallExlMaternity(""); 
			                	swPricingSummaryVO.setFinmaternityCPM(""); 
			                	swPricingSummaryVO.setFinfinalweightage("");  
			                	swPricingSummaryVO.setFininpatientcrediblty("");  
			                	swPricingSummaryVO.setFinoutpatientcrediblty("");  
			                	swPricingSummaryVO.setFinmaternitycrediblty("");  
			                	swPricingSummaryVO.setFinopticalcrediblty("");  
			                	swPricingSummaryVO.setFindentalcrediblty("");  
			                	}
			                	
			                }
			              
			            }
			          
			           
			     
					return swPricingSummaryVO;	
		    	}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "insurance");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "insurance");
				}//end of catch (Exception exp)
				finally
				{
					/* Nested Try Catch to ensure resource closure */ 
					try // First try closing the result set
					{
						try
						{
						
							if (rs2 != null) rs2.close();
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null)	cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) {
			        					conn.close();
			                					}
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs2 = null;
						rs1 = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}

			
		public ArrayList saveDemographicData(SwPolicyConfigVO swpolicyConfigVO) throws TTKException
		{
			int iResult=0;
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ArrayList arraylist = new ArrayList();
			ArrayList alDemoPastdatalist = new ArrayList();
			try
			{
			
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveDemographicData);
				alDemoPastdatalist = 	swpolicyConfigVO.getAlDemoPastData();
				int i= 0;
				    if(!(alDemoPastdatalist.isEmpty())){
		                for(Object object:alDemoPastdatalist){
		                i++;
		            swpolicyConfigVO = (SwPolicyConfigVO)object;
		        
		            if(swpolicyConfigVO.getDemographicSeqId()!= null)
		            {
		            cStmtObject.setLong(1,swpolicyConfigVO.getDemographicSeqId());
		         
		            }else{
		            cStmtObject.setLong(1,0);
		            }
		            
		            if(swpolicyConfigVO.getLngGroupProfileSeqID()!= null)
		            {
		            cStmtObject.setLong(2,swpolicyConfigVO.getLngGroupProfileSeqID());
		         
		            }else{
		            cStmtObject.setLong(2,0);
		            }
		  
		            cStmtObject.setString(3,swpolicyConfigVO.getDemodataType()); 
		            cStmtObject.setString(4,swpolicyConfigVO.getDemoClientCode()); 
		          
		            cStmtObject.setString(5,swpolicyConfigVO.getDemoPolicyNo());
		         

		            if(swpolicyConfigVO.getDemoEffectiveYear() != null){
		            cStmtObject.setString(6,swpolicyConfigVO.getDemoEffectiveYear());
		            }else{
		            	cStmtObject.setLong(6,0);
		            }
		            if(swpolicyConfigVO.getDemoPolicyEffDate() != null){
					cStmtObject.setString(7,swpolicyConfigVO.getDemoPolicyEffDate());//LETTER_DATE
		            }else{
		            	cStmtObject.setString(7,null);
		            }
					
		            if(swpolicyConfigVO.getDemoPolicyExpDate() != null){
						cStmtObject.setString(8,swpolicyConfigVO.getDemoPolicyExpDate());//LETTER_DATE
			        }else{
			        cStmtObject.setTimestamp(8,null);
			        }
		            
		            if(swpolicyConfigVO.getDemoPolicyDurationPerMonth() != null){
			            cStmtObject.setString(9,swpolicyConfigVO.getDemoPolicyDurationPerMonth());
			            }else{
			            	cStmtObject.setLong(9,0);
			            }
		            
		            if(swpolicyConfigVO.getDemoNoOfLives() != null){
			            cStmtObject.setString(10,swpolicyConfigVO.getDemoNoOfLives());
			            }else{
			            	cStmtObject.setLong(10,0);
			            }
		          
		           
		            if(swpolicyConfigVO.getDemoAverageAge() != null && swpolicyConfigVO.getDemoAverageAge() !=""){
			            cStmtObject.setBigDecimal(11,new BigDecimal(swpolicyConfigVO.getDemoAverageAge()));
			            }else{
			            	cStmtObject.setBigDecimal(11,null);
			            }
			      

		            cStmtObject.setString(12,TTKCommon.checkNull(swpolicyConfigVO.getDemoNationality()));
			        cStmtObject.setString(13,TTKCommon.checkNull(swpolicyConfigVO.getDemoAreaOfCover()));
			        cStmtObject.setString(14,TTKCommon.checkNull(swpolicyConfigVO.getDemoNetwork()));
		            
		          /*  if(swpolicyConfigVO.getDemoMaximumBenfitLimit() != null && swpolicyConfigVO.getDemoMaximumBenfitLimit() !=""){
			            cStmtObject.setBigDecimal(15,new BigDecimal(swpolicyConfigVO.getDemoMaximumBenfitLimit()));
			            }else{
			            	cStmtObject.setBigDecimal(15,null);
			            }*/
		            cStmtObject.setString(15,TTKCommon.checkNull(swpolicyConfigVO.getDemoMaximumBenfitLimit()));
		            
		            cStmtObject.setString(16,TTKCommon.checkNull(swpolicyConfigVO.getDemoOpCopay()));
		            cStmtObject.setString(17,TTKCommon.checkNull(swpolicyConfigVO.getDemoOPDeductable()));
		            cStmtObject.setString(18,TTKCommon.checkNull(swpolicyConfigVO.getDemoMaternityCoverage()));
		           
		            
		          /*  if(swpolicyConfigVO.getDemoMaternityLimit() != null &&  swpolicyConfigVO.getDemoMaternityLimit()!= ""){
			            	cStmtObject.setBigDecimal(19,new BigDecimal(swpolicyConfigVO.getDemoMaternityLimit()));
				            }else{
				            	cStmtObject.setBigDecimal(19,null);
				            }*/
		            cStmtObject.setString(19,TTKCommon.checkNull(swpolicyConfigVO.getDemoMaternityLimit()));
		            cStmtObject.setString(20,TTKCommon.checkNull(swpolicyConfigVO.getDemoOpticalCoverage()));

		            
		        /*  if(swpolicyConfigVO.getDemoOpticalLimit() != null &&  swpolicyConfigVO.getDemoOpticalLimit()!= ""){
				         cStmtObject.setBigDecimal(21,new BigDecimal(swpolicyConfigVO.getDemoOpticalLimit()));
					        }else{
					         cStmtObject.setBigDecimal(21,null);
					        }*/
		            cStmtObject.setString(21,TTKCommon.checkNull(swpolicyConfigVO.getDemoOpticalLimit()));

		            cStmtObject.setString(22,TTKCommon.checkNull(swpolicyConfigVO.getDemoOpticalCopay()));
		            cStmtObject.setString(23,TTKCommon.checkNull(swpolicyConfigVO.getDemoDentalCoverage()));
		         
		        /*  if(swpolicyConfigVO.getDemoDentalLimit() != null &&  swpolicyConfigVO.getDemoDentalLimit()!= ""){
		            	cStmtObject.setBigDecimal(24,new BigDecimal(swpolicyConfigVO.getDemoDentalLimit()));
			            }else{
			            	cStmtObject.setBigDecimal(24,null);
			            }*/
		            cStmtObject.setString(24,TTKCommon.checkNull(swpolicyConfigVO.getDemoDentalLimit()));

		           cStmtObject.setString(25,TTKCommon.checkNull(swpolicyConfigVO.getDemoDentalCopay()));
		        
		          if(swpolicyConfigVO.getDemoSlNo() != null &&  swpolicyConfigVO.getDemoSlNo()!= ""){
		            	cStmtObject.setBigDecimal(26,new BigDecimal(swpolicyConfigVO.getDemoSlNo()));
			            }else{
			            	cStmtObject.setBigDecimal(26,null);
			            }
		          
		          cStmtObject.setString(27,TTKCommon.checkNull(swpolicyConfigVO.getDemoAlahli()));
		            cStmtObject.setString(28,swpolicyConfigVO.getMaternityCopay());
		            cStmtObject.setLong(29,swpolicyConfigVO.getAddedBy());
					//cStmtObject.registerOutParameter(27, Types.INTEGER);//out parameter which gives the number 
		            cStmtObject.addBatch();
				}
		                cStmtObject.executeBatch(); 
				}//end of try
				     iResult=i;
				     arraylist.add(iResult);
				     
			}
			//end of catch (SQLException sqlExp)
			catch (BatchUpdateException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "insurance");
			}
			catch (Exception exp)
			{
				throw new TTKException(exp, "insurance");
			}//end of catch (Exception exp)
			finally
			{
	        	
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        			throw new TTKException(sqlExp, "insurance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null)
	        					{
	        					conn.close();
	        					}
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
	        				throw new TTKException(sqlExp, "insurance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "insurance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			return arraylist;
		}//end of deleteInsuranceCompany(String strInsSeqID)
				public int swSaveQuotationdetails(SwFinalQuoteVO swFinalQuoteVO) throws TTKException
				{
					int iResult=0;
					Connection conn = null;
					CallableStatement cStmtObject=null;
					
					try
					{

						conn = ResourceManager.getConnection();
						cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strswSaveQuotationDetails);
						cStmtObject.setLong(1,swFinalQuoteVO.getLngGroupProfileSeqID());	//num
						cStmtObject.setLong(2,swFinalQuoteVO.getProductSeqID());//num
						cStmtObject.setLong(3,swFinalQuoteVO.getAddedBy());		//num
						cStmtObject.setString(4,swFinalQuoteVO.getLogicType());	
						cStmtObject.setString(5,swFinalQuoteVO.getAdministrationCharges());	
						cStmtObject.setString(6,swFinalQuoteVO.getCreditGeneration());	
						cStmtObject.setString(7,swFinalQuoteVO.getCreditGenerationOth());	
						cStmtObject.setString(8,swFinalQuoteVO.getMaternityMinBand());	
						cStmtObject.setString(9,swFinalQuoteVO.getMaternityMaxBand());	
						
						cStmtObject.registerOutParameter(10,Types.INTEGER);//num
					
					
						cStmtObject.execute();	
						iResult = cStmtObject.getInt(10);
						
					}//end of try
					catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (Exception exp)
					finally
					{
			        	/* Nested Try Catch to ensure resource closure */ 
			        	try // First try closing the Statement
			        	{
			        		try
			        		{
			        			if (cStmtObject != null) cStmtObject.close();
			        		}//end of try
			        		catch (SQLException sqlExp)
			        		{
			        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
			        			throw new TTKException(sqlExp, "insurance");
			        		}//end of catch (SQLException sqlExp)
			        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			        		{
			        			try
			        			{
			        				if(conn != null)
			        					{
			        					conn.close();
			        					}
			        			}//end of try
			        			catch (SQLException sqlExp)
			        			{
			        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
			        				throw new TTKException(sqlExp, "insurance");
			        			}//end of catch (SQLException sqlExp)
			        		}//end of finally Connection Close
			        	}//end of try
			        	catch (TTKException exp)
			        	{
			        		throw new TTKException(exp, "insurance");
			        	}//end of catch (TTKException exp)
			        	finally // Control will reach here in anycase set null to the objects 
			        	{
			        		cStmtObject = null;
			        		conn = null;
			        	}//end of finally
					}//end of finally
					return iResult;
				}//end of 

				
				
				
				 public SwFinalQuoteVO swSelectQuotationdetails(SwFinalQuoteVO swFinalQuoteVO)  throws TTKException {
				    	
				    	
						Connection conn = null;
				    	CallableStatement cStmtObject=null;
				        ResultSet rs1 = null;
				      
				       
				        try{
				        	conn = ResourceManager.getConnection();
							cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strQuoPolicyDetails);
							
					        cStmtObject.setLong(1,swFinalQuoteVO.getLngGroupProfileSeqID());
							cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
							cStmtObject.execute();
							rs1 = (java.sql.ResultSet)cStmtObject.getObject(2);
						
					            if(rs1 != null){
					                if (rs1.next()) {
					                	//swFinalQuoteVO = new SwFinalQuoteVO();
					                	
					                
					                	if(rs1.getString("POLICY_SEQ_ID") != null){
					                		swFinalQuoteVO.setPolicySeqid(rs1.getLong("POLICY_SEQ_ID")); 
					    				}
					                	
					                	if(rs1.getString("ENROL_BATCH_SEQ_ID") != null){
					                		swFinalQuoteVO.setEnrollbatchSeqid(rs1.getLong("ENROL_BATCH_SEQ_ID")); 
					    				}
					                	
					                	if(rs1.getString("POLICY_NUMBER") != null){
					                		swFinalQuoteVO.setPolicyNumber(TTKCommon.checkNull(rs1.getString("POLICY_NUMBER"))); 
					    				}
					                	if(rs1.getString("BATCH_NUMBER") != null){
					                		swFinalQuoteVO.setBatchNumber(TTKCommon.checkNull(rs1.getString("BATCH_NUMBER"))); 
					    				}
					                	if(rs1.getString("PRODUCT_SEQ_ID") != null){
					                		swFinalQuoteVO.setProductSeqID(rs1.getLong("PRODUCT_SEQ_ID")); 
					    				}
					                	
					                	if(rs1.getString("GROUP_ID") != null){
					                		swFinalQuoteVO.setGroupId(TTKCommon.checkNull(rs1.getString("GROUP_ID"))); 
					    				}
					                	if(rs1.getString("INS_COMP_NAME") != null){
					                		swFinalQuoteVO.setCompanyName(TTKCommon.checkNull(rs1.getString("INS_SEQ_ID"))); 
					    				}
					                	
					                	if(rs1.getString("INS_COMP_CODE_NUMBER") != null){
					                		swFinalQuoteVO.setOfficeCode(TTKCommon.checkNull(rs1.getString("INS_COMP_CODE_NUMBER"))); 
					    				}
					                	
					                	if(rs1.getString("TREND_FCTR_APRVL_YN") != null){
					                		swFinalQuoteVO.setTrendFactorYNValue(TTKCommon.checkNull(rs1.getString("TREND_FCTR_APRVL_YN"))); 
					    				}
					                
					                	
					                	if(rs1.getString("LOGIC_TYPE_ID") != null){
					                		swFinalQuoteVO.setLogicType(TTKCommon.checkNull(rs1.getString("LOGIC_TYPE_ID"))); 
					    				}
					                	
					                	if(rs1.getString("CLM_ADMN_CHRGS") != null){
					                		swFinalQuoteVO.setAdministrationCharges(TTKCommon.checkNull(rs1.getString("CLM_ADMN_CHRGS"))); 
					    				}
					                	
					                	if(rs1.getString("MEM_CANCL_DAYS_TYPE_ID") != null){
					                		swFinalQuoteVO.setCreditGeneration(TTKCommon.checkNull(rs1.getString("MEM_CANCL_DAYS_TYPE_ID"))); 
					    				}
					                	
					                	if(rs1.getString("MEM_CANCL_DAYS") != null){
					                		swFinalQuoteVO.setCreditGenerationOth(TTKCommon.checkNull(rs1.getString("MEM_CANCL_DAYS"))); 
					    				}
					                	
					                	if(rs1.getString("MAT_PREMUM_FROM_AGE") != null){
					                		swFinalQuoteVO.setMaternityMinBand(TTKCommon.checkNull(rs1.getString("MAT_PREMUM_FROM_AGE"))); 
					    				}
					                	
					                	if(rs1.getString("MAT_PREMUM_TO_AGE") != null){
					                		swFinalQuoteVO.setMaternityMaxBand(TTKCommon.checkNull(rs1.getString("MAT_PREMUM_TO_AGE"))); 
					    				}
				
					                }
					              
					            }
					     
							return swFinalQuoteVO;	
				    	}//end of try
						catch (SQLException sqlExp)
						{
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						catch (Exception exp)
						{
							throw new TTKException(exp, "insurance");
						}//end of catch (Exception exp)
						finally
						{
							/* Nested Try Catch to ensure resource closure */ 
							try // First try closing the result set
							{
								try
								{
									if (rs1 != null) rs1.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
								finally // Even if result set is not closed, control reaches here. Try closing the statement now.
								{
									try
									{
										if (cStmtObject != null)	cStmtObject.close();
									}//end of try
									catch (SQLException sqlExp)
									{
										log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
										throw new TTKException(sqlExp, "insurance");
									}//end of catch (SQLException sqlExp)
									finally // Even if statement is not closed, control reaches here. Try closing the connection now.
									{
										try
										{
											if(conn != null) {
					        					conn.close();
					                					}
										}//end of try
										catch (SQLException sqlExp)
										{
											log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
											throw new TTKException(sqlExp, "insurance");
										}//end of catch (SQLException sqlExp)
									}//end of finally Connection Close
								}//end of finally Statement Close
							}//end of try
							catch (TTKException exp)
							{
								throw new TTKException(exp, "insurance");
							}//end of catch (TTKException exp)
							finally // Control will reach here in anycase set null to the objects 
							{
								rs1 = null;
								cStmtObject = null;
								conn = null;
							}//end of finally
						}//end of finally
					}


				public long swSave_pol_copies(SwFinalQuoteVO swFinalQuoteVO, byte[] data) throws TTKException
				{
					long lResult =0;
					Connection conn = null;
					CallableStatement cStmtObject=null;
					try{
						conn = ResourceManager.getConnection();
					
						cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePolicyCopy);
						
						if(swFinalQuoteVO.getPolcopyseqid() == null)
						{
						
							cStmtObject.setLong(1,0);
						}//end of if
						else
						{
							
							cStmtObject.setLong(1,swFinalQuoteVO.getPolcopyseqid());
						}//end of else
						
						if(swFinalQuoteVO.getLngGroupProfileSeqID()!=null)
						{
							
							cStmtObject.setLong(2,swFinalQuoteVO.getLngGroupProfileSeqID());
						}//end of if(addressVO.getLngGroupProfileSeqID()!=null)
						else
						{
							
							cStmtObject.setLong(2,0);
						}//end of else
						
						if (data!= null)
						{
						
							cStmtObject.setBytes(3, data);
						}//end of if byte data
						else{
							
							cStmtObject.setBytes(3,null);
						}//end of else
						cStmtObject.setString(4,swFinalQuoteVO.getFinalpolicydocYN());//fnl_doc_aprv_yn
						cStmtObject.setLong(5,swFinalQuoteVO.getAddedBy());
						cStmtObject.setString(6,swFinalQuoteVO.getTrendFactorYNValue());
						
						//cStmtObject.setString(7,swFinalQuoteVO.getAttachmentname1());
						if(swFinalQuoteVO.getSourceAttchments1() != null){
						byte[] iStream1	=	swFinalQuoteVO.getSourceAttchments1().getFileData();
						cStmtObject.setBytes(7, iStream1);
						}else{
							cStmtObject.setBytes(7, null);
						}
						
						cStmtObject.setString(8,swFinalQuoteVO.getQuotationNo());
						cStmtObject.setString(9,swFinalQuoteVO.getAttachmentname1());
						cStmtObject.registerOutParameter(10,Types.INTEGER);//ROW_PROCESSED
						cStmtObject.execute();
						lResult = cStmtObject.getLong(10);
					}//end of try
					catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (Exception exp)
					finally
					{
			        	/* Nested Try Catch to ensure resource closure */ 
			        	try // First try closing the Statement
			        	{
			        		try
			        		{
			        			if (cStmtObject != null) cStmtObject.close();
			        		}//end of try
			        		catch (SQLException sqlExp)
			        		{
			        			log.error("Error while closing the Statement in InsuranceDAOImpl addUpdateInsuranceCompany()",sqlExp);
			        			throw new TTKException(sqlExp, "insurance");
			        		}//end of catch (SQLException sqlExp)
			        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			        		{
			        			try
			        			{
			        				if(conn != null) conn.close();
			        			}//end of try
			        			catch (SQLException sqlExp)
			        			{
			        				log.error("Error while closing the Connection in InsuranceDAOImpl addUpdateInsuranceCompany()",sqlExp);
			        				throw new TTKException(sqlExp, "insurance");
			        			}//end of catch (SQLException sqlExp)
			        		}//end of finally Connection Close
			        	}//end of try
			        	catch (TTKException exp)
			        	{
			        		throw new TTKException(exp, "insurance");
			        	}//end of catch (TTKException exp)
			        	finally // Control will reach here in anycase set null to the objects 
			        	{
			        		cStmtObject = null;
			        		conn = null;
			        	}//end of finally
					}//end of finally
					return  lResult;
				}//  end of addUpdateInsuranceCompany(InsuranceDetailVO insuranceDetailVO) throws TTKException

			
				 public ArrayList getPolicyCopiesList(SwFinalQuoteVO swFinalQuoteVO) throws TTKException {
						Connection conn = null;
						CallableStatement cStmtObject=null;
						ResultSet rs = null;
						byte[] bytes = new byte[1024];  
						Blob blob	=	null;
						Collection<Object> alResultList = new ArrayList<Object>();
						InputStream iStream1	=	new ByteArrayInputStream(new String("").getBytes());
						Blob blob1	=	null;
						try 
						{ 
								conn = ResourceManager.getConnection();				
								cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSwPolicyCopyList);
							    cStmtObject.setLong(1,swFinalQuoteVO.getLngGroupProfileSeqID());
								cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
								cStmtObject.execute();
								rs = (java.sql.ResultSet)cStmtObject.getObject(2);
								if(rs != null)
								{
									while (rs.next()) {
										SwFinalQuoteVO swFinalQuoteVO2 = new SwFinalQuoteVO();
										
										swFinalQuoteVO2.setPolcopyseqid(rs.getLong("POL_CPY_SEQ_ID"));
										blob	=	rs.getBlob("POL_COPY_DOC") ;
										if(blob != null){
										ByteArrayOutputStream baos = new ByteArrayOutputStream();
										byte[] buf = new byte[1024];

										InputStream in = blob.getBinaryStream();

										int n = 0;
										while ((n=in.read(buf))>=0)
										{
										   baos.write(buf, 0, n);

										}
										in.close();
										 bytes = baos.toByteArray(); 		
										}
										
										if(blob!= null){
											swFinalQuoteVO2.setPolicydocYN("Y");
											swFinalQuoteVO2.setFiledocimageYN("shortfall");
											swFinalQuoteVO2.setFiledoctitle("Quotation");
										}else{
											swFinalQuoteVO2.setPolicydocYN("N");
										}
										if(TTKCommon.checkNull(rs.getString("FNL_DOC_APRV_YN")).equalsIgnoreCase("Y")){
											swFinalQuoteVO2.setFinalpolicydocYN("YES");
										}else if(TTKCommon.checkNull(rs.getString("FNL_DOC_APRV_YN")).equalsIgnoreCase("N")){
											swFinalQuoteVO2.setFinalpolicydocYN("NO");
										}
										
										
										swFinalQuoteVO2.setQuotationNo(rs.getString("QUOT_NO"));
										swFinalQuoteVO2.setQuoGeneratedDate(rs.getString("GENERATED_DATE"));
									//	System.out.println("generated date--->"+rs.getString("GENERATED_DATE"));
										
										//to finalize the quotation
										swFinalQuoteVO2.setFinalPolcopyseqid(rs.getLong("POL_CPY_SEQ_ID"));
										swFinalQuoteVO2.setFinalQuotationNo(rs.getString("QUOT_NO"));
										swFinalQuoteVO2.setFinalQuotationdocs(bytes);
										
										blob1	=	rs.getBlob("FINL_POL_COPY_REF_DOC") ;
										if(blob1!= null){
											iStream1	=	blob1.getBinaryStream();
											swFinalQuoteVO2.setInputstreamdoc1(iStream1);
										}
										swFinalQuoteVO2.setAttachmentname1(rs.getString("FNL_POL_CPY_REF_DOC_NAME"));
									
										alResultList.add(swFinalQuoteVO2);
										}//end of if(strIdentifier.equalsIgnoreCase("Administration"))
									
									}// End of   while (rs.next())
						
							
							return (ArrayList)alResultList;
						}//end of try
						catch (SQLException sqlExp)
						{
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						catch (Exception exp)
						{
							throw new TTKException(exp, "insurance");
						}//end of catch (Exception exp)
						finally
						{
							/* Nested Try Catch to ensure resource closure */ 
							try // First try closing the result set
							{
								try
								{
									if (rs != null) rs.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
								finally // Even if result set is not closed, control reaches here. Try closing the statement now.
								{
									try
									{
										if (cStmtObject != null)	cStmtObject.close();
									}//end of try
									catch (SQLException sqlExp)
									{
										log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
										throw new TTKException(sqlExp, "insurance");
									}//end of catch (SQLException sqlExp)
									finally // Even if statement is not closed, control reaches here. Try closing the connection now.
									{
										try
										{
											if(conn != null) {
					        					conn.close();
					                					}
										}//end of try
										catch (SQLException sqlExp)
										{
											log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
											throw new TTKException(sqlExp, "insurance");
										}//end of catch (SQLException sqlExp)
									}//end of finally Connection Close
								}//end of finally Statement Close
							}//end of try
							catch (TTKException exp)
							{
								throw new TTKException(exp, "insurance");
							}//end of catch (TTKException exp)
							finally // Control will reach here in anycase set null to the objects 
							{
								rs = null;
								cStmtObject = null;
								conn = null;
							}//end of finally
						}//end of finally
					}
				 public ArrayList getViewUploadDocs(long policycopy_seq_id)throws TTKException{
						
						Connection conn = null;
						CallableStatement cStmtObject=null;
						ResultSet rs = null;
						Blob blob	=	null;
						ArrayList arraylist = new ArrayList();
						InputStream iStream	=	new ByteArrayInputStream(new String("").getBytes());
						try{
							conn = ResourceManager.getConnection();
					                 
					         cStmtObject = conn.prepareCall(strGetUploadedFile);
					       
					        cStmtObject.setLong(1, policycopy_seq_id);
					        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
							cStmtObject.execute();
							rs = (java.sql.ResultSet)cStmtObject.getObject(2);
							if(rs != null)
							{
								
								if(rs.next()) {
									
									blob	=	rs.getBlob("POL_COPY_DOC") ;
									if(blob!= null){
										iStream	=	blob.getBinaryStream();
									}//end of if(strIdentifier.equalsIgnoreCase("Administration"))
								
								}// End of   while (rs.next())
							
							}//end try
									
						arraylist.add(iStream);//taking for pdf data
								
					    }//end of try
					    catch (SQLException sqlExp) 
					    {
					        throw new TTKException(sqlExp, "preauth");
					    }//end of catch (SQLException sqlExp)
					    catch (Exception exp) 
					    {
					        throw new TTKException(exp, "preauth");
					    }//end of catch (Exception exp) 
					    finally
						{
					    	/* Nested Try Catch to ensure resource closure */ 
					    	try // First try closing the Statement
					    	{
					    		try
					    		{	if (rs != null) rs.close();
					    			if (cStmtObject != null) cStmtObject.close();
					    		}//end of try
					    		catch (SQLException sqlExp)
					    		{
					    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderDocs()",sqlExp);
					    			throw new TTKException(sqlExp, "preauth");
					    		}//end of catch (SQLException sqlExp)
					    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					    		{
					    			try
					    			{
					    				if(conn != null) conn.close();
					    			}//end of try
					    			catch (SQLException sqlExp)
					    			{
					    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderDocs()",sqlExp);
					    				throw new TTKException(sqlExp, "policy");
					    			}//end of catch (SQLException sqlExp)
					    		}//end of finally Connection Close
					    	}//end of try
					    	catch (TTKException exp)
					    	{
					    		throw new TTKException(exp, "preauth");
					    	}//end of catch (TTKException exp)
					    	finally // Control will reach here in anycase set null to the objects 
					    	{
					    		rs = null;
					    		cStmtObject = null;
					    		conn = null;
					    	}//end of finally
						}//end of finally
					    return arraylist;
				 }
				 
				 
				 public String swIssuePolicy(SwFinalQuoteVO swFinalQuoteVO) throws TTKException
					{
						String alertResult="";
						Connection conn = null;
						CallableStatement cStmtObject=null;
						
						try
						{

							conn = ResourceManager.getConnection();
							cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strswissuePolicy);
							cStmtObject.setLong(1,swFinalQuoteVO.getLngGroupProfileSeqID());	//num

							if(swFinalQuoteVO.getEnrollbatchSeqid() != null){
								cStmtObject.setLong(2,swFinalQuoteVO.getEnrollbatchSeqid());//num
															}
							else{
								cStmtObject.setLong(2,0);
							}//end of else
							
							
							if(swFinalQuoteVO.getBatchNumber() != null){

								cStmtObject.setString(3,swFinalQuoteVO.getBatchNumber());//str
							}
							else{
								cStmtObject.setString(3,null);
							}//end of else
							
							
							if(swFinalQuoteVO.getPolicySeqid() != null){

								cStmtObject.setLong(4,swFinalQuoteVO.getPolicySeqid());//num
							}
							else{
								cStmtObject.setLong(4,0);
							}//end of else
							
							
							if(swFinalQuoteVO.getPolicyNumber() != null){
								cStmtObject.setString(5,swFinalQuoteVO.getPolicyNumber());//str
							}
							else{
								cStmtObject.setString(5,null);
							}//end of else				
						
						
							cStmtObject.setLong(6,swFinalQuoteVO.getAddedBy());		//num
							cStmtObject.registerOutParameter(7,Types.INTEGER);//num
							cStmtObject.registerOutParameter(8,Types.VARCHAR);//num// alert message 7 parameter get 0/1 based on alert message will appear
							
							cStmtObject.execute();	
							alertResult = cStmtObject.getString(8);
							
						}//end of try
						catch (SQLException sqlExp)
						{
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						catch (Exception exp)
						{
							throw new TTKException(exp, "insurance");
						}//end of catch (Exception exp)
						finally
						{
				        	/* Nested Try Catch to ensure resource closure */ 
				        	try // First try closing the Statement
				        	{
				        		try
				        		{
				        			if (cStmtObject != null) cStmtObject.close();
				        		}//end of try
				        		catch (SQLException sqlExp)
				        		{
				        			log.error("Error while closing the Statement in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
				        			throw new TTKException(sqlExp, "insurance");
				        		}//end of catch (SQLException sqlExp)
				        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				        		{
				        			try
				        			{
				        				if(conn != null)
				        					{
				        					conn.close();
				        					}
				        			}//end of try
				        			catch (SQLException sqlExp)
				        			{
				        				log.error("Error while closing the Connection in InsuranceDAOImpl deleteInsuranceCompany()",sqlExp);
				        				throw new TTKException(sqlExp, "insurance");
				        			}//end of catch (SQLException sqlExp)
				        		}//end of finally Connection Close
				        	}//end of try
				        	catch (TTKException exp)
				        	{
				        		throw new TTKException(exp, "insurance");
				        	}//end of catch (TTKException exp)
				        	finally // Control will reach here in anycase set null to the objects 
				        	{
				        		cStmtObject = null;
				        		conn = null;
				        	}//end of finally
						}//end of finally
						return alertResult;
					}//end of 

			
		
				public ArrayList getAlkootProducts() throws TTKException {
			    	Connection conn1 = null;
			    	ResultSet rs = null;
			    	PreparedStatement pStmt = null;
			        ArrayList<Object> alproductList = new ArrayList<Object>();
			        CacheObject cacheObject = null;
			        try{
			            conn1 = ResourceManager.getConnection();
			            pStmt=conn1.prepareStatement(strProductList);
			            rs = pStmt.executeQuery();
			            if(rs != null){
			                while(rs.next()){
			                    cacheObject = new CacheObject();
			                    cacheObject.setCacheId((rs.getString("PRODUCT_SEQ_ID")));
			                    cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("PRODUCT_NAME")));
			                    alproductList.add(cacheObject);
			                }//end of while(rs.next())
			            }//end of if(rs != null)
			            return alproductList;
			        }//end of try
			        catch (SQLException sqlExp)
			        {
			            throw new TTKException(sqlExp, "insurance");
			        }//end of catch (SQLException sqlExp)
			        catch (Exception exp)
			        {
			            throw new TTKException(exp, "insurance");
			        }//end of catch (Exception exp)
			        finally
					{
						/* Nested Try Catch to ensure resource closure */ 
						try // First try closing the result set
						{
							try
							{
								if (rs != null) rs.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Resultset in PolicyDAOImpl getProductList()",sqlExp);
								throw new TTKException(sqlExp, "enrollment");
							}//end of catch (SQLException sqlExp)
							finally // Even if result set is not closed, control reaches here. Try closing the statement now.
							{
								try
								{
									if (pStmt != null) pStmt.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Statement in PolicyDAOImpl getProductList()",sqlExp);
									throw new TTKException(sqlExp, "enrollment");
								}//end of catch (SQLException sqlExp)
								finally // Even if statement is not closed, control reaches here. Try closing the connection now.
								{
									try
									{
										if(conn1 != null) conn1.close();
									}//end of try
									catch (SQLException sqlExp)
									{
										log.error("Error while closing the Connection in PolicyDAOImpl getProductList()",sqlExp);
										throw new TTKException(sqlExp, "insurance");
									}//end of catch (SQLException sqlExp)
								}//end of finally Connection Close
							}//end of finally Statement Close
						}//end of try
						catch (TTKException exp)
						{
							throw new TTKException(exp, "insurance");
						}//end of catch (TTKException exp)
						finally // Control will reach here in anycase set null to the objects 
						{
							rs = null;
							pStmt = null;
							conn1 = null;
						}//end of finally
					}//end of finally
			    }//end of getProductList(long lngInsSeqID,String strPolicyType)

				public String PricingUploadExcel(ArrayList inputData)throws TTKException {
			        Connection con=null;        
			        CallableStatement statement=null;
			        CallableStatement statement2=null;
			        String status="";      
			        try{            
			        	
					    con = ResourceManager.getConnection();
			        	  
					      statement=con.prepareCall(strSavememberXml);  
			              
			               SAXReader saxReader=new SAXReader(); 
			               
			               Document document=saxReader.read((FileReader)inputData.get(1));
			               
			               String filedetailNode=document.selectSingleNode("batch").asXML();
			               StringBuilder builder;
			               builder=new StringBuilder();
			               builder.append(filedetailNode);
		              
			              String oneMemberEntry=builder.toString();   
			             // System.out.println("oneMemberEntry----->"+oneMemberEntry);
			              
			              
			              XMLType poXML = null;
			  			if(oneMemberEntry != null)
			  			{
			  				poXML = XMLType.createXML (((OracleConnection)((WrappedConnectionJDK6)con).getUnderlyingConnection()), oneMemberEntry);
			  			}//end of if(mouDocument != null)
			  			
			             
			              statement.setLong(1,(Long)inputData.get(3));
			              statement.setObject(2,poXML);
			              statement.setLong(3,(Long)inputData.get(4));  // added by govind
			              statement.setString(6,(String)inputData.get(5)); 
			              statement.registerOutParameter(4,Types.VARCHAR);//success/error message
			              statement.registerOutParameter(5,Types.INTEGER);
			 			  statement.execute();	
			 			 status= statement.getString(4);
			 			 //System.out.println("status-------"+status);
			 			 
			                }catch (TTKException sqlExp)
			        		{
			        			throw new TTKException(sqlExp, "cheque");
			        		}//end of catch (SQLException sqlExp)
			        		catch (Exception exp)
			        		{
			        			throw new TTKException(exp, "cheque");
			        		}//end of catch (Exception exp)
			        		finally
			        		{
			                	/* Nested Try Catch to ensure resource closure */
			                	try // First try closing the Statement
			                	{
			                		try
			                		{
			                			if (statement != null) statement.close();
			                			if (statement2 != null) statement2.close();
			                		
			                		}//end of try
			                		catch (SQLException sqlExp)
			                		{
			                			log.error("Error while closing the Connection in ChequeDAOImpl ChequeUploadExcel()",sqlExp);
			                			throw new TTKException(sqlExp, "cheque");
			                		}//end of catch (SQLException sqlExp)
			                		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			                		{
			                			try
			                			{
			                				if(con != null) con.close();
			                			}//end of try
			                			catch (SQLException sqlExp)
			                			{
			                				log.error("Error while closing the Connection in ChequeDAOImpl ChequeUploadExcel()",sqlExp);
			                				throw new TTKException(sqlExp, "cheque");
			                			}//end of catch (SQLException sqlExp)
			                		}//end of finally Connection Close
			                	}//end of try
			                	catch (TTKException exp)
			                	{
			                		throw new TTKException(exp, "cheque");
			                	}//end of catch (TTKException exp)
			                	finally // Control will reach here in anycase set null to the objects
			                	{
			                		statement = null;
			                		statement2 = null;
			                		con = null;
			                		
			                	}//end of finally
			        		}//end of finally
			        return status;
			        	}

				public Object[] getBenefitvalueAfterExl(Long lpricingSeqId) throws TTKException {
			    	
					HashMap<String, ArrayList<AgeMasterVO>> tablesDataHM=new HashMap<>();
					HashMap<String, String> tablesNamesHM=new HashMap<>();
			    	ArrayList<AgeMasterVO> table1=new ArrayList<>();
			    	ArrayList<AgeMasterVO> table2=new ArrayList<>();
			    	ArrayList<AgeMasterVO> table3=new ArrayList<>();
			    	ArrayList<AgeMasterVO> table4=new ArrayList<>();
			    	Object[] tableObjects=new Object[2];
			    	
			    	
			    	int table1totalLives=0;
			    	int table2totalLives=0;
			    	int table3totalLives=0;
			    	int table4totalLives=0;
			    	
			    	String table1HeaderName=null;
			    	String table2HeaderName=null;
			    	String table3HeaderName=null;
			    	String table4HeaderName=null;
			    	
			    	Connection conn = null;
			    	CallableStatement cStmtObject=null;
			        ResultSet rs1 = null;
			        ResultSet rs2 = null;
			        ResultSet rs3 = null;
			        ResultSet rs4 = null;
			        try{
			        	conn = ResourceManager.getConnection();
						cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strswProfileIncomeListValue);
						cStmtObject.setLong(1,lpricingSeqId);
						cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
						cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
						cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
						cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
						cStmtObject.execute();
						rs1 = (java.sql.ResultSet)cStmtObject.getObject(2);
						rs2 = (java.sql.ResultSet)cStmtObject.getObject(3);
						rs3 = (java.sql.ResultSet)cStmtObject.getObject(4);
						rs4 = (java.sql.ResultSet)cStmtObject.getObject(5);
						 
				            if(rs1 != null){
				            	AgeMasterVO ageMasterVO=null;
				            	while (rs1.next()) {	
				            		ageMasterVO = new AgeMasterVO();		                    
				            		ageMasterVO.setColumn1(rs1.getString("inp2_seq_id"));
				            		ageMasterVO.setColumn2(rs1.getString("col1"));
				            		ageMasterVO.setColumn3(rs1.getString("col2"));
				            		ageMasterVO.setColumn4(rs1.getString("val"));
				            		ageMasterVO.setOvrlPofloDist(rs1.getString("OVRL_PORTPOL_DISTRIB"));
				            		table1totalLives+=rs1.getInt("val");
				            		
				                	table1.add(ageMasterVO);
				                	
				                	if(table1HeaderName==null)table1HeaderName=rs1.getString("table_name");
				                }
				            }
				           
				            
				            if(rs2 != null){
				            	AgeMasterVO ageMasterVO=null;
				            	while (rs2.next()) {
				            		tablesNamesHM.put(rs2.getString("VID"), rs2.getString("table_name"));
				            		
				            		ageMasterVO = new AgeMasterVO();		                    
				            		ageMasterVO.setColumn1(rs2.getString("inp2_seq_id"));
				            		ageMasterVO.setColumn2(rs2.getString("col1"));
				            		ageMasterVO.setColumn3(rs2.getString("col2"));
				            		ageMasterVO.setColumn4(rs2.getString("val"));
				            		ageMasterVO.setOvrlPofloDist(rs2.getString("OVRL_PORTPOL_DISTRIB"));
				            		table2totalLives+=rs2.getInt("val");
				                	table2.add(ageMasterVO);
				                	if(table2HeaderName==null)table2HeaderName=rs2.getString("table_name");
				                }
				            }
				            
				            if(rs3 != null){
				            	AgeMasterVO ageMasterVO=null;
				            	while (rs3.next()) {
				            		
				            		
				            		ageMasterVO = new AgeMasterVO();		                    
				            		ageMasterVO.setColumn1(rs3.getString("inp2_seq_id"));
				            		ageMasterVO.setColumn5(rs3.getString("col2"));
				            		ageMasterVO.setColumn4(rs3.getString("val"));
				            		ageMasterVO.setOvrlPofloDist(rs3.getString("OVRL_PORTPOL_DISTRIB"));
				            		table3totalLives+=rs3.getInt("val");
				            		ageMasterVO.setIncmFlag(rs3.getString("INCME_GRP_FLG"));   
				                	table3.add(ageMasterVO);
				                	if(table3HeaderName==null)table3HeaderName=rs3.getString("table_name");
				                }
				            }
				            
				            
				            if(rs4 != null){
				            	AgeMasterVO ageMasterVO=null;
				            	while (rs4.next()) {
				            		
				            		
				            		ageMasterVO = new AgeMasterVO();		                    
				            		ageMasterVO.setColumn1(rs4.getString("inp2_seq_id"));
				            		
				            		//ageMasterVO.setColumn1(rs4.getString("pri_seq_id"));
				            		ageMasterVO.setColumn6(rs4.getString("col2"));
				            		ageMasterVO.setColumn4(rs4.getString("val"));
				            		table4totalLives+=rs4.getInt("val");
				            		ageMasterVO.setOvrlPofloDist(rs4.getString("OVRL_PORTPOL_DISTRIB"));
				                	table4.add(ageMasterVO);
				                	if(table4HeaderName==null)table4HeaderName=rs4.getString("table_name");
				                }
				            }
				            //table-1
				            tablesDataHM.put("table1Data", table1);
				            tablesNamesHM.put("table1HeaderName", table1HeaderName);
				            tablesNamesHM.put("table1totalLives", table1totalLives+"");
				            //table-2
				            tablesDataHM.put("table2Data", table2);
				            tablesNamesHM.put("table2HeaderName", table2HeaderName);
				             tablesNamesHM.put("table2totalLives", table2totalLives+"");
				             //table-3
				             tablesDataHM.put("table3Data", table3);
					         tablesNamesHM.put("table3HeaderName", table3HeaderName);
					         tablesNamesHM.put("table3totalLives", table3totalLives+"");
					         //table-4
					         tablesDataHM.put("table4Data", table4);
					         tablesNamesHM.put("table4HeaderName", table4HeaderName);
					         tablesNamesHM.put("table4totalLives", table4totalLives+"");
					         
				            tableObjects[0]=tablesNamesHM;
				            tableObjects[1]=tablesDataHM;
				            
				            
						return tableObjects;	
			    	}//end of try
					catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (Exception exp)
					finally
					{
						/* Nested Try Catch to ensure resource closure */ 
						try // First try closing the result set
						{
							try
							{
								if (rs1 != null) rs1.close();
								if (rs2 != null) rs2.close();
								if (rs3 != null) rs3.close();
								if (rs4 != null) rs4.close();
								
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Resultset in InsuranceDAOImpl getBenefitvalueAfter()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if result set is not closed, control reaches here. Try closing the statement now.
							{
								try
								{
									if (cStmtObject != null)	cStmtObject.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Statement in InsuranceDAOImpl getBenefitvalueAfter()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
								finally // Even if statement is not closed, control reaches here. Try closing the connection now.
								{
									try
									{
										if(conn != null) {
				        					conn.close();
				                					}
									}//end of try
									catch (SQLException sqlExp)
									{
										log.error("Error while closing the Connection in InsuranceDAOImpl getBenefitvalueAfter()",sqlExp);
										throw new TTKException(sqlExp, "insurance");
									}//end of catch (SQLException sqlExp)
								}//end of finally Connection Close
							}//end of finally Statement Close
						}//end of try
						catch (TTKException exp)
						{
							throw new TTKException(exp, "insurance");
						}//end of catch (TTKException exp)
						finally // Control will reach here in anycase set null to the objects 
						{
							
							rs4 = null;
							rs3 = null;
							rs2 = null;
							rs1 = null;
							cStmtObject = null;
							conn = null;
						}//end of finally
					}//end of finally
				}

				public InsPricingVO swFetchScreen1(InsPricingVO  insPricingVO) throws TTKException
				{
					Connection conn = null;
					//Long lpricingSeqId = 1l;
					CallableStatement cStmtObject=null;
					ResultSet rs = null;

					try
					{
						conn = ResourceManager.getConnection();
						cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strFetchDetails);
						
						if(insPricingVO.getPreviousPolicyNo()!=null)
						{
							cStmtObject.setString(1,insPricingVO.getPreviousPolicyNo());
						}if(insPricingVO.getRenewalYN()!=null)
						{
							cStmtObject.setString(2,insPricingVO.getRenewalYN());
						}
						
				
						cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
						cStmtObject.execute();
						rs = (java.sql.ResultSet)cStmtObject.getObject(3);
						if(rs!=null){
							while(rs.next()){
								
								/*insPricingVO.setRenewalYN(TTKCommon.checkNull(rs.getString("RENEWAL_YN")));	
								insPricingVO.setClientCode(TTKCommon.checkNull(rs.getString("CLIENT_CODE")));
								insPricingVO.setClientName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
								insPricingVO.setPreviousPolicyNo(TTKCommon.checkNull(rs.getString("PREV_POLICY_NO")));
								insPricingVO.setTotalCovedLives(TTKCommon.checkNull(rs.getString("TOT_COV_LIVES")));*/
														
								//insPricingVO.setTotalLivesMaternity(TTKCommon.checkNull(rs.getString("TOT_COV_LIVES_MATRNTY")));	
							//	insPricingVO.setTrendFactor(TTKCommon.checkNull(rs.getString("TRND_FACTOR_PERC")));
								
								/*System.out.println("MAT_COV_YN------"+rs.getString("MAT_COV_YN"));
								System.out.println("DNTL_COV_YN------"+rs.getString("DNTL_COV_YN"));*/

								insPricingVO.setMaternityYN(TTKCommon.checkNull(rs.getString("MAT_COV_YN")));						
								insPricingVO.setDentalYN(TTKCommon.checkNull(rs.getString("DNTL_COV_YN")));
								insPricingVO.setDentalLivesYN(TTKCommon.checkNull(rs.getString("DNTL_COV_ALL_LIVES_YN")));
								insPricingVO.setOpticalYN(TTKCommon.checkNull(rs.getString("OPTCL_COV_YN")));				
								insPricingVO.setOpticalLivesYN(TTKCommon.checkNull(rs.getString("OPTCL_COV_ALL_LIVES_YN")));
								insPricingVO.setPhysiocoverage(TTKCommon.checkNull(rs.getString("PSYTHRPY_COV_YN")));
								insPricingVO.setVitDcoverage(TTKCommon.checkNull(rs.getString("VITAMIN_D_COV_YN")));
								
								insPricingVO.setVaccImmuCoverage(TTKCommon.checkNull(rs.getString("VACC_IMMUN_COV_YN")));
								insPricingVO.setMatComplicationCoverage(TTKCommon.checkNull(rs.getString("MAT_COMPLCTION_COV_YN")));
								insPricingVO.setPsychiatrycoverage(TTKCommon.checkNull(rs.getString("PSYCHRTRY_COV_YN")));				
								insPricingVO.setDeviatedNasalSeptum(TTKCommon.checkNull(rs.getString("DEVTD_NASL_SEPTM_COV_YN")));
								insPricingVO.setObesityTreatment(TTKCommon.checkNull(rs.getString("OBSTY_TRTMNT_COV_YN")));
								insPricingVO.setAreaOfCoverList(TTKCommon.checkNull(rs.getString("AREA_TYPE_SEQ_ID")));
								
								insPricingVO.setNetworkList(TTKCommon.checkNull(rs.getString("NWK_TYPE_SEQ_ID")));
								insPricingVO.setMaxBenifitList(TTKCommon.checkNull(rs.getString("MAX_BENF_LIMIT_SEQ_ID")));
								insPricingVO.setMaxBeneLimitOth(TTKCommon.checkNull(rs.getString("MAX_BENF_OTH_LIMIT")));				
								insPricingVO.setDentalLimitList(TTKCommon.checkNull(rs.getString("DENTL_LIMIT_SEQ_ID")));
								
								insPricingVO.setDentalLimitOth(TTKCommon.checkNull(rs.getString("DENTL_OTH_LIMIT")));
								insPricingVO.setMaternityLimitList(TTKCommon.checkNull(rs.getString("MAT_LIMIT_SEQ_ID")));
								insPricingVO.setMaternityLimitOth(TTKCommon.checkNull(rs.getString("MAT_OTH_LIMIT")));
							
								insPricingVO.setOpticalLimitList(TTKCommon.checkNull(rs.getString("OPTCL_LIMIT_SEQ_ID")));
								insPricingVO.setOpticalLimitOth(TTKCommon.checkNull(rs.getString("OPTCL_OTH_LIMIT")));				
						
								insPricingVO.setOpCopayList(TTKCommon.checkNull(rs.getString("OPT_COPAY_TYPE_SEQ_ID")));
								insPricingVO.setOpCopayListDesc(TTKCommon.checkNull(rs.getString("OPT_COPAY_PERC")));
								insPricingVO.setDentalcopayList(TTKCommon.checkNull(rs.getString("DNT_COPAY_TYPE_SEQ_ID")));
								insPricingVO.setDentalcopayListDesc(TTKCommon.checkNull(rs.getString("DNT_COPAY_PERC")));				
								insPricingVO.setOpticalCopayList(TTKCommon.checkNull(rs.getString("OPTCL_COPAY_TYPE_SEQ_ID")));
								insPricingVO.setOpticalCopayListDesc(TTKCommon.checkNull(rs.getString("OPTCL_COPAY_PERC")));
								insPricingVO.setOpDeductableList(TTKCommon.checkNull(rs.getString("OPT_DEDUCT_TYPE_SEQ_ID")));
								insPricingVO.setOpDeductableListDesc(TTKCommon.checkNull(rs.getString("OPT_DEDUCTBLE")));
								/////////////////////// comments and document fetch removed///////////////////////////
					/*			insPricingVO.setAttachmentname1(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC1_NAME")));
								insPricingVO.setAttachmentname2(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC2_NAME")));
								insPricingVO.setAttachmentname3(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC3_NAME")));
								insPricingVO.setAttachmentname4(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC4_NAME")));
								insPricingVO.setAttachmentname5(TTKCommon.checkNull(rs.getString("INPT_SRC_DOC5_NAME")));*/
						//		insPricingVO.setCompleteSaveYN(TTKCommon.checkNull(rs.getString("COMP_SAVE_YN")));//complete save = Y ; else N
								//2nd phase
								insPricingVO.setPolicycategory(TTKCommon.checkNull(rs.getString("PREV_POL_PRODUCT_NAME")));
								insPricingVO.setInpatientBenefit(TTKCommon.checkNull(rs.getString("IP_COV_YN")));
								insPricingVO.setOutpatientBenefit(TTKCommon.checkNull(rs.getString("OP_COV_YN")));
								insPricingVO.setGastricBinding(TTKCommon.checkNull(rs.getString("GASTRC_COV_YN")));
								insPricingVO.setHealthScreen(TTKCommon.checkNull(rs.getString("HLTH_COV_YN")));
								insPricingVO.setOrthodonticsCopay(TTKCommon.checkNull(rs.getString("ORTHO_COPAY_TYPE_SEQ_ID")));
								insPricingVO.setOrthodonticsCopayDesc(TTKCommon.checkNull(rs.getString("ORTHO_COPAY_PERC")));
								insPricingVO.setOpdeductableserviceYN(TTKCommon.checkNull(rs.getString("OP_CPY_DEDCT_ALL_OP_YN")));
								insPricingVO.setOpCopaypharmacy(TTKCommon.checkNull(rs.getString("OP_COPAY_PHM_TYPE_SEQ_ID")));
								insPricingVO.setOpCopaypharmacyDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_PHM_PERC")));
								insPricingVO.setOpCopyconsultn(TTKCommon.checkNull(rs.getString("OP_COPAY_CON_TYPE_SEQ_ID")));
								insPricingVO.setOpCopyconsultnDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_CON_PERC")));
								insPricingVO.setOpInvestigation(TTKCommon.checkNull(rs.getString("OP_COPAY_INVST_TYPE_SEQ_ID")));
								insPricingVO.setOpInvestigationDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_INVST_PERC")));
								insPricingVO.setOpCopyothers(TTKCommon.checkNull(rs.getString("OP_COPAY_OTH_TYPE_SEQ_ID")));
								insPricingVO.setOpCopyothersDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_OTH_PERC")));
								insPricingVO.setAlAhlihospital(TTKCommon.checkNull(rs.getString("ALHALLI_COV_YN")));
								insPricingVO.setAlAhlihospOPservices(TTKCommon.checkNull(rs.getString("OP_CPY_ALHALLI_ALL_OP_YN")));
								insPricingVO.setOpCopyalahlihosp(TTKCommon.checkNull(rs.getString("OP_COPAY_ALH_TYPE_SEQ_ID")));
								insPricingVO.setOpCopyalahlihospDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_ALH_PERC")));
								insPricingVO.setOpPharmacyAlAhli(TTKCommon.checkNull(rs.getString("OP_COPAY_PHM_ALH_TYPE_SEQ_ID")));
								insPricingVO.setOpPharmacyAlAhliDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_PHM_ALH_PERC")));
								insPricingVO.setOpConsultAlAhli(TTKCommon.checkNull(rs.getString("OP_COPAY_CON_ALH_TYPE_SEQ_ID")));
								insPricingVO.setOpConsultAlAhliDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_CON_ALH_PERC")));
								insPricingVO.setOpInvestnAlAhli(TTKCommon.checkNull(rs.getString("OP_COPAY_INVST_ALH_TYPE_SEQ_ID")));
								insPricingVO.setOpInvestnAlAhliDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_INVST_ALH_PERC")));
								insPricingVO.setOpothersAlAhli(TTKCommon.checkNull(rs.getString("OP_COPAY_OTH_ALH_TYPE_SEQ_ID")));
								insPricingVO.setOpothersAlAhliDesc(TTKCommon.checkNull(rs.getString("OP_COPAY_OTH_ALH_PERC")));
								insPricingVO.setOpticalFrameLimitList(TTKCommon.checkNull(rs.getString("OPTCL_FRAMES_LIMIT_SEQ_ID")));
								insPricingVO.setNotifyerror(TTKCommon.checkNull(rs.getString("ALERT")));
								//insPricingVO.setAlertmsgscreen1(TTKCommon.checkNull(rs.getString("POL_CNT_MSG")));
								insPricingVO.setIpCopay(TTKCommon.checkNull(rs.getString("IP_COPAY_TYPE_SEQ_ID")));
								insPricingVO.setIpCopayPerc(TTKCommon.checkNull(rs.getString("IP_COPAY_PERC")));
								insPricingVO.setIpCopayAtAlAhli(TTKCommon.checkNull(rs.getString("IP_COPAY_ALH_TYPE_SEQ_ID")));
								insPricingVO.setIpCopayAtAlAhliPerc(TTKCommon.checkNull(rs.getString("IP_COPAY_ALH_PERC")));
								insPricingVO.setMaternityCopay(TTKCommon.checkNull(rs.getString("MAT_COPAY_TYPE_SEQ_ID")));
								insPricingVO.setMaternityCopayPerc(TTKCommon.checkNull(rs.getString("MAT_COPAY_PERC")));
								

							}//end of while(rs.next())
						}// End of  if(rs!=null)
						return insPricingVO;
					}//end of try
					catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (Exception exp)
					finally
					{
						/* Nested Try Catch to ensure resource closure */ 
						try // First try closing the result set
						{
							try
							{
								if (rs != null) {
									rs.close();
								
			                					
								}
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Resultset in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
								throw new TTKException(sqlExp, "insurance");
							}//end of catch (SQLException sqlExp)
							finally // Even if result set is not closed, control reaches here. Try closing the statement now.
							{
								try
								{
									if (cStmtObject != null)
									{
										cStmtObject.close();
									}
										
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Statement in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
									throw new TTKException(sqlExp, "insurance");
								}//end of catch (SQLException sqlExp)
								finally // Even if statement is not closed, control reaches here. Try closing the connection now.
								{
									try
									{
										if(conn != null) {
				        					conn.close();
				                					}
									}//end of try
									catch (SQLException sqlExp)
									{
										log.error("Error while closing the Connection in InsuranceDAOImpl getInsuranceCompanyDetail()",sqlExp);
										throw new TTKException(sqlExp, "insurance");
									}//end of catch (SQLException sqlExp)
								}//end of finally Connection Close
							}//end of finally Statement Close
						}//end of try
						catch (TTKException exp)
						{
							throw new TTKException(exp, "insurance");
						}//end of catch (TTKException exp)
						finally // Control will reach here in anycase set null to the objects 
						{
							rs = null;
							cStmtObject = null;
							conn = null;
						}//end of finally
					}//end of finally
				}

				public InsPricingVO getPolicyStatusInfo(ArrayList dataList) throws TTKException {
					InsPricingVO insPricingVO=new InsPricingVO();
					try(Connection conn = ResourceManager.getConnection();
					    PreparedStatement pStmt = conn.prepareStatement(strPolicyStatusInfo);
						CallableStatement cStmtObject = conn.prepareCall(strPolicyDateInfo)){
						if(dataList.get(0)!=null)
						pStmt.setString(1, (String) dataList.get(0));
						else
							pStmt.setString(1, null);
						System.out.println("Prev Policy Number::"+(String) dataList.get(0)+"----Client Name::"+(String) dataList.get(1));
						if(dataList.get(0)!=null)
						cStmtObject.setString(1, (String) dataList.get(0));
						else
							pStmt.setString(1, null);
						if(dataList.get(1)!=null)
						cStmtObject.setString(2, (String) dataList.get(1));
						else
							pStmt.setString(2, null);
						if(dataList.get(2)!=null)
						cStmtObject.setString(3, (String) dataList.get(2));
						else
							pStmt.setString(3, null);
						cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
						cStmtObject.execute();
					    try(ResultSet rs = pStmt.executeQuery()){
					    	if(rs != null){
				                while(rs.next()){
				                	insPricingVO.setPolicyNumberFlag(TTKCommon.checkNull(rs.getString("FLAG")));	
//				                	insPricingVO.setPolicycategory(TTKCommon.checkNull(rs.getString("POLICY_CATEGORY")));	
				                }}	
					    }
					    try(ResultSet rs1 = (java.sql.ResultSet)cStmtObject.getObject(4)){
					    	if(rs1 != null){
				                while(rs1.next()){
				                	//System.out.println("start date::"+rs1.getString("START_DATE")+"---"+"END_DATE::"+rs1.getString("END_DATE"));
				                	insPricingVO.setCoverStartDate(rs1.getString("START_DATE")!=null ? new Date(rs1.getTimestamp("START_DATE").getTime()):null);
									insPricingVO.setCoverEndDate(rs1.getString("END_DATE")!=null ? new Date(rs1.getTimestamp("END_DATE").getTime()):null);
									insPricingVO.setPolicycategory(TTKCommon.checkNull(rs1.getString("POLCIY_CATEGORY")));
									insPricingVO.setAlertmsgscreen1(TTKCommon.checkNull(rs1.getString("POL_CNT_MSG")));
									insPricingVO.setNumberOfLives(rs1.getDouble("V_TOTAL_LIVES"));
				            }}	
					    }
			            
			        }catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (Exception exp)
			        
					return insPricingVO;
				}

				public ArrayList getViewFiles(ArrayList inputData) throws TTKException{
					ArrayList<String> outputData=new ArrayList();
					try(Connection conn = ResourceManager.getConnection();
						PreparedStatement pStmt = conn.prepareStatement(strPolicyViewFile)){
						pStmt.setString(1, (String)inputData.get(0));
						try(ResultSet rs = pStmt.executeQuery()){
					    	if(rs != null){
				                while(rs.next()){
				                	outputData.add(TTKCommon.checkNull(rs.getString("FILE_NAME")));
				                }}	
					    }
					}catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (Exception exp)
					return outputData;
				}

				public ArrayList addUpdateReInsurance(ReInsuranceDetailVO insDetailVO) throws TTKException{
					int outputData=0;
					ArrayList outputList=new ArrayList<>();
					try(Connection connection = ResourceManager.getConnection();
						CallableStatement callableStatement = connection.prepareCall(strSaveReInsuranceData)){
						callableStatement.registerOutParameter(1, Types.INTEGER);
						if(insDetailVO.getReins_seq_id()!=null)
						callableStatement.setLong(1,insDetailVO.getReins_seq_id());
						else
							callableStatement.setLong(1,0);
						callableStatement.registerOutParameter(2, Types.INTEGER);
						if(insDetailVO.getAddr_seq_id()!=null)
						callableStatement.setLong(2,insDetailVO.getAddr_seq_id());
						else
							callableStatement.setLong(2,0);
						callableStatement.registerOutParameter(3, Types.INTEGER);
						if(insDetailVO.getReins_bank_seq_id()!=null)
						callableStatement.setLong(3,insDetailVO.getReins_bank_seq_id());
						else
							callableStatement.setLong(3,0);
						if(insDetailVO.getTpa_office_seq_id()!=null)
						callableStatement.setLong(4, insDetailVO.getTpa_office_seq_id());
						else
							callableStatement.setLong(4,0);
						callableStatement.setString(5, insDetailVO.getReinsurerId());
						callableStatement.setString(6, insDetailVO.getReinsurerName());
						callableStatement.setString(7, insDetailVO.getTreatyID());
						callableStatement.setString(8, insDetailVO.getReInsuranceStructureType());
						callableStatement.setString(9, insDetailVO.getTreatyType());
						callableStatement.setString(10, insDetailVO.getPricingTerms());
						callableStatement.setString(11, insDetailVO.getRetentionSharePerc());
						callableStatement.setString(12, insDetailVO.getReinsExpAllowancePerc());
						callableStatement.setString(13, insDetailVO.getCedantsExpAllowancePerc());
						callableStatement.setString(14, insDetailVO.getFrequencyORremittance());
						callableStatement.setString(15, insDetailVO.getUnexpiredPremReservBasis());
						callableStatement.setString(16, insDetailVO.getFreqOfGenOfBordereaux());
						callableStatement.setString(17, insDetailVO.getProfitShareBasis());
						callableStatement.setString(18, insDetailVO.getProfitSharePercentage());
						callableStatement.setString(19, insDetailVO.getProfitSharePremiumPerc());
						callableStatement.setString(20, insDetailVO.getProfitShareClaimsPerc());
						callableStatement.setString(21, insDetailVO.getProfitShareExpensesPerc());
						callableStatement.setString(22, insDetailVO.getReportingCurrency());
						callableStatement.setString(23, insDetailVO.getRemittingCurrency());
						if(insDetailVO.getEmpanelDate()!=null)
						callableStatement.setTimestamp(24, new Timestamp(insDetailVO.getEmpanelDate().getTime()));
						else
							callableStatement.setObject(24, null);
						callableStatement.setString(25, insDetailVO.getActiveYN());
						callableStatement.setString(26, insDetailVO.getInactivatedDate());
						callableStatement.setLong(27, insDetailVO.getAddedBy());
						callableStatement.setString(28, insDetailVO.getAddressVO().getAddress1());
						callableStatement.setString(29, insDetailVO.getAddressVO().getAddress2());
						callableStatement.setString(30, insDetailVO.getAddressVO().getAddress3());
						callableStatement.setString(31, insDetailVO.getAddressVO().getStateCode());
						callableStatement.setString(32, insDetailVO.getAddressVO().getCityCode());
						callableStatement.setString(33, insDetailVO.getAddressVO().getPinCode());
						callableStatement.setString(34, insDetailVO.getAddressVO().getCountryCode());
						callableStatement.setInt(35, insDetailVO.getAddressVO().getIsdCode());
						callableStatement.setInt(36, insDetailVO.getAddressVO().getStdCode());
						callableStatement.setString(37, insDetailVO.getAddressVO().getPhoneNo1());
						callableStatement.setString(38, insDetailVO.getAddressVO().getPhoneNo2());
						callableStatement.setString(39, insDetailVO.getBankName());
						callableStatement.setString(40, insDetailVO.getBankAccountNo());
						callableStatement.setString(41, insDetailVO.getAccountType());
						callableStatement.setString(42, insDetailVO.getBankPhone());
						callableStatement.setString(43, insDetailVO.getBankBranch());
						callableStatement.setString(44, insDetailVO.getIBANSwiftCode());
						callableStatement.setString(45, insDetailVO.getBankAddress1());
						callableStatement.setString(46, insDetailVO.getBankAddress2());
						callableStatement.setString(47, insDetailVO.getBankAddress3());
						callableStatement.setString(48, insDetailVO.getReinsuranceSharePerc());
						callableStatement.setString(49, insDetailVO.getSpretentionSharePerc());
						callableStatement.setString(50, insDetailVO.getSpreinsuranceSharePerc());
						callableStatement.registerOutParameter(51,OracleTypes.INTEGER);
						callableStatement.execute();
						outputList.add(callableStatement.getInt(1));
						outputList.add(callableStatement.getInt(51));
						}catch (SQLException sqlExp)
						{
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
						catch (Exception exp)
						{
							throw new TTKException(exp, "insurance");
						}//end of catch (Exception exp)
					return outputList;
				}

				public ArrayList getReInsuranceCompanyList(ArrayList searchData) throws TTKException{
					ArrayList outputData=new ArrayList();
					ReInsuranceDetailVO  reInsuranceDetailVO = null;
					try(Connection connection = ResourceManager.getConnection();
							CallableStatement callableStatement = connection.prepareCall(strSearchReInsuranceData)){
						callableStatement.setString(1, (String)searchData.get(0));
						callableStatement.setString(2, (String) searchData.get(1));
						callableStatement.setString(3, (String)searchData.get(2));
						callableStatement.registerOutParameter(4,OracleTypes.CURSOR);
						callableStatement.execute();
						try(ResultSet resultSet = (java.sql.ResultSet)callableStatement.getObject(4)){
				                while(resultSet.next()){
				                	reInsuranceDetailVO = new ReInsuranceDetailVO();
				                    if(resultSet.getString("REINS_SEQ_ID") != null){
				                    	reInsuranceDetailVO.setReins_seq_id((new Long(resultSet.getLong("REINS_SEQ_ID"))));
				                    }
				                    reInsuranceDetailVO.setReinsurerName(TTKCommon.checkNull(resultSet.getString("REINSURER_NAME")));

				                    if(resultSet.getDate("EMPANELED_DATE") != null){
				                    	reInsuranceDetailVO.setEmpanelDate(resultSet.getDate("EMPANELED_DATE"));
				                    }//end of if(rs.getDate("EMPANELED_DATE") != null)
				                    
				                    if(resultSet.getString("REINSURER_ID") != null){
				                    	reInsuranceDetailVO.setReinsurerId(resultSet.getString("REINSURER_ID"));
				                    }//end of if(rs.getDate("EMPANELED_DATE") != null)

				                    reInsuranceDetailVO.setCompanyStatus(TTKCommon.checkNull(resultSet.getString("ACTIVE_YN")));
				                    reInsuranceDetailVO.setActiveYN(TTKCommon.checkNull(resultSet.getString("ACTIVE_YN")).equalsIgnoreCase("Y")?"Active":"Inactive");
				                    outputData.add(reInsuranceDetailVO);
				                }
					    }
					}catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "insurance");
					}//end of catch (Exception exp)
					return outputData;
				}

				public ReInsuranceDetailVO getReInsuranceCompanyDetail(ArrayList inputData) throws TTKException {
					ReInsuranceDetailVO reInsuranceDetailVO=new ReInsuranceDetailVO();
					try(Connection connection = ResourceManager.getConnection();
						CallableStatement callableStatement = connection.prepareCall(strGetReInsuranceData)){
						callableStatement.setLong(1, Long.parseLong(String.valueOf(inputData.get(0))));
						callableStatement.registerOutParameter(2,OracleTypes.CURSOR);
						callableStatement.execute();
						try(ResultSet resultSet = (java.sql.ResultSet)callableStatement.getObject(2)){
							while(resultSet.next()){
								reInsuranceDetailVO.setReins_seq_id(resultSet.getLong("REINS_SEQ_ID"));
								reInsuranceDetailVO.setAddr_seq_id(resultSet.getLong("ADDR_SEQ_ID"));
								reInsuranceDetailVO.setReins_bank_seq_id(resultSet.getLong("REINS_BANK_SEQ_ID"));
								reInsuranceDetailVO.setCedantsExpAllowancePerc(TTKCommon.checkNull(resultSet.getString("CEDANTS_EXP_ALLOWANCE")));
								reInsuranceDetailVO.setTpa_office_seq_id(resultSet.getLong("TPA_OFFICE_SEQ_ID"));
								reInsuranceDetailVO.setReinsurerId(TTKCommon.checkNull(resultSet.getString("REINSURER_ID")));
								reInsuranceDetailVO.setReinsurerName(TTKCommon.checkNull(resultSet.getString("REINSURER_NAME")));
								reInsuranceDetailVO.setTreatyID(TTKCommon.checkNull(resultSet.getString("TREATY_ID")));
								reInsuranceDetailVO.setReInsuranceStructureType(TTKCommon.checkNull(resultSet.getString("REINS_STRCTR_GEN_TYPE_ID")));
								reInsuranceDetailVO.setTreatyType(TTKCommon.checkNull(resultSet.getString("TREATY_GEN_TYPE_ID")));
								reInsuranceDetailVO.setPricingTerms(TTKCommon.checkNull(resultSet.getString("PRICING_TERM_GEN_TYPE_ID")));
								reInsuranceDetailVO.setRetentionSharePerc(TTKCommon.checkNull(resultSet.getString("RETENTION_SHARE")));
								reInsuranceDetailVO.setReinsExpAllowancePerc(TTKCommon.checkNull(resultSet.getString("REINS_EXPENSE_ALLOWANCE")));
								reInsuranceDetailVO.setFrequencyORremittance(TTKCommon.checkNull(resultSet.getString("FREQUENCY_OF_REMITTANCE")));
								reInsuranceDetailVO.setUnexpiredPremReservBasis(TTKCommon.checkNull(resultSet.getString("UNEXPIR_PREM_RESER_BASIS")));
								reInsuranceDetailVO.setFreqOfGenOfBordereaux(TTKCommon.checkNull(resultSet.getString("FREQ_OF_GEN_OF_BORDERAUX")));
								reInsuranceDetailVO.setProfitShareBasis(TTKCommon.checkNull(resultSet.getString("PROFIT_SHARE_BASIS")));
								reInsuranceDetailVO.setProfitSharePercentage(TTKCommon.checkNull(resultSet.getString("PROFIT_SHARE")));
								reInsuranceDetailVO.setProfitSharePremiumPerc(TTKCommon.checkNull(resultSet.getString("PROFIT_SHARE_PREMIUM")));
								reInsuranceDetailVO.setProfitShareClaimsPerc(TTKCommon.checkNull(resultSet.getString("PROFIT_SHARE_CLAIMS")));
								reInsuranceDetailVO.setProfitShareExpensesPerc(TTKCommon.checkNull(resultSet.getString("PROFIT_SHARE_EXPENSES")));
								reInsuranceDetailVO.setReportingCurrency(TTKCommon.checkNull(resultSet.getString("REPORTING_CURRENCY")));
								reInsuranceDetailVO.setRemittingCurrency(TTKCommon.checkNull(resultSet.getString("REMITTING_CURRENCY")));
								reInsuranceDetailVO.setEmpanelDate(resultSet.getDate("EMPANELED_DATE"));
								reInsuranceDetailVO.setActiveYN(TTKCommon.checkNull(resultSet.getString("ACTIVE_YN")));
								reInsuranceDetailVO.setInactivatedDate(TTKCommon.checkNull(resultSet.getString("INACTIVATED_DATE")));
								AddressVO addressVO=new AddressVO();
								addressVO.setAddress1(TTKCommon.checkNull(resultSet.getString("ADDRESS_1")));
								addressVO.setAddress2(TTKCommon.checkNull(resultSet.getString("ADDRESS_2")));
								addressVO.setAddress3(TTKCommon.checkNull(resultSet.getString("ADDRESS_3")));
								addressVO.setCityCode(TTKCommon.checkNull(resultSet.getString("CITY_TYPE_ID")));
								addressVO.setStateCode(TTKCommon.checkNull(resultSet.getString("STATE_TYPE_ID")));
								addressVO.setCountryCode(TTKCommon.checkNull(resultSet.getString("COUNTRY_ID")));
								addressVO.setPinCode(TTKCommon.checkNull(resultSet.getString("PIN_CODE")));
								addressVO.setStdCode(resultSet.getInt("STD_CODE"));
								addressVO.setIsdCode(resultSet.getInt("ISD_CODE"));
								addressVO.setPhoneNo1(TTKCommon.checkNull(resultSet.getString("PHONE1")));
								addressVO.setPhoneNo2(TTKCommon.checkNull(resultSet.getString("PHONE2")));
								reInsuranceDetailVO.setAddressVO(addressVO);
								reInsuranceDetailVO.setBankName(TTKCommon.checkNull(resultSet.getString("BANK_NAME")));
								reInsuranceDetailVO.setBankAccountNo(TTKCommon.checkNull(resultSet.getString("BANK_ACCOUNT_NO")));
								reInsuranceDetailVO.setBankPhone(TTKCommon.checkNull(resultSet.getString("BANK_PHONE_NO")));
								reInsuranceDetailVO.setBankBranch(TTKCommon.checkNull(resultSet.getString("BANK_BRANCH")));
								reInsuranceDetailVO.setIBANSwiftCode(TTKCommon.checkNull(resultSet.getString("IBAN_OR_SWIFT_CODE")));
								reInsuranceDetailVO.setBankAddress1(TTKCommon.checkNull(resultSet.getString("BANK_ADDRESS1")));
								reInsuranceDetailVO.setBankAddress2(TTKCommon.checkNull(resultSet.getString("BANK_ADDRESS2")));
								reInsuranceDetailVO.setBankAddress3(TTKCommon.checkNull(resultSet.getString("BANK_ADDRESS3")));
								reInsuranceDetailVO.setAccountType(TTKCommon.checkNull(resultSet.getString("ACCOUNT_TYPE")));
								reInsuranceDetailVO.setReinsuranceSharePerc(TTKCommon.checkNull(resultSet.getString("reinsurer_share")));
								reInsuranceDetailVO.setSpretentionSharePerc(TTKCommon.checkNull(resultSet.getString("sp_retention_share")));
								reInsuranceDetailVO.setSpreinsuranceSharePerc(TTKCommon.checkNull(resultSet.getString("sp_reinsurer_share")));
							}
						}
						
					}catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "insurance");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "insurance");
					}
					return reInsuranceDetailVO;
				}
}// end of InsuranceDAOImpl
