/**
 * @ (#)  TariffPlanDAOImpl.java Oct 3, 2005
 * Project      : TTKPROJECT
 * File         : TariffPlanDAOImpl.java
 * Author       : Suresh.M
 * Company      : Span Systems Corporation
 * Date Created : Oct 3, 2005
 *
 * @author       :  Suresh.M
 * Modified by   :  Ramakrishna K M
 * Modified date :  Oct 10, 2005
 * Reason        :  Adding getPlanDetailList() and getPlanHistoryList()
 *
 * Modified by   :  Nagaraj D V
 * Modified date :  Nov 05, 2005
 * Reason        :  To handle the where clause in administration and empanelment flow
 */

package com.ttk.dao.impl.administration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ocsp.Request;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;
import org.w3c.dom.Document;

import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.administration.PlanPackageVO;
import com.ttk.dto.administration.RateVO;
import com.ttk.dto.administration.RevisionPlanVO;
import com.ttk.dto.administration.TariffItemVO;
import com.ttk.dto.administration.TariffPlanVO;
import com.ttk.dto.claims.ClaimUploadErrorLogVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.DocumentDetailVO;
import com.ttk.dto.empanelment.HospitalDetailVO;
import com.ttk.dto.empanelment.InsuranceVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.administration.TariffUploadVO;


public class TariffPlanDAOImpl implements BaseDAO,Serializable{
	
	private static Logger log = Logger.getLogger(TariffPlanDAOImpl.class);
	
	private static final String strTariffPlanList = "SELECT * FROM(SELECT PLAN_SEQ_ID,PLAN_NAME,PLAN_DESCRIPTION,DEFAULT_PLAN_YN,dense_RANK() OVER (ORDER BY #, ROWNUM) Q FROM  TPA_HOSP_PLAN_CODE";//WHERE DEFAULT_PLAN_YN IN ('Y', 'N') in administration flow, WHERE DEFAULT_PLAN_YN IN ('N') in hospital flow
    private static final String strAddUpdateTariffPlanInfo = "{CALL HOSPITAL_ADMIN_TARIFF_PKG.PR_HOSPITAL_PLAN_SAVE(?,?,?,?,?)}";
    private static final String strDeleteTariffPlanInfo = "{CALL HOSPITAL_ADMIN_TARIFF_PKG.PR_HOSPITAL_PLAN_DELETE(?,?)}";
    private static final String strAddUpdateRatesInfo = "{CALL HOSPITAL_ADMIN_TARIFF_PKG.PR_MANAGE_RATES(";
    private static final String strCheckPackageInfo = "{CALL HOSPITAL_ADMIN_TARIFF_PKG.PR_MANAGE_RATES_CHECK_PKG(?,?,?,?)}";
    private static final String strTariffPkgNonPkgList = "{CALL HOSPITAL_ADMIN_TARIFF_PKG.PR_SELECT_TARIFF_ITEM(?,?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String strRatesDetailInfo = "{CALL HOSPITAL_ADMIN_TARIFF_PKG.PR_SELECT_PKG_RATES(?,?,?,?,?)}";
    private static final String strCheckAvailability = "{CALL HOSPITAL_ADMIN_TARIFF_PKG.PR_CHECK_PKG_AVAILABILITY(?,?,?,?,?)}";
    private static final String strRevisedPlanList = "SELECT * FROM (SELECT A.*,dense_RANK() OVER (ORDER BY #, ROWNUM) Q FROM (SELECT from_period.revised_plan_seq_id,'Revision ' || revision_number Revision_Number,revision_number as revision_no, tpa_hosp_plan_code.Plan_Name,tpa_hosp_plan_code.PLAN_SEQ_ID, tpa_hosp_plan_code.DEFAULT_PLAN_YN, from_period.plan_from_date from_date,Lead ( from_period.plan_from_date, 1 ) OVER ( ORDER BY from_period.plan_from_date ) - 1 to_date FROM tpa_hosp_revised_plan from_period, tpa_hosp_plan_code WHERE from_period.plan_seq_id = tpa_hosp_plan_code.plan_seq_id AND from_period.prod_hosp_seq_id is null and from_period.plan_seq_id = ?)A ";
    private static final String strAddRevisedPlan = "{call HOSPITAL_ADMIN_TARIFF_PKG.PR_HOSPITAL_REVISE_PLAN_SAVE(?,?,?,?,?,?,?)}";
   // private static final String strTariffDetailList = "SELECT * FROM(SELECT B.*,DENSE_RANK() OVER (ORDER BY #, ROWNUM) Q FROM ( (SELECT TPA_INS_ASSOC_PROD_HOSP.PROD_HOSP_SEQ_ID,-1 ASSOCIATED_SEQ_ID,TPA_INS_ASSOC_PROD_HOSP.GENERAL_TYPE_ID GENERAL_TYPE_ID,'TTK-(' || TPA_GENERAL_CODE.DESCRIPTION || ')' ASSOCIATED_TO,'-' PRODUCT_POLICY_NO FROM TPA_INS_ASSOC_PROD_HOSP,TPA_GENERAL_CODE WHERE ( TPA_INS_ASSOC_PROD_HOSP.GENERAL_TYPE_ID = TPA_GENERAL_CODE.GENERAL_TYPE_ID ) AND ( TPA_GENERAL_CODE.HEADER_TYPE = 'NHCP' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID = ? ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID IS NULL ) UNION ALL SELECT TPA_INS_ASSOC_PROD_HOSP.PROD_HOSP_SEQ_ID,TPA_INS_INFO.INS_SEQ_ID ASSOCIATED_SEQ_ID,'' GENERAL_TYPE_ID,TPA_INS_INFO.INS_COMP_NAME ASSOCIATED_TO , TPA_INS_PRODUCT.PRODUCT_NAME FROM TPA_INS_ASSOC_PROD_HOSP,TPA_INS_INFO,TPA_INS_PRODUCT,TPA_INS_PROD_POLICY WHERE ( TPA_INS_PRODUCT.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID ) AND ( TPA_INS_PROD_POLICY.PRODUCT_SEQ_ID = TPA_INS_PRODUCT.PRODUCT_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID = TPA_INS_PROD_POLICY.PROD_POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.STATUS_GENERAL_TYPE_ID = 'ASL' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID = ?) UNION ALL SELECT TPA_INS_ASSOC_PROD_HOSP.PROD_HOSP_SEQ_ID,TPA_INS_INFO.INS_SEQ_ID ASSOCIATED_SEQ_ID,''GENERAL_TYPE_ID,TPA_INS_INFO.INS_COMP_NAME || '(' || TPA_INS_INFO.INS_COMP_CODE_NUMBER || ')', '-' PRODUCT_POLICY_NO FROM TPA_INS_ASSOC_PROD_HOSP,TPA_INS_INFO, TPA_ENR_POLICY,TPA_INS_PROD_POLICY WHERE ( TPA_ENR_POLICY.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID ) AND ( TPA_INS_PROD_POLICY.POLICY_SEQ_ID = TPA_ENR_POLICY.POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID = TPA_INS_PROD_POLICY.PROD_POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.STATUS_GENERAL_TYPE_ID = 'ASL' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID= ? ) ))B";
 //   private static final String strAssociatedInfo = "SELECT DISTINCT * FROM (SELECT -1 ASSOCIATED_SEQ_ID,'TTK' ASSOCIATED_TO FROM DUAL UNION ALL SELECT TPA_INS_INFO.INS_SEQ_ID ASSOCIATED_SEQ_ID,TPA_INS_INFO.INS_COMP_NAME ASSOCIATED_TO FROM TPA_INS_ASSOC_PROD_HOSP,TPA_INS_INFO,TPA_INS_PRODUCT,TPA_INS_PROD_POLICY WHERE ( TPA_INS_PRODUCT.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID ) AND ( TPA_INS_PROD_POLICY.PRODUCT_SEQ_ID = TPA_INS_PRODUCT.PRODUCT_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID = TPA_INS_PROD_POLICY.PROD_POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.STATUS_GENERAL_TYPE_ID = 'ASL' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID = ?) UNION ALL SELECT TPA_INS_INFO.INS_SEQ_ID ASSOCIATED_SEQ_ID, TPA_INS_INFO.INS_COMP_NAME || '(' || TPA_INS_INFO.INS_COMP_CODE_NUMBER || ')' FROM TPA_INS_ASSOC_PROD_HOSP, TPA_INS_INFO,TPA_ENR_POLICY, TPA_INS_PROD_POLICY WHERE ( TPA_ENR_POLICY.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID ) AND ( TPA_INS_PROD_POLICY.POLICY_SEQ_ID = TPA_ENR_POLICY.POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID = TPA_INS_PROD_POLICY.PROD_POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.STATUS_GENERAL_TYPE_ID = 'ASL' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID = ? ) ) ORDER BY ASSOCIATED_TO";
    private static final String strRevisionHistoryList = "SELECT * FROM (SELECT A.*,dense_RANK() OVER (ORDER BY # , ROWNUM) Q FROM (SELECT from_period.revised_plan_seq_id,'Revision ' || revision_number Revision_Number,revision_number as revision_no,tpa_hosp_plan_code.Plan_Name,tpa_hosp_plan_code.PLAN_SEQ_ID,from_period.plan_from_date from_date, from_period.DISCOUNT_OFFERED_TTK, Lead ( from_period.plan_from_date, 1 ) OVER ( ORDER BY from_period.plan_from_date ) - 1 to_date,from_period.PROD_HOSP_SEQ_ID FROM tpa_hosp_revised_plan from_period, tpa_hosp_plan_code    WHERE from_period.PROD_HOSP_SEQ_ID = ? AND   from_period.plan_seq_id = tpa_hosp_plan_code.plan_seq_id ) A ";
private static final String strTariffDetailList = "SELECT * FROM(SELECT B.*,DENSE_RANK() OVER (ORDER BY #, ROWNUM) Q FROM ( (SELECT TPA_INS_ASSOC_PROD_HOSP.PROD_HOSP_SEQ_ID,-1 ASSOCIATED_SEQ_ID,TPA_INS_ASSOC_PROD_HOSP.GENERAL_TYPE_ID GENERAL_TYPE_ID,'Vidal Health-(' || TPA_GENERAL_CODE.DESCRIPTION || ')' ASSOCIATED_TO,'-' PRODUCT_POLICY_NO FROM TPA_INS_ASSOC_PROD_HOSP,TPA_GENERAL_CODE WHERE ( TPA_INS_ASSOC_PROD_HOSP.GENERAL_TYPE_ID = TPA_GENERAL_CODE.GENERAL_TYPE_ID ) AND ( TPA_GENERAL_CODE.HEADER_TYPE = 'NHCP' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID = ? ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID IS NULL ) UNION ALL SELECT TPA_INS_ASSOC_PROD_HOSP.PROD_HOSP_SEQ_ID,TPA_INS_INFO.INS_SEQ_ID ASSOCIATED_SEQ_ID,'' GENERAL_TYPE_ID,TPA_INS_INFO.INS_COMP_NAME ASSOCIATED_TO , TPA_INS_PRODUCT.PRODUCT_NAME FROM TPA_INS_ASSOC_PROD_HOSP,TPA_INS_INFO,TPA_INS_PRODUCT,TPA_INS_PROD_POLICY WHERE ( TPA_INS_PRODUCT.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID ) AND ( TPA_INS_PROD_POLICY.PRODUCT_SEQ_ID = TPA_INS_PRODUCT.PRODUCT_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID = TPA_INS_PROD_POLICY.PROD_POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.STATUS_GENERAL_TYPE_ID = 'ASL' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID = ?) UNION ALL SELECT TPA_INS_ASSOC_PROD_HOSP.PROD_HOSP_SEQ_ID,TPA_INS_INFO.INS_SEQ_ID ASSOCIATED_SEQ_ID,''GENERAL_TYPE_ID,TPA_INS_INFO.INS_COMP_NAME || '(' || TPA_INS_INFO.INS_COMP_CODE_NUMBER || ')', '-' PRODUCT_POLICY_NO FROM TPA_INS_ASSOC_PROD_HOSP,TPA_INS_INFO, TPA_ENR_POLICY,TPA_INS_PROD_POLICY WHERE ( TPA_ENR_POLICY.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID ) AND ( TPA_INS_PROD_POLICY.POLICY_SEQ_ID = TPA_ENR_POLICY.POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID = TPA_INS_PROD_POLICY.PROD_POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.STATUS_GENERAL_TYPE_ID = 'ASL' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID= ? ) ))B";
private static final String strAssociatedInfo = "SELECT DISTINCT * FROM (SELECT -1 ASSOCIATED_SEQ_ID,'Vidal Health' ASSOCIATED_TO FROM DUAL UNION ALL SELECT TPA_INS_INFO.INS_SEQ_ID ASSOCIATED_SEQ_ID,TPA_INS_INFO.INS_COMP_NAME ASSOCIATED_TO FROM TPA_INS_ASSOC_PROD_HOSP,TPA_INS_INFO,TPA_INS_PRODUCT,TPA_INS_PROD_POLICY WHERE ( TPA_INS_PRODUCT.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID ) AND ( TPA_INS_PROD_POLICY.PRODUCT_SEQ_ID = TPA_INS_PRODUCT.PRODUCT_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID = TPA_INS_PROD_POLICY.PROD_POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.STATUS_GENERAL_TYPE_ID = 'ASL' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID = ?) UNION ALL SELECT TPA_INS_INFO.INS_SEQ_ID ASSOCIATED_SEQ_ID, TPA_INS_INFO.INS_COMP_NAME || '(' || TPA_INS_INFO.INS_COMP_CODE_NUMBER || ')' FROM TPA_INS_ASSOC_PROD_HOSP, TPA_INS_INFO,TPA_ENR_POLICY, TPA_INS_PROD_POLICY WHERE ( TPA_ENR_POLICY.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID ) AND ( TPA_INS_PROD_POLICY.POLICY_SEQ_ID = TPA_ENR_POLICY.POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.PROD_POLICY_SEQ_ID = TPA_INS_PROD_POLICY.PROD_POLICY_SEQ_ID ) AND ( TPA_INS_ASSOC_PROD_HOSP.STATUS_GENERAL_TYPE_ID = 'ASL' ) AND ( TPA_INS_ASSOC_PROD_HOSP.HOSP_SEQ_ID = ? ) ) ORDER BY ASSOCIATED_TO";
private static final String strUploadTariff	 = "{CALL TARIFF_BULK_UPLOAD.SAVE_TARIFF_XML(?,?,?,?,?,?,?,?,?,?)}";
private static final String strUploadPharmacyTariff	 = "{CALL HOSPITAL_EMPANEL_PKG.LOAD_HOSP_PHARMACY(?,?)}";
private static final String strgetTariffs	 = "{CALL hospital_empanel_pkg.select_tariff_list (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
private static final String strgetPharmacyTariffs	 = "{CALL hospital_empanel_pkg.get_pharma_tariff_list(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
private static final String strTariffDetails	 = "{CALL hospital_empanel_pkg.select_tariff_item  (?,?,?,?)}";
private static final String strUpdateTariffItems	 = "{CALL hospital_empanel_pkg.save_tariff_details  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
private static final String strUpdateBulkTariffItems = "{CALL hospital_empanel_pkg.modify_service_item (?,?,?,?,?)}";
//private static final String strUpdateDiscountServiceTypes = "{CALL hospital_empanel_pkg.modify_network_service_item  (?,?,?,?,?,?,?,?,?)}";
private static final String strUpdateDiscountServiceTypes = "{CALL HOSPITAL_EMPANEL_PKG.MODIFY_NETWORK_SERVICE_ITEM  (?,?,?,?,?,?,?)}";

private static final String strGetTariffUploadedData = "{CALL hospital_empanel_pkg.dwnld_tariff (?,?)}";
private static final String strGetPharmcyTariffUploadedData = "{CALL hospital_empanel_pkg.dwnld_pharma_tariff(?,?)}";
private static final String strGetPharmacyServiceNames = "{CALL HOSPITAL_EMPANEL_PKG.MODIFY_SERVICE_TYPE(?,?,?,?,?)}";
private static final String strActivityCodeList = "{CALL hospital_empanel_pkg.select_activity_list  (?,?,?,?,?,?)}";
private static final String strActivityCodeDetails="{CALL hospital_empanel_pkg.select_activity_data_details  (?,?)}";
private static final String strUploadBulkModify	 = "{CALL HOSPITAL_EMPANEL_PKG.UPDATE_BULK_TARIFF(?,?)}";
private static final String strTariffupload = "{CALL TARIFF_BULK_UPLOAD.UPLD_ACT_DETAILS(?,?,?,?,?)}";
private static final String strGetTariffUploadLogList = "{CALL TARIFF_BULK_UPLOAD.GET_ERROR_LOG_LIST(?,?,?,?,?,?,?,?,?,?,?,?)}";



	private static final int PLAN_SEQ_ID = 1;
    private static final int PLAN_NAME = 2;
    private static final int PLAN_DESCRIPTION = 3;
    private static final int USER_SEQ_ID  = 4;
    private static final int ROW_PROCESSED = 5;

    /**
     * This method returns the ArrayList, which contains the TariffPlanVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of TariffPlanVO object's which contains the tariff plan details
     * @exception throws TTKException
     */
    public ArrayList getTariffPlanList(ArrayList alSearchObjects) throws TTKException {
    	Connection conn = null;
    	PreparedStatement pStmt = null;
    	ResultSet rs = null;
        StringBuffer sbfDynamicQuery = new StringBuffer();
        String strStaticQuery = strTariffPlanList;
        Collection<Object> alResultList = new ArrayList<Object>();
        TariffPlanVO tariffPlanVO = null;
        if(alSearchObjects != null && alSearchObjects.size() > 0)
        {
            for(int i=0; i < alSearchObjects.size()-4; i++)
            {
                if(alSearchObjects != null && alSearchObjects.size() > 0)
                {
                    if(sbfDynamicQuery.toString().equals(""))
                    {
                        sbfDynamicQuery = sbfDynamicQuery.append(" WHERE "+((SearchCriteria)alSearchObjects.get(i)).getName()+" IN ("+((SearchCriteria)alSearchObjects.get(i)).getValue()+")");
                    }//end of if(sbfDynamicQuery.toString().equals(""))
                    else
                    {
                        sbfDynamicQuery = sbfDynamicQuery.append(" AND UPPER("+ ((SearchCriteria)alSearchObjects.get(i)).getName()+") LIKE UPPER('"+((SearchCriteria)alSearchObjects.get(i)).getValue()+"%')");
                    }//end of else
                }//end of  if(alSearchObjects != null && alSearchObjects.size() > 0)
            }//end of for()
        }//end of if(alSearchObjects != null && alSearchObjects.size() > 0)
        strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"#", (String)alSearchObjects.get(alSearchObjects.size()-4)+" "+(String)alSearchObjects.get(alSearchObjects.size()-3));
        sbfDynamicQuery = sbfDynamicQuery .append( " )A WHERE Q >= "+(String)alSearchObjects.get(alSearchObjects.size()-2)+ " AND Q <= "+(String)alSearchObjects.get(alSearchObjects.size()-1));  
        strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();
        try
        {
            conn = ResourceManager.getConnection();
            pStmt = conn.prepareStatement(strStaticQuery);
            rs = pStmt.executeQuery();
            if(rs != null){
                while (rs.next()) {
                    tariffPlanVO =new TariffPlanVO();
                    if(rs.getString("PLAN_SEQ_ID")!=null){
                    	tariffPlanVO.setTariffPlanID(new Long(rs.getLong("PLAN_SEQ_ID")));
                    }//end of if(rs.getString("PLAN_SEQ_ID")!=null)
                    tariffPlanVO.setTariffPlanName(TTKCommon.checkNull(rs.getString("PLAN_NAME")));
                    tariffPlanVO.setTariffPlanDesc(TTKCommon.checkNull(rs.getString("PLAN_DESCRIPTION")));
                    tariffPlanVO.setDefaultPlanYn(TTKCommon.checkNull(rs.getString("DEFAULT_PLAN_YN")));
                    if (TTKCommon.checkNull(rs.getString("DEFAULT_PLAN_YN")).equals("Y")){
                    	tariffPlanVO.setImageTitle("Edit Rates");
                    }//end of if (TTKCommon.checkNull(rs.getString("DEFAULT_PLAN_YN")).equals("Y"))
                    alResultList.add(tariffPlanVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList) alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getTariffPlanList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getTariffPlanList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getTariffPlanList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getTariffPlanList(ArrayList alSearchObjects)

    /**
     * This method adds/updates the details  TariffPlanVO which contains tariff plan details
     * @param tariffPlanVO the details of tariff plan which has to be added or updated
     * @return long value, Plan Seq Id
     * @exception throws TTKException
     */
    public long addUpdateTariffPlan(TariffPlanVO tariffPlanVO) throws TTKException
    {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	long lResult = 0;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAddUpdateTariffPlanInfo);
            if(tariffPlanVO.getTariffPlanID() == null){
            	cStmtObject.setLong(PLAN_SEQ_ID,0);
            }//end of if(tariffPlanVO.getTariffPlanID() == null)
            else{
            	cStmtObject.setLong(PLAN_SEQ_ID,tariffPlanVO.getTariffPlanID());
            }//end of else
                
            cStmtObject.setString(PLAN_NAME,tariffPlanVO.getTariffPlanName());
            cStmtObject.setString(PLAN_DESCRIPTION,tariffPlanVO.getTariffPlanDesc());
            cStmtObject.setLong(USER_SEQ_ID, tariffPlanVO.getUpdatedBy());
            cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);
            cStmtObject.registerOutParameter(PLAN_SEQ_ID,Types.BIGINT);
            cStmtObject.execute();
            lResult = cStmtObject.getLong(PLAN_SEQ_ID);
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
        			log.error("Error while closing the Statement in TariffPlanDAOImpl addUpdatePlan()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl addUpdatePlan()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return lResult;
    }//end of addUpdatePlan(TariffPlanVO tariffPlanVO)

    /**
     * This method delete's the tariff plan's records from the database.
     * @param alTariffPlanList ArrayList object which contains the tariff plans id's to be deleted
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int deleteTariffPlan(ArrayList alTariffPlanList) throws TTKException{
        int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try
        {
            if(alTariffPlanList != null && alTariffPlanList.size() > 0)
            {
                conn = ResourceManager.getConnection();
                cStmtObject = conn.prepareCall(strDeleteTariffPlanInfo);
                cStmtObject.setString(1, (String)alTariffPlanList.get(0));//string of sequence id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.registerOutParameter(2, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(2);
            }//end of if(alTariffPlanList != null && alTariffPlanList.size() > 0)
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
        			log.error("Error while closing the Statement in TariffPlanDAOImpl deleteTariffPlan()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl deleteTariffPlan()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//end of deleteTariffPlan(ArrayList alTariffPlanList)

    /**
     * This method adds/updates the RateVO which contains room rates details
     * @param rateVO the details which has to be added or updated
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int addUpdateRates(RateVO rateVO) throws TTKException {
        int iResult = 1;
        StringBuffer sbfSQL = null;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        Statement stmt = null;
        try{
            conn = ResourceManager.getConnection();
            conn.setAutoCommit(false);
            stmt = (java.sql.Statement)conn.createStatement();
            if(rateVO.getRates() != null){
                for(int i=0;i<rateVO.getPkgCostSeqIdList().length;i++){
                    sbfSQL = new StringBuffer();
                    sbfSQL = sbfSQL.append("'"+rateVO.getSelRevPlanSeqId()+"',");
                    sbfSQL = sbfSQL.append("'"+rateVO.getRevPlanSeqId()+"',");
                    sbfSQL = sbfSQL.append("'"+rateVO.getPkgCostSeqIdList()[i]+"',");
                    sbfSQL = sbfSQL.append("'"+rateVO.getPkgDetailSeqIdList()[i]+"',");

                    if(rateVO.getRates()[i].equals("")){
                    	sbfSQL = sbfSQL.append(""+null+",");
                    }//end of if(rateVO.getRates()[i].equals(""))
                    else{
                    	sbfSQL = sbfSQL.append(""+rateVO.getRates()[i]+",");
                    }//end of else

                    if(rateVO.getDiscountList() != null){
                    	if(rateVO.getDiscountList()[i].equals("")){
                        	sbfSQL = sbfSQL.append(""+null+",");//Discount On Services
                        }//end of if(rateVO.getDiscountList()[i].equals(""))
                        else{
                        	sbfSQL = sbfSQL.append(""+rateVO.getDiscountList()[i]+",");
                        }//end of else
                    }//end of if(rateVO.getDiscountList() != null)
                    else{
                    	sbfSQL = sbfSQL.append(""+null+",");//Discount On Services
                    }//end of else

                    sbfSQL = sbfSQL.append("'"+rateVO.getUpdatedBy()+"')}");
                    stmt.addBatch(strAddUpdateRatesInfo+sbfSQL.toString());
               }//end of for
            }//end of if(rateVO.getRates() != null)
            stmt.executeBatch();
            stmt.close();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCheckPackageInfo);
            if(rateVO.getSelRevPlanSeqId() != null){
            	cStmtObject.setLong(1,rateVO.getSelRevPlanSeqId());
            }//end of if(rateVO.getSelRevPlanSeqId() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else
                
            if(rateVO.getPkgSeqId() != null){
            	cStmtObject.setLong(2,rateVO.getPkgSeqId());
            }//end of if(rateVO.getPkgSeqId() != null)
            else{
            	cStmtObject.setLong(2,0);
            }//end of else
                
            cStmtObject.setString(3,rateVO.getAvblGnrlTypeId());
            cStmtObject.setLong(4,rateVO.getUpdatedBy());//USER_SEQ_ID
            cStmtObject.execute();
            cStmtObject.close();
            conn.commit();
        }//end of try
        catch (SQLException sqlExp)
        {
            try {
                conn.rollback();
                throw new TTKException(sqlExp, "tariffplan");
            } //end of try
            catch (SQLException sExp) {
                throw new TTKException(sExp, "tariffplan");
            }//end of catch (SQLException sExp)
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            try {
                conn.rollback();
                throw new TTKException(exp, "tariffplan");
            } //end of try
            catch (SQLException sqlExp) {
                throw new TTKException(sqlExp, "tariffplan");
            }//end of catch (SQLException sqlExp)
        }//end of catch (Exception exp)
        finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try{
        			if (stmt != null) stmt.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in TariffPlanDAOImpl addUpdateRates()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally{ // Even if statement is not closed, control reaches here. Try closing the Callabale Statement now.
        			try
            		{
            			if (cStmtObject != null) cStmtObject.close();
            		}//end of try
            		catch (SQLException sqlExp)
            		{
            			log.error("Error while closing the Statement in TariffPlanDAOImpl addUpdateRates()",sqlExp);
            			throw new TTKException(sqlExp, "tariffplan");
            		}//end of catch (SQLException sqlExp)
            		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
            		{
            			try
            			{
            				if(conn != null) conn.close();
            			}//end of try
            			catch (SQLException sqlExp)
            			{
            				log.error("Error while closing the Connection in TariffPlanDAOImpl addUpdateRates()",sqlExp);
            				throw new TTKException(sqlExp, "tariffplan");
            			}//end of catch (SQLException sqlExp)
            		}//end of finally Connection Close
        		}//end of finally
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		stmt = null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
   }//end of addUpdateRates(RateVO rateVO)

    /**
     * This method returns the ArrayList, which contains the RevisionPlanVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of RevisionPlanVO object's which contains the tariff revision plan details
     * @exception throws TTKException
     */
    public ArrayList getRevisionPlanList(ArrayList alSearchObjects) throws TTKException
    {
    	Connection conn = null;
    	PreparedStatement pStmt = null;
    	ResultSet rs = null;
        StringBuffer sbfDynamicQuery = new StringBuffer();
        String strStaticQuery = strRevisedPlanList;
        RevisionPlanVO revisionPlanVO = null;
        Collection<Object> alResultList = new ArrayList<Object>();
        if(alSearchObjects != null && alSearchObjects.size()-4 > 0)
        {
            String strPlanSeqId = ((SearchCriteria)alSearchObjects.get(0)).getValue();
            strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"?", strPlanSeqId);
            String strFromDate   = TTKCommon.checkNull(((SearchCriteria)alSearchObjects.get(1)).getValue());
            String strToDate = TTKCommon.checkNull(((SearchCriteria)alSearchObjects.get(2)).getValue());
            sbfDynamicQuery = sbfDynamicQuery.append("  WHERE ( ( from_date >= nvl(to_date( '"+strFromDate+"','dd/mm/yyyy'),to_date('1/1/2002','dd/mm/yyyy') ) AND ( NVL(TO_DATE,nvl2(to_date('"+strToDate+"','dd/mm/yyyy'),to_date('"+strToDate+"','dd/mm/yyyy')+1,from_date)) <= nvl(to_date('"+strToDate+"','dd/mm/yyyy'), NVL(TO_DATE,FROM_DATE)))))");
        }//end of  if(alSearchObjects != null && alSearchObjects.size() > 0)
        //build the Order By Clause
        strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"#", (String)alSearchObjects.get(alSearchObjects.size()-4)+" "+(String)alSearchObjects.get(alSearchObjects.size()-3));
        sbfDynamicQuery = sbfDynamicQuery .append( " )A WHERE Q >= "+(String)alSearchObjects.get(alSearchObjects.size()-2)+ " AND Q <= "+(String)alSearchObjects.get(alSearchObjects.size()-1));  
        strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();
        try
        {
            conn = ResourceManager.getConnection();
            pStmt = conn.prepareStatement(strStaticQuery);
            rs = pStmt.executeQuery();
            if (rs!=null)
            {
                while (rs.next()) {
                    revisionPlanVO = new RevisionPlanVO();
                    if(rs.getString("REVISED_PLAN_SEQ_ID")!=null){
                    	revisionPlanVO.setRevPlanSeqId(new Long(rs.getLong("REVISED_PLAN_SEQ_ID")));
                    }//end of if(rs.getString("REVISED_PLAN_SEQ_ID")!=null)s
                    revisionPlanVO.setRevisionName(TTKCommon.checkNull(rs.getString("REVISION_NUMBER")));
                    revisionPlanVO.setTariffPlanName(TTKCommon.checkNull(rs.getString("Plan_Name")));
                    if (rs.getString("PLAN_SEQ_ID") != null){
                    	revisionPlanVO.setTariffPlanID(new Long(rs.getLong("PLAN_SEQ_ID")));
                    }//end of if (rs.getString("PLAN_SEQ_ID") != null)
                    revisionPlanVO.setDefaultPlanYn(TTKCommon.checkNull(rs.getString("DEFAULT_PLAN_YN")));
                    if(rs.getTimestamp("FROM_DATE") != null){
                    	revisionPlanVO.setStartDate(new java.util.Date(rs.getTimestamp("FROM_DATE").getTime()));
                    }//end of if(rs.getTimestamp("FROM_DATE") != null)
                    if(rs.getTimestamp("TO_DATE") != null){
                    	revisionPlanVO.setEndDate(new java.util.Date(rs.getTimestamp("TO_DATE").getTime()));
                    }//end of if(rs.getTimestamp("TO_DATE") != null)
                    alResultList.add(revisionPlanVO);
                }// End of  while (rs.next()
            }// End of if (rs!=null)
            return (ArrayList)alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getRevisionPlanList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getRevisionPlanList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getRevisionPlanList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//End of getRevisionPlanList(ArrayList alSearchObjects, String sortColumnName, String sortOrder, String startRow, String endRow)

    /**
     * This method returns the ArrayList, which contains the PlanPackageVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of PlanPackageVO object's which contains the tariff package details
     * @exception throws TTKException
     */
    public ArrayList getPackageList(ArrayList alSearchObjects) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        Collection<Object> alRresultList = new ArrayList<Object>();
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTariffPkgNonPkgList);
            cStmtObject.setString(1,(String)alSearchObjects.get(0));
            cStmtObject.setLong(2, (Long)alSearchObjects.get(1));
            if(alSearchObjects.get(2) != null){
            	cStmtObject.setLong(3, (Long)alSearchObjects.get(2));
            }//end of if(alSearchObjects.get(2) != null)
            else{
            	cStmtObject.setLong(3,0);
            }//end of else
                
            cStmtObject.setLong(4, (Long)alSearchObjects.get(3));
            cStmtObject.setString(5,(String)alSearchObjects.get(4));
            cStmtObject.setString(6,(String)alSearchObjects.get(5));
            cStmtObject.setString(7,(String)alSearchObjects.get(6));
            cStmtObject.setString(8,(String)alSearchObjects.get(7));
            cStmtObject.setString(9,(String)alSearchObjects.get(8));
            cStmtObject.setString(10,(String)alSearchObjects.get(9));
            cStmtObject.setString(11,(String)alSearchObjects.get(10));
            cStmtObject.registerOutParameter(12,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(12);
            if(rs != null){
                PlanPackageVO planPackageVO = null;
                while (rs.next()) {
                    planPackageVO = new PlanPackageVO();
                    if(rs.getString("REVISED_PLAN_SEQ_ID") != null){
                    	planPackageVO.setRevPlanSeqId(new Long(rs.getLong("REVISED_PLAN_SEQ_ID")));
                    }//end of if(rs.getString("REVISED_PLAN_SEQ_ID") != null)
                        
                    if(rs.getString("PKG_SEQ_ID") != null){
                    	planPackageVO.setPkgSeqId(new Long(rs.getLong("PKG_SEQ_ID")));
                    }//end of if(rs.getString("PKG_SEQ_ID") != null)
                        
                    planPackageVO.setName(TTKCommon.checkNull(rs.getString("NAME")));
                    planPackageVO.setType(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
                    planPackageVO.setMedicalPkgYN(TTKCommon.checkNull(rs.getString("MEDICAL_PACKAGE_YN")));
                    if(TTKCommon.checkNull(rs.getString("STATUS")).equals("MOD")){
                        planPackageVO.setModified(true);
                        planPackageVO.setImageTitle("Modified");
                        planPackageVO.setImageName("ModifiedIcon");
                    }// End of if(TTKCommon.checkNull(rs.getString("STATUS")).equals("MOD"))
                    alRresultList.add(planPackageVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alRresultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getPackageList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getPackageList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getPackageList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getPackageList(ArrayList alSearchObjects, String sortColumnName, String sortOrder, String startRow, String endRow)

    /**
     * This method returns the ArrayList object, which contains all the details about the Room Rates
     * @param lRevisedPlanSeqId long Revised Plan Seq Id
     * @param lPkgSeqId long Package Seq Id
     * @param strGeneralTypeId String  General Type Id for Package/Non Package
     * @param strWardTypeId String  Ward Type Id for Ward Charges, for packages ward type id is null
     * @return ArrayList object which contains all the details about the Room Rates
     * @exception throws TTKException
     */
    public ArrayList getRates(long lRevisedPlanSeqId,long lPkgSeqId,String strGeneralTypeId,String strWardTypeId) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        RateVO rateVO = null;
        ArrayList<Object> alRatesList = null;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strRatesDetailInfo);
            cStmtObject.setLong(1, lPkgSeqId);
            cStmtObject.setString(2, TTKCommon.checkNull(strGeneralTypeId));
            cStmtObject.setLong(3, lRevisedPlanSeqId);
            cStmtObject.setString(4, TTKCommon.checkNull(strWardTypeId));
            cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(5);
            alRatesList= new ArrayList<Object>();
            if(rs != null){
                while(rs.next()){
                    rateVO = new RateVO();
                    rateVO.setSelRevPlanSeqId(lRevisedPlanSeqId);
                    rateVO.setPkgSeqId(lPkgSeqId);
                    rateVO.setGnrlTypeId(TTKCommon.checkNull(rs.getString("GENERAL_TYPE_ID")));
                    
                    if(rs.getString("REVISED_PLAN_SEQ_ID") != null){
                    	rateVO.setRevPlanSeqId(new Long(rs.getLong("REVISED_PLAN_SEQ_ID")));
                    }//end of if(rs.getString("REVISED_PLAN_SEQ_ID") != null)
                        
                    if(rs.getString("PKG_COST_SEQ_ID") != null){
                    	rateVO.setPkgCostSeqId(new Long(rs.getLong("PKG_COST_SEQ_ID")));
                    }//end of if(rs.getString("PKG_COST_SEQ_ID") != null)
                        
                    if(rs.getString("PKG_DETAIL_SEQ_ID") != null){
                    	rateVO.setPkgDetailSeqId(new Long(rs.getLong("PKG_DETAIL_SEQ_ID")));
                    }//end of if(rs.getString("PKG_DETAIL_SEQ_ID") != null)
                        
                    rateVO.setRoomDesc(TTKCommon.checkNull(rs.getString("ROOM_DESCRIPTION")));
                    if(rs.getString("COST") != null){
                    	rateVO.setRate(new BigDecimal(rs.getString("COST")));
                    }//end of if(rs.getString("COST") != null)
                        
                    if(rs.getString("DISCOUNT_ON_SERVICES") != null){
                    	rateVO.setDisctOnServices(new BigDecimal(rs.getString("DISCOUNT_ON_SERVICES")));
                    }//end of if(rs.getString("DISCOUNT_ON_SERVICES") != null)
                    else{
                    	rateVO.setDisctOnServices(new BigDecimal(0));
                    }//end of else
                        
                    alRatesList.add(rateVO);
                }//end of while(rs.next())
           }//end of if(rs != null)
            return alRatesList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getRates()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getRates()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getRates()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getRates(long lRevisedPlanSeqId,long lPkgSeqId,String strGeneralTypeId,String strWardTypeId)

    /**
     * This method will sets the package as available or not available in the Package records in the database
     * @param alPackageList ArrayList which contains the details of the packages, which are to be available or not available
     * @param strAvblGnrlTypeId String which contains the available general type id for available or notavailable the package
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int updateAvailabilityStatus(ArrayList alPackageList,String strAvblGnrlTypeId) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	int iResult = 0;
        try{
            if(alPackageList != null && alPackageList.size() > 0)
            {
                conn = ResourceManager.getConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCheckAvailability);
                cStmtObject.setLong(1,(Long)alPackageList.get(0));//REVISED_PLAN_SEQ_ID
                cStmtObject.setString(2, TTKCommon.checkNull((String)alPackageList.get(1)));//string of package sequence id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.setString(3,strAvblGnrlTypeId);//AVAILABLE_GENERAL_TYPE_ID
                cStmtObject.setLong(4, (Long)alPackageList.get(2));//user sequence id
                cStmtObject.registerOutParameter(5, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(5);
            }//end of if(alPackageList != null && alPackageList.size() > 0)
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "tariffplan");
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
        			log.error("Error while closing the Statement in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//end of updateAvailabilityStatus(ArrayList alPackageList,String strAvblGnrlTypeId)

    /**
     * This method returns the ArrayList, which contains the HospitalTariffPlanDetailVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of HospitalTariffPlanDetailVO object's which contains the hospital's tariff plan details
     * @exception throws TTKException
     */
    public ArrayList getHospitalTariffDetailList(ArrayList alSearchObjects) throws TTKException
    {
    	Connection conn = null;
    	PreparedStatement pStmt = null;
    	ResultSet rs = null;
        StringBuffer sbfDynamicQuery = new StringBuffer();
        String strStaticQuery = strTariffDetailList;
        Collection<Object> alResultList = new ArrayList<Object>();
        InsuranceVO  insuranceVO = null;
        if(alSearchObjects != null && alSearchObjects.size() > 0)
        {
            String strHospSeqId = ((SearchCriteria)alSearchObjects.get(0)).getValue();
            strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"?",strHospSeqId);
            for(int i=1; i < alSearchObjects.size()-4; i++)
            {
            	if(!((SearchCriteria)alSearchObjects.get(i)).getValue().equals(""))
                {
            		if(((SearchCriteria)alSearchObjects.get(i)).getOperator().equalsIgnoreCase("equals"))
                    {
                        if(sbfDynamicQuery.toString().equals("")){
                        	sbfDynamicQuery =sbfDynamicQuery.append( " WHERE "+((SearchCriteria)alSearchObjects.get(i)).getName()+" = '"+((SearchCriteria)alSearchObjects.get(i)).getValue()+"' ");
                        }//end of if
                        else{
                        	sbfDynamicQuery = sbfDynamicQuery.append(" AND "+((SearchCriteria)alSearchObjects.get(i)).getName()+" = '"+((SearchCriteria)alSearchObjects.get(i)).getValue()+"' ");
                        }//end of else
                    }// end of   if(((SearchCriteria)alSearchObjects.get(i)).getOperator().equalsIgnoreCase("equals"))
                    else
                    {
                        if(sbfDynamicQuery.toString().equals("")){
                        	sbfDynamicQuery = sbfDynamicQuery.append(" WHERE UPPER("+((SearchCriteria)alSearchObjects.get(i)).getName()+") LIKE UPPER('"+((SearchCriteria)alSearchObjects.get(i)).getValue()+"%')");
                        }//end of if
                        else{
                        	sbfDynamicQuery = sbfDynamicQuery.append(" AND UPPER("+ ((SearchCriteria)alSearchObjects.get(i)).getName()+") LIKE UPPER('"+((SearchCriteria)alSearchObjects.get(i)).getValue()+"%')");
                        }//end of else
                    }//end of else
            	}// end of  if(!((SearchCriteria)alSearchObjects.get(i)).getValue().equals(""))
            }//end of for()
        }//end of if(alSearchObjects != null && alSearchObjects.size() > 0)
        //build the Order By Clause
        strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"#", (String)alSearchObjects.get(alSearchObjects.size()-4)+" "+(String)alSearchObjects.get(alSearchObjects.size()-3));
        sbfDynamicQuery = sbfDynamicQuery .append( " )A WHERE Q >= "+(String)alSearchObjects.get(alSearchObjects.size()-2)+ " AND Q <= "+(String)alSearchObjects.get(alSearchObjects.size()-1));
        strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();
        try
        {
            conn = ResourceManager.getConnection();
            //  
            pStmt = conn.prepareStatement(strStaticQuery);
            rs = pStmt.executeQuery();
            if (rs!=null)
            {
                while (rs.next()) {
                    insuranceVO = new InsuranceVO();
                    if (rs.getString("PROD_HOSP_SEQ_ID")!=null){
                    	insuranceVO.setProdHospSeqId(new Long(rs.getLong("PROD_HOSP_SEQ_ID")));
                    }//end of if (rs.getString("PROD_HOSP_SEQ_ID")!=null)
                    if (rs.getString("ASSOCIATED_SEQ_ID")!=null){
                    	insuranceVO.setAssociatedSeqId(new Long(rs.getLong("ASSOCIATED_SEQ_ID")));
                    }//end of if (rs.getString("ASSOCIATED_SEQ_ID")!=null)
                    insuranceVO.setGenTypeID(TTKCommon.checkNull(rs.getString("GENERAL_TYPE_ID")));
                    insuranceVO.setCompanyName(TTKCommon.checkNull(rs.getString("ASSOCIATED_TO")));
                    insuranceVO.setProdPolicyNumber(TTKCommon.checkNull(rs.getString("PRODUCT_POLICY_NO")));
                    alResultList.add(insuranceVO);
                }//end of while
            }//End of  if (rs!=null)
            return  (ArrayList)alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getHospitalTariffDetailList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getHospitalTariffDetailList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getHospitalTariffDetailList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//End of getHospitalTariffDetailList(ArrayList alSearchObjects, String sortColumnName, String sortOrder, String startRow, String endRow)

    /**
     * This method returns the ArrayList, which contains the RevisionPlanVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of RevisionPlanVO object's which contains the tariff revision plan details
     * @exception throws TTKException
     */
    public ArrayList getHospitalRevisionPlanList(ArrayList alSearchObjects) throws TTKException{
    	Connection conn = null;
    	PreparedStatement pStmt = null;
    	ResultSet rs = null;
        StringBuffer sbfDynamicQuery = new StringBuffer();
        String strStaticQuery = strRevisionHistoryList;
        Collection<Object> alResultList = new ArrayList<Object>();
        RevisionPlanVO revisionPlanVO = null;
        if(alSearchObjects != null && alSearchObjects.size() > 0)
        {
            String strProdHospSeqId = TTKCommon.checkNull(((SearchCriteria)alSearchObjects.get(0)).getValue());
            String strStartDate = TTKCommon.checkNull(((SearchCriteria)alSearchObjects.get(1)).getValue());
            String strEnddate = TTKCommon.checkNull(((SearchCriteria)alSearchObjects.get(2)).getValue());
            strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"?", strProdHospSeqId);
            sbfDynamicQuery = sbfDynamicQuery.append("  WHERE ( ( from_date >= nvl(to_date( '"+strStartDate+"','dd/mm/yyyy'),to_date('1/1/2002','dd/mm/yyyy') ) AND ( NVL(TO_DATE,nvl2(to_date('"+strEnddate+"','dd/mm/yyyy'),to_date('"+strEnddate+"','dd/mm/yyyy')+1,from_date)) <= nvl(to_date('"+strEnddate+"','dd/mm/yyyy'), NVL(TO_DATE,FROM_DATE)))))");
        }//end of if(alSearchObjects != null && alSearchObjects.size() > 0)
        //build the Order By Clause
        strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"#", (String)alSearchObjects.get(alSearchObjects.size()-4)+" "+(String)alSearchObjects.get(alSearchObjects.size()-3));
        sbfDynamicQuery = sbfDynamicQuery .append( " )A WHERE Q >= "+(String)alSearchObjects.get(alSearchObjects.size()-2)+ " AND Q <= "+(String)alSearchObjects.get(alSearchObjects.size()-1));
        strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();
        try
        {
            conn = ResourceManager.getConnection();
            //  
            pStmt = conn.prepareStatement(strStaticQuery);
            rs = pStmt.executeQuery();
            if (rs!=null)
            {
                while (rs.next()) {
                    revisionPlanVO = new RevisionPlanVO();
                    if (rs.getString("REVISED_PLAN_SEQ_ID")!=null){
                    	revisionPlanVO.setRevPlanSeqId(new Long(rs.getLong("REVISED_PLAN_SEQ_ID")));
                    }//end of if (rs.getString("REVISED_PLAN_SEQ_ID")!=null)
                        
                    revisionPlanVO.setRevisionName(TTKCommon.checkNull(rs.getString("REVISION_NUMBER")));
                    revisionPlanVO.setTariffPlanName(TTKCommon.checkNull(rs.getString("PLAN_NAME")));
                    
                    if (rs.getString("PLAN_SEQ_ID")!=null){
                    	revisionPlanVO.setTariffPlanID(new Long(rs.getLong("PLAN_SEQ_ID")));
                    }//end of if (rs.getString("PLAN_SEQ_ID")!=null)
                        
                    if (rs.getString("DISCOUNT_OFFERED_TTK")!=null){
                    	revisionPlanVO.setDiscountOffered(new BigDecimal(rs.getString("DISCOUNT_OFFERED_TTK")));
                    }//end of if (rs.getString("DISCOUNT_OFFERED_TTK")!=null)
                        
                    if(rs.getTimestamp("FROM_DATE") != null){
                    	revisionPlanVO.setStartDate(new java.util.Date(rs.getTimestamp("FROM_DATE").getTime()));
                    }//end of if(rs.getTimestamp("FROM_DATE") != null)
                        
                    if(rs.getTimestamp("TO_DATE") != null){
                    	revisionPlanVO.setEndDate(new java.util.Date(rs.getTimestamp("TO_DATE").getTime()));
                    }//end of if(rs.getTimestamp("TO_DATE") != null)
                        
                    if (rs.getString("PROD_HOSP_SEQ_ID")!= null){
                    	revisionPlanVO.setProdHospSeqId(new Long(rs.getLong("PROD_HOSP_SEQ_ID")));
                    }//end of if (rs.getString("PROD_HOSP_SEQ_ID")!= null)
                        
                    alResultList.add(revisionPlanVO);
                }//end of while
            }//end of if (rs!=null)
            return  (ArrayList) alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getHospitalRevisionPlanList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getHospitalRevisionPlanList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getHospitalRevisionPlanList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getHospitalRevisionPlanList(ArrayList alSearchObjects, String sortColumnName, String sortOrder, String startRow, String endRow)

    /**
     * This method addsthe RevisionPlanVO which contains the details of the revision of the plan
     * @param revisionPlanVO the details which has to be added
     * @return Long value the revised plan sequence id which is added/updated
     * @exception throws TTKException
     */
    public Long addRevisionPlan(RevisionPlanVO revisionPlanVO) throws TTKException
    {
        Long lResult = null;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAddRevisedPlan);
            if (revisionPlanVO.getRevPlanSeqId() != null){
            	cStmtObject.setLong(1,revisionPlanVO.getRevPlanSeqId());
            }//end of if (revisionPlanVO.getRevPlanSeqId() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else
                
            cStmtObject.setLong(2,revisionPlanVO.getTariffPlanID());
            
            if(revisionPlanVO.getProdHospSeqId() != null){
            	cStmtObject.setLong(3,revisionPlanVO.getProdHospSeqId());
            }//end of if(revisionPlanVO.getProdHospSeqId() != null)
            else{
            	cStmtObject.setString(3,null);
            }//end of else
                
            if (revisionPlanVO.getStartDate() != null && revisionPlanVO.getStartDate()!=""){
            	cStmtObject.setTimestamp(4,new Timestamp(TTKCommon.getUtilDate(revisionPlanVO.getStartDate()).getTime()));
            }//end of if (revisionPlanVO.getStartDate() != null && revisionPlanVO.getStartDate()!="")
            else{
            	cStmtObject.setTimestamp(4, null);
            }//end of else
                
            if(revisionPlanVO.getDiscountOffered()!=null){
            	cStmtObject.setBigDecimal(5,revisionPlanVO.getDiscountOffered());
            }//end of if(revisionPlanVO.getDiscountOffered()!=null)
            else{
            	cStmtObject.setString(5,null);
            }//end of else
                
            cStmtObject.setLong(6,revisionPlanVO.getUpdatedBy());
            cStmtObject.registerOutParameter(1,Types.BIGINT);
            cStmtObject.registerOutParameter(7,Types.INTEGER);
            cStmtObject.execute();
            lResult = cStmtObject.getLong(1);
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
        			log.error("Error while closing the Statement in TariffPlanDAOImpl addRevisionPlan()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl addRevisionPlan()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return lResult;
    }//end of addRevisionPlan(RevisionPlanVO revisionPlanVO)

    /**
     * This method returns the ArrayList object, which contains all the details about the Insurance Companies
     * @param lHospSeqId long Package Hospital Seq Id
     * @return ArrayList object which contains all the details about the Insurance Companies
     * @exception throws TTKException
     */
    public ArrayList getAssociatedCompanyList(long lngHospSeqId) throws TTKException{
    	Connection conn = null;
    	PreparedStatement pStmt = null;
    	ResultSet rs = null;
        ArrayList<Object> alAssociatedTo = new ArrayList<Object>();
        CacheObject cacheObject = null;
        try{
            conn = ResourceManager.getConnection();
            pStmt=conn.prepareStatement(strAssociatedInfo);
            pStmt.setLong(1,lngHospSeqId);
            pStmt.setLong(2,lngHospSeqId);
            rs = pStmt.executeQuery();
            if(rs != null){
                while(rs.next()){
                    cacheObject = new CacheObject();
                    cacheObject.setCacheId((rs.getString("ASSOCIATED_SEQ_ID")));
                    cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("ASSOCIATED_TO")));
                    alAssociatedTo.add(cacheObject);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alAssociatedTo;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getAssociatedCompanyList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getAssociatedCompanyList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getAssociatedCompanyList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getAssociatedCompanyList(long lngHospSeqId)
    
    
    /**
     * This method returns the ArrayList object, which contains all the details about the Insurance Companies
     * @param lHospSeqId long Package Hospital Seq Id
     * @return ArrayList object which contains all the details about the Insurance Companies
     * @exception throws TTKException
     */
    public String uploadTariffEmpanelment(InputStream inputStream2,String fileName, Long userSeqId,String hospitalseqid,String insuranceCode,String tariffType,String networkCategory, String providerForGroup, Long hospGroupSeqId) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	String tariffLog = "";
    	Reader reader	=	null;
    	FileWriter fileWriter	=	null;
    	Clob clob	=	null;
    	String BatchRefNo = "";
        try{
        	
   
            //------------
        	conn = ResourceManager.getConnection();
			cStmtObject=conn.prepareCall(strUploadTariff); 
			XMLType poXML = null;
			if(inputStream2 != null)
			{
				poXML = XMLType.createXML (((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), inputStream2);
			}//end of if(mouDocument != null)
		/*	cStmtObject.setObject(1, poXML);
			cStmtObject.registerOutParameter(2, Types.CLOB);//out parameter which gives the Log file
			cStmtObject.execute();
	        clob=cStmtObject.getClob(2);*/	        
	        
			
			cStmtObject.setLong(1,0);
			cStmtObject.registerOutParameter(1, OracleTypes.BIGINT);
			cStmtObject.setObject(2, poXML);
			cStmtObject.setLong(3, userSeqId);
			cStmtObject.setString(4, fileName);
			cStmtObject.setString(5, hospitalseqid);
			cStmtObject.setString(6, insuranceCode);
			
			cStmtObject.setString(7, tariffType);
			cStmtObject.setString(8, providerForGroup);
			if(hospGroupSeqId != null){
				cStmtObject.setLong(9, hospGroupSeqId);
			}else{
				cStmtObject.setString(9, null);
			}
			
			cStmtObject.setString(10, networkCategory);
			cStmtObject.execute();
			
			BatchRefNo=(BatchRefNo + cStmtObject.getInt(1)).trim();
			
	        
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "tariffplan");
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
        			log.error("Error while closing the Statement in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        			
        			try{
        				if(reader!=null)
        					reader.close();
        			}
        			catch(IOException ioExp)
        			{
        				log.error("Error in Reader");
        			}
        			try{
        				if(fileWriter!=null)
        					fileWriter.close();
        			}catch(IOException ioExp)
        			{
        				log.error("Error in fileWriter");
        			}
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return BatchRefNo;
    }//end of uploadTariffEmpanelment(FileReader xmlFile)
    
    
    public Clob uploadPharmacyTariffEmpanelment(InputStream inputStream2) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	String tariffLog = "";
    	Reader reader	=	null;
    	FileWriter fileWriter	=	null;
    	Clob clob	=	null;
        try{
        	
        	/*cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strUploadTariff);
            cStmtObject.(1, xmlFile,length);
            cStmtObject.registerOutParameter(2, Types.CLOB);//out parameter which gives the Log file

            cStmtObject.execute();
            clob=cStmtObject.getClob(2);*/

            

            //------------
        	conn = ResourceManager.getConnection();
			cStmtObject=conn.prepareCall(strUploadPharmacyTariff); 
			XMLType poXML = null;
			if(inputStream2 != null)
			{
				poXML = XMLType.createXML (((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), inputStream2);
			}//end of if(mouDocument != null)
			cStmtObject.setObject(1, poXML);
			cStmtObject.registerOutParameter(2, Types.CLOB);//out parameter which gives the Log file
			cStmtObject.execute();
	        clob=cStmtObject.getClob(2);
			
		
            return clob;
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "tariffplan");
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
        			log.error("Error while closing the Statement in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        			
        			try{
        				if(reader!=null)
        					reader.close();
        			}
        			catch(IOException ioExp)
        			{
        				log.error("Error in Reader");
        			}
        			try{
        				if(fileWriter!=null)
        					fileWriter.close();
        			}catch(IOException ioExp)
        			{
        				log.error("Error in fileWriter");
        			}
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    }//end of uploadTariffEmpanelment(FileReader xmlFile)

    /*
     * Search tariffs
     */
    public ArrayList getTariffSearchList(ArrayList alSearchObjects,String strIdentifier) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        Collection<Object> alRresultList = new ArrayList<Object>();
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strgetTariffs);
            cStmtObject.setString(1,(String)alSearchObjects.get(0));
            //cStmtObject.setString(2, null);
            if(alSearchObjects.get(0).equals("INS"))
            	cStmtObject.setString(2, (String)alSearchObjects.get(1));
            else if(alSearchObjects.get(0).equals("HOSP"))
            	cStmtObject.setString(2, (String)alSearchObjects.get(2));
            else if(alSearchObjects.get(0).equals("COR"))
            	cStmtObject.setString(2, (String)alSearchObjects.get(4));//corp seq Id
            else if(alSearchObjects.get(0).equals("TPA"))
            	cStmtObject.setString(2, null);//TPA
            cStmtObject.setString(3, (String)alSearchObjects.get(5));//price Ref No.
            cStmtObject.setString(4,(String)alSearchObjects.get(1));//ins seq Id
            

            cStmtObject.setString(5,(String)alSearchObjects.get(7));
            cStmtObject.setString(6,(String)alSearchObjects.get(8));
            cStmtObject.setString(7,(String)alSearchObjects.get(9));
			cStmtObject.setString(8,(String)alSearchObjects.get(3));//Network Type
            
			cStmtObject.setString(9,(String)alSearchObjects.get(11));//10 Sort variable
			cStmtObject.setString(10,(String)alSearchObjects.get(12));//11 Sort Order
			cStmtObject.setString(11,(String)alSearchObjects.get(13));//13 start num
			cStmtObject.setString(12,(String)alSearchObjects.get(14));//14 end num

            cStmtObject.setLong(13,(Long)alSearchObjects.get(6));//user ID
            cStmtObject.setString(14,(String)alSearchObjects.get(10));//end_date
            cStmtObject.registerOutParameter(15,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(15);

            if(rs != null){
                TariffUploadVO	tariffUploadVO	=	null;
                while (rs.next()) {
                	tariffUploadVO	=	new TariffUploadVO();
                	
                tariffUploadVO.setPriceRefNo(TTKCommon.checkNull(rs.getString("price_ref_number")));
                tariffUploadVO.setActivityCode(TTKCommon.checkNull(rs.getString("activity_code")));
                tariffUploadVO.setGrossAmount(TTKCommon.checkNull(rs.getString("gross_amount")));
                tariffUploadVO.setDiscAmount(TTKCommon.checkNull(rs.getString("disc_amount")));
                tariffUploadVO.setDiscPercentage(TTKCommon.checkNull(rs.getString("disc_percentage")));
                tariffUploadVO.setTariffTypeSearch(TTKCommon.checkNull(rs.getString("tariff_type")));
                tariffUploadVO.setTariffSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("tariff_seq_id"))));
                tariffUploadVO.setInternalCode(TTKCommon.checkNull(rs.getString("internal_code")));
                tariffUploadVO.setServiceName(TTKCommon.checkNull(rs.getString("service_name")));
                tariffUploadVO.setActivityDesc(TTKCommon.checkNull(rs.getString("hosp_act_desc")));
                if(rs.getString("end_date")!=null)
                {
              //tariffUploadVO.setEndDate(new Date(rs.getTimestamp("end_date").getTime()));
                	tariffUploadVO.setStrend_date((rs.getString("end_date")));
                }
                tariffUploadVO.setNetworkTypeDesc(TTKCommon.checkNull(rs.getString("NETWORK_DESC")));
                System.out.println("NETWORK_DESC::::::::::::"+rs.getString("NETWORK_DESC"));
                    //  
                    alRresultList.add(tariffUploadVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alRresultList;
        }//end of try
        catch (SQLException sqlExp)
        {
        	
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
        	
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getPackageList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getPackageList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getPackageList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of uploadTagetTariffSearchList(ArrayList alSearchObjects,String strIdentifiere)
    
    /*
     * Search Pharmacy tariffs
     */
    public ArrayList getPharmacyTariffSearchList(ArrayList alSearchObjects,String strIdentifier) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        Collection<Object> alRresultList = new ArrayList<Object>();
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strgetPharmacyTariffs);
            cStmtObject.setString(1,(String)alSearchObjects.get(0));
            cStmtObject.setString(2,(String)alSearchObjects.get(1));
            cStmtObject.setString(3,(String)alSearchObjects.get(2));
            cStmtObject.setString(4,(String)alSearchObjects.get(3));
            cStmtObject.setString(5,(String)alSearchObjects.get(4));
            cStmtObject.setString(6,(String)alSearchObjects.get(5));
            cStmtObject.setString(7,(String)alSearchObjects.get(6));
			cStmtObject.setString(8,(String)alSearchObjects.get(7));
			cStmtObject.setString(9,(String)alSearchObjects.get(8));
			cStmtObject.setString(10,(String)alSearchObjects.get(9));
			cStmtObject.setString(11,(String)alSearchObjects.get(10));
			cStmtObject.setString(12,(String)alSearchObjects.get(11));
            cStmtObject.registerOutParameter(13,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(13);

            if(rs != null){
                TariffUploadVO	tariffUploadVO	=	null;
                while (rs.next()) {
                	tariffUploadVO	=	new TariffUploadVO();
                	
                tariffUploadVO.setPriceRefNo(TTKCommon.checkNull(rs.getString("price_ref_number")));
                tariffUploadVO.setActivityCode(TTKCommon.checkNull(rs.getString("activity_code")));
                tariffUploadVO.setUnitPrice(TTKCommon.checkNull(rs.getString("unit_price")));
                tariffUploadVO.setPkgPrice(TTKCommon.checkNull(rs.getString("pckg_price")));
                tariffUploadVO.setDiscAmount(TTKCommon.checkNull(rs.getString("disc_amt")));
                tariffUploadVO.setDiscPercentage(TTKCommon.checkNull(rs.getString("discount_percentage")));
                tariffUploadVO.setPkgSize(TTKCommon.checkNull(rs.getString("pkgSize")));
                tariffUploadVO.setTariffTypeSearch(TTKCommon.checkNull(rs.getString("tariff_type")));
                tariffUploadVO.setTariffSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("tariff_seq_id"))));
                tariffUploadVO.setInternalCode(TTKCommon.checkNull(rs.getString("internal_code")));
                tariffUploadVO.setServiceName(TTKCommon.checkNull(rs.getString("service_type")));
                tariffUploadVO.setActivityDesc(TTKCommon.checkNull(rs.getString("activity_desc")));
                tariffUploadVO.setInternalCodeDesc(TTKCommon.checkNull(rs.getString("internal_desc")));
                tariffUploadVO.setGrossAmount(TTKCommon.checkNull(rs.getString("unit_price")));
                if(rs.getString("end_date")!=null)
                {
              //tariffUploadVO.setEndDate(new Date(rs.getTimestamp("end_date").getTime()));
                	tariffUploadVO.setStrend_date((rs.getString("end_date")));
                }
                tariffUploadVO.setNetworkTypeDesc(TTKCommon.checkNull(rs.getString("NETWORK_DESC")));
                System.out.println("NETWORK_DESC::::::::::"+rs.getString("NETWORK_DESC"));
                
                    //System.out.println("price_ref_number::"+tariffUploadVO.getPriceRefNo()+"-activity_code::"+tariffUploadVO.getActivityCode());
                    alRresultList.add(tariffUploadVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alRresultList;
        }//end of try
        catch (SQLException sqlExp)
        {
        	
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
        	
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getPackageList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getPackageList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getPackageList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of uploadTagetTariffSearchList(ArrayList alSearchObjects,String strIdentifiere)
    
    
    public TariffUploadVO getTariffDetail(long TariffSeqId,String tariffType,long addedBy) throws TTKException
	{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
	    TariffUploadVO tariffUploadVO	=	null;
	    try {
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTariffDetails);
            cStmtObject.setString(1,tariffType);

            cStmtObject.setLong(2,TariffSeqId);//Sort variable
			cStmtObject.setLong(3,addedBy);//Sort Order
			
            cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(4);

            if(rs != null){
                while (rs.next()) {
                tariffUploadVO	=	new TariffUploadVO();
                tariffUploadVO.setPriceRefNo(TTKCommon.checkNull(rs.getString("price_ref_number")));
                tariffUploadVO.setActivityCode(TTKCommon.checkNull(rs.getString("activity_code")));
                tariffUploadVO.setHospSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("hosp_seq_id"))));
                tariffUploadVO.setInsSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("ins_seq_id"))));
                tariffUploadVO.setHospName(TTKCommon.checkNull(rs.getString("hosp_name")));
                tariffUploadVO.setInsCompName(TTKCommon.checkNull(rs.getString("ins_comp_name")));
                tariffUploadVO.setGrossAmount(TTKCommon.checkNull(rs.getString("gross_amount")));
                tariffUploadVO.setDiscAmount(TTKCommon.checkNull(rs.getString("disc_amount")));
                tariffUploadVO.setActivitySeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("activity_seq_id"))));
                tariffUploadVO.setActivityTypeSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("acitivity_type_seq_id"))));
                tariffUploadVO.setServiceSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("service_seq_id"))));
                tariffUploadVO.setServiceName(TTKCommon.checkNull(rs.getString("service_name")));
                tariffUploadVO.setNetworkTypeSearch(TTKCommon.checkNull(rs.getString("network_type")));
                tariffUploadVO.setPackageId(TTKCommon.checkNull(rs.getString("package_id")));
                tariffUploadVO.setBundleId(TTKCommon.checkNull(rs.getString("bundle_id")));
                
                tariffUploadVO.setGrpRegSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("group_reg_seq_id"))));
                tariffUploadVO.setGrpName(TTKCommon.checkNull(rs.getString("group_name")));
                tariffUploadVO.setGroupId(TTKCommon.checkNull(rs.getString("group_id")));
                tariffUploadVO.setStartDate(rs.getString("start_date")!=null ? new Date(rs.getTimestamp("start_date").getTime()):null);
                tariffUploadVO.setEndDate(rs.getString("end_date")!=null ? new Date(rs.getTimestamp("end_date").getTime()):null);
                tariffUploadVO.setTariffType(TTKCommon.checkNull(rs.getString("tariff_type")));
                tariffUploadVO.setTariffSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("tariff_seq_id"))));
               // tariffUploadVO.setPriceRefSeqId(TTKCommon.getLong(TTKCommon.checkNull(rs.getString("price_ref_seq_id"))));
                tariffUploadVO.setRemarks(TTKCommon.checkNull(rs.getString("remarks")));
                tariffUploadVO.setActivityDesc(TTKCommon.checkNull(rs.getString("hosp_act_desc")));
                tariffUploadVO.setDiscPercentage(TTKCommon.checkNull(rs.getString("disc_percentage")));
                tariffUploadVO.setInternalCodeDesc(TTKCommon.checkNull(rs.getString("INTERNAL_DESC")));
                tariffUploadVO.setInternalCode(TTKCommon.checkNull(rs.getString("INTERNAL_CODE")));
                }//end of while(rs.next())
            }//end of if(rs != null)
            return tariffUploadVO;
        } //end of try
	    catch (SQLException sqlExp) {
	        throw new TTKException(sqlExp, "tariffitem");
	    }//end of catch (SQLException sqlExp) 
	    catch (Exception exp) {
	        throw new TTKException(exp, "tariffitem");
	    } //end of catch (Exception exp)
	     finally
		{
			 //Nested Try Catch to ensure resource closure  
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getPackageList()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getPackageList()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getPackageList()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//End of getTariffDetail(long TariffSeqId,String tariffType,long addedBy)
    
    
    /*
     * Update Tariff Items
     */
    
    public long editTariffItem(TariffUploadVO tariffUploadVO,long addedBy) throws TTKException
	{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	int iResult = 0;
        try{
                conn = ResourceManager.getConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strUpdateTariffItems);
                cStmtObject.setLong(1,(Long)tariffUploadVO.getTariffSeqId());
                if(tariffUploadVO.getHospSeqId()==null)
                	cStmtObject.setString(2, null);
                else
                	cStmtObject.setString(2, tariffUploadVO.getHospSeqId().toString());
                
                if(tariffUploadVO.getInsSeqId()==null)
                	cStmtObject.setString(3,null);
                else
                	cStmtObject.setString(3,tariffUploadVO.getInsSeqId().toString());
                
                if(tariffUploadVO.getGrpRegSeqId()==null)
                	cStmtObject.setString(4, null);
                else
                	cStmtObject.setString(4, tariffUploadVO.getGrpRegSeqId().toString());

                cStmtObject.setString(5, (String)TTKCommon.checkNull(tariffUploadVO.getTariffType()));
                
                if(tariffUploadVO.getPriceRefNo()==null)	
                	cStmtObject.setString(6,null);
                else
                	cStmtObject.setString(6,tariffUploadVO.getPriceRefNo());
                if(tariffUploadVO.getActivitySeqId()==null)
                	cStmtObject.setLong(7, 0);
                else{                	
                	cStmtObject.setLong(7, (Long)tariffUploadVO.getActivitySeqId());
                }if(tariffUploadVO.getActivityTypeSeqId()==null)
                	cStmtObject.setLong(8,0);
                else{
                	cStmtObject.setLong(8, (Long)tariffUploadVO.getActivityTypeSeqId());
                }cStmtObject.setString(9, TTKCommon.checkNull(tariffUploadVO.getGrossAmount()));
                cStmtObject.setString(10, TTKCommon.checkNull(tariffUploadVO.getDiscAmount()));
                cStmtObject.setLong(11, 0);//drug
                if(tariffUploadVO.getServiceSeqId()==null)
                	cStmtObject.setLong(12, 0);
                else{                	
                	cStmtObject.setLong(12, (Long)tariffUploadVO.getServiceSeqId());
                }
                cStmtObject.setString(13, TTKCommon.checkNull(tariffUploadVO.getBundleId()));
                cStmtObject.setString(14, TTKCommon.checkNull(tariffUploadVO.getPackageId()));
                //cStmtObject.setTimestamp(15, new Timestamp(tariffUploadVO.getStartDate().getTime()));
                if(tariffUploadVO.getStartDate() != null)
                {
                	cStmtObject.setTimestamp(15,new Timestamp(tariffUploadVO.getStartDate().getTime()));//START_DATE
                }//end of if(hospitalDetailVO.getTpaRegDate() != null)
                else
                {
                    cStmtObject.setTimestamp(15, null);//START_DATE
                }//end of else
                if(tariffUploadVO.getEndDate() != null)
                {
                	cStmtObject.setTimestamp(16,new Timestamp(tariffUploadVO.getEndDate().getTime()));//END_DATE
                }//end of if(hospitalDetailVO.getTpaRegDate() != null)
                else
                {
                    cStmtObject.setTimestamp(16, null);//END_DATE
                }//end of else
                
                cStmtObject.setString(17, TTKCommon.checkNull(tariffUploadVO.getRemarks()));
                cStmtObject.setLong(18, addedBy);
                cStmtObject.setString(19, TTKCommon.checkNull(tariffUploadVO.getInternalCode()));
                cStmtObject.setString(20, TTKCommon.checkNull(tariffUploadVO.getActivityDesc()));
                cStmtObject.setString(21, TTKCommon.checkNull(tariffUploadVO.getNetworkTypeSearch()));
                cStmtObject.setString(22, TTKCommon.checkNull(tariffUploadVO.getDiscPercentage()));
                cStmtObject.setString(22, TTKCommon.checkNull(tariffUploadVO.getDiscPercentage()));
                cStmtObject.setString(23, TTKCommon.checkNull(tariffUploadVO.getInternalCodeDesc()));
                if("PAC".equals(tariffUploadVO.getSwitchTo())){
                	cStmtObject.setString(24, TTKCommon.checkNull(tariffUploadVO.getStrpkgSize()));
                	cStmtObject.setString(25, TTKCommon.checkNull(tariffUploadVO.getStrpkgPrice()));
                }else{
                	cStmtObject.setString(24, "");
                	cStmtObject.setString(25, "");
                }
                
                cStmtObject.registerOutParameter(26, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(26);
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "tariffplan");
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
        			log.error("Error while closing the Statement in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//End of editTariffItem(TariffUploadVO tariffUploadVO)
    
    
    /*
     * Update Tariff Items
     */
    
    public long editBulkTariffItem(String tariffTypeSearch,String selectedIds,float discAmount,long addedBy) throws TTKException
	{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	int iResult = 0;
        try{
                conn = ResourceManager.getConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strUpdateBulkTariffItems);
                
                /*String ids[]	=	selectedIds.split("@");
                //String amounts[]=	null;
                for(int i=1;i<ids.length;i++){
                	String tariffSeqIds[]	=	ids[i].split("#");
            		cStmtObject.setString(1,tariffTypeSearch);
            		cStmtObject.setString(2,tariffSeqIds[1]);
            		cStmtObject.setString(3,tariffSeqIds[0]);
            		cStmtObject.setLong(4,addedBy);
                    cStmtObject.registerOutParameter(5, Types.INTEGER);//out parameter which gives the number of records deleted
                    cStmtObject.execute();
                    iResult = cStmtObject.getInt(5);
                }*/
                cStmtObject.setString(1,tariffTypeSearch);
        		cStmtObject.setString(2,selectedIds);
        		cStmtObject.setFloat(3,discAmount);
        		cStmtObject.setLong(4,addedBy);
                cStmtObject.registerOutParameter(5, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(5);
                
                
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "tariffplan");
        }//end of catch (Exception exp)
        finally
		{
        	// Nested Try Catch to ensure resource closure  
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//End of editBulkTariffItem(String tariffTypeSearch,String selectedIds,long addedBy)
    
    
    /*
     * Update Tariff Items
     */
    
    public ArrayList<String> getAllServiceNames(String networkType,String providerCode,String payerCode) throws TTKException
	{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	PreparedStatement pStmt = null;
    	ArrayList<String> alResult = null;
    	ResultSet	rs	=	null;
    	
    	
        try{
                conn = ResourceManager.getConnection();
                //pStmt = conn.prepareStatement("SELECT D.SERVICE_SEQ_ID,D.SERVICE_NAME FROM APP.TPA_SERVICE_DETAILS D ORDER BY D.SERVICE_NAME");
               /* String strQry	=	"select k.hosp_seq_id,to_char(wm_concat(k.disc_percent))as disc_percent,k.service_seq_id,k.service_name,k.ins_comp_name,k.hosp_name,to_char(wm_concat(k.network_type))as network_type from "
                		+ "( select hi.hosp_seq_id,td.disc_percent,d.service_seq_id,d.service_name,td.network_type,to_char(wm_concat(md.activity_code))as activity_code,ii.ins_comp_name,hi.hosp_name"
                		+ " from app.tpa_hosp_tariff_details td join app.tpa_hosp_info hi on (td.hosp_seq_id = hi.hosp_seq_id)"
                		+ " join APP.TPA_ACTIVITY_MASTER_DETAILS md on (md.act_mas_dtl_seq_id = td.activity_seq_id)"
                		+ " join APP.TPA_SERVICE_DETAILS D on (d.service_seq_id = td.service_seq_id) join app.tpa_ins_info ii"
                		+ " on (td.ins_seq_id = ii.ins_seq_id) where hi.hosp_licenc_numb in ("+providerCode+")"
                		+ " and ii.ins_comp_code_number = '"+payerCode+"' and td.network_type in ("+networkType+") group by  hi.hosp_seq_id,td.disc_percent,d."
                		+ " service_seq_id,d.service_name,td.network_type,ii.ins_comp_name,hi.hosp_name ) k"
                		+ " group by k.hosp_seq_id,k.service_seq_id,k.service_name,k.ins_comp_name,k.hosp_name ORDER BY k.SERVICE_NAME";
                */
               /* String strQry	=	"select wm_concat(td.disc_percent) as disc_percent,td.service_seq_id as service_seq_id,"
                		+ " d.service_name,ii.ins_comp_name,wm_concat(hi.hosp_name)as hosp_name,wm_concat(td.network_type) as network_type"
                		+ " from app.tpa_hosp_tariff_details td join app.tpa_hosp_info hi on (td.hosp_seq_id = hi.hosp_seq_id)"
                		+ " join APP.TPA_ACTIVITY_MASTER_DETAILS md on (md.act_mas_dtl_seq_id = td.activity_seq_id) join APP.TPA_SERVICE_DETAILS D"
                		+ " on (d.service_seq_id = td.service_seq_id) join app.tpa_ins_info ii on (td.ins_seq_id = ii.ins_seq_id)"
                		+ " where hi.hosp_licenc_numb in ("+providerCode+") and ii.ins_comp_code_number = '"+payerCode+"' and td.network_type in ("+networkType+")"
                		+ " group by ii.ins_comp_name,d.service_name,td.service_seq_id";*/
                
                
                String strQry	=	"SELECT TD.SERVICE_SEQ_ID AS SERVICE_SEQ_ID,D.SERVICE_NAME||'|'||TD.NETWORK_TYPE||'|'||TD.SERVICE_SEQ_ID AS "
                		+"SERVICE_NAME_NETWORK_TYPE FROM APP.TPA_HOSP_TARIFF_DETAILS TD JOIN APP.TPA_HOSP_INFO HI ON "
                		+"(TD.HOSP_SEQ_ID =  HI.HOSP_SEQ_ID) JOIN APP.TPA_ACTIVITY_MASTER_DETAILS MD ON (MD.ACT_MAS_DTL_SEQ_ID "
                		+"= TD.ACTIVITY_SEQ_ID) JOIN APP.TPA_SERVICE_DETAILS D ON (D.SERVICE_SEQ_ID = TD.SERVICE_SEQ_ID) JOIN "
                		+"APP.TPA_INS_INFO II ON (TD.INS_SEQ_ID =  II.INS_SEQ_ID) WHERE HI.HOSP_LICENC_NUMB IN ("+providerCode+") "
                		+"AND II.INS_COMP_CODE_NUMBER = '"+payerCode+"' AND TD.NETWORK_TYPE IN ("+networkType+") GROUP BY II.INS_COMP_NAME, "
                		+"D.SERVICE_NAME||'|'||TD.NETWORK_TYPE,  TD.SERVICE_SEQ_ID";
                
                pStmt = conn.prepareStatement(strQry);
                //  
                
                rs = pStmt.executeQuery();
                if(rs != null){
                    alResult =new ArrayList<String>();
                    while (rs.next()) {
                        //alResult.add(new String[]{rs.getString("service_seq_id"),rs.getString("service_name"),rs.getString("network_type"),rs.getString("disc_percent")});
                    	alResult.add(rs.getString("SERVICE_NAME_NETWORK_TYPE"));
                        
                    }//end of while(rs.next())
                }//end of if(rs != null)
                
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "tariffplan");
        }//end of catch (Exception exp)
        finally
		{
        	// Nested Try Catch to ensure resource closure  
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        			if (pStmt != null) pStmt.close();
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        			throw new TTKException(sqlExp, "tariffplan");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
        				throw new TTKException(sqlExp, "tariffplan");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tariffplan");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		pStmt = null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return alResult;
    }//End of getAllServiceNames()
    
    
    /*
     * Update Pharmacy Tariff Items
     */
    public ArrayList<String> getPharmacyServiceNames(String networkType,String providerCode,String payerCode) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        Collection<String> alRresultList = new ArrayList<String>();
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetPharmacyServiceNames);
            cStmtObject.setString(1,(String)providerCode);
            cStmtObject.setString(2,(String)payerCode);
            cStmtObject.setString(3,(String)networkType); 
            cStmtObject.setString(4,"PAC");
            cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(5);
            	if(rs != null){
                    while (rs.next()) {
                        //alResult.add(new String[]{rs.getString("service_seq_id"),rs.getString("service_name"),rs.getString("network_type"),rs.getString("disc_percent")});
                    	alRresultList.add(rs.getString("SERVICE_NAME_NETWORK_TYPE"));
                    }//end of while(rs.next())
                }//end of if(rs != null)
            return (ArrayList)alRresultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tariffplan");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tariffplan");
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
					log.error("Error while closing the Resultset in TariffPlanDAOImpl getPharmacyServiceNames()",sqlExp);
					throw new TTKException(sqlExp, "tariffplan");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in TariffPlanDAOImpl getPharmacyServiceNames()",sqlExp);
						throw new TTKException(sqlExp, "tariffplan");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in TariffPlanDAOImpl getPharmacyServiceNames()",sqlExp);
							throw new TTKException(sqlExp, "tariffplan");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tariffplan");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }
    
    /*
     * Update Tariff Items by Service types
     */
    
    public int updateServiceTypesByNetworks(String tarifFlag,StringBuffer strService,String networkType,
    		Long userSeqId,String providerCode,String payerCode,String corpCode) throws TTKException
   	{
       	Connection conn = null;
       	CallableStatement cStmtObject=null;
       	int iResult = 0;
           try{
                   conn = ResourceManager.getConnection();
                   cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strUpdateDiscountServiceTypes);
                   cStmtObject.setString(1,tarifFlag);
                  // cStmtObject.setString(2,strService.toString());
                  // cStmtObject.setString(2,networkType);
                   cStmtObject.setString(2,strService.toString());
                   cStmtObject.setLong(3,userSeqId);
                   cStmtObject.setString(4,providerCode);
                   cStmtObject.setString(5,payerCode);
                   cStmtObject.setString(6,corpCode);
                   cStmtObject.registerOutParameter(7, Types.INTEGER);//out parameter which gives the number of records deleted
                   cStmtObject.execute();
                   iResult = cStmtObject.getInt(7);
           }//end of try
           catch (SQLException sqlExp)
           {
                 throw new TTKException(sqlExp, "tariffplan");
           }//end of catch (SQLException sqlExp)
           catch (Exception exp)
           {
                 throw new TTKException(exp, "tariffplan");
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
           			log.error("Error while closing the Statement in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
           			throw new TTKException(sqlExp, "tariffplan");
           		}//end of catch (SQLException sqlExp)
           		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
           		{
           			try
           			{
           				if(conn != null) conn.close();
           			}//end of try
           			catch (SQLException sqlExp)
           			{
           				log.error("Error while closing the Connection in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
           				throw new TTKException(sqlExp, "tariffplan");
           			}//end of catch (SQLException sqlExp)
           		}//end of finally Connection Close
           	}//end of try
           	catch (TTKException exp)
           	{
           		throw new TTKException(exp, "tariffplan");
           	}//end of catch (TTKException exp)
           	finally // Control will reach here in anycase set null to the objects 
           	{
           		cStmtObject = null;
           		conn = null;
           	}//end of finally
   		}//end of finally
           return iResult;
       }//End of updateServiceTypesByNetworks()
    
  //Added for Tariff  Download
    
    public Object[] getTariffUploadedData(String hospSeqId) throws TTKException {
        Connection conn = null;
        CallableStatement cStmtObject=null;
        ResultSet rs = null;
        Object[] objects	=	new Object[1];
        try{
        	conn = ResourceManager.getConnection();
        	ArrayList<String[]> alErrorMsg	=	new ArrayList<String[]>();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetTariffUploadedData);
            cStmtObject.setString(1, hospSeqId);//batch Seq Id
            cStmtObject.registerOutParameter(2, OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				String[] strTemp	=	null;
				while(rs.next()){
					strTemp	=	new String[12];
					strTemp[0]	=	rs.getString("Activity_Code");
					strTemp[1]	=	rs.getString("Internal_Code");
					strTemp[2]	=	rs.getString("Service_Name");
					strTemp[3]	=	rs.getString("Internal_Description");
					strTemp[4]	=	rs.getString("Activity_Description");
					strTemp[5]	=	rs.getString("Package_Id");
					strTemp[6]	=	rs.getString("Bundle_Id");
					strTemp[7]	=	rs.getString("Gross_Amount");
					strTemp[8]	=	rs.getString("Discount_Percentage");
					strTemp[9]	=	rs.getString("From_Date");
					strTemp[10]	=	rs.getString("Expiry_Date");
					strTemp[11]	=	rs.getString("Network_Type");
					alErrorMsg.add(strTemp);
				}
			}
			objects[0]	=	alErrorMsg;
			
            return objects;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in ClaimsDAOImpl getClaimSubmittedDetails()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ClaimsDAOImpl getClaimSubmittedDetails()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ClaimsDAOImpl getClaimSubmittedDetails()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getClaimSubmittedDetails(String batchNo)

	public ArrayList getActivityCodeList(ArrayList<Object> searchData,String switchToVal) throws TTKException{
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ActivityDetailsVO activityDetailsVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strActivityCodeList);
			cStmtObject.setString(1,String.valueOf(searchData.get(0)));
			cStmtObject.setString(2,(String)searchData.get(1));
			cStmtObject.setInt(3,Integer.parseInt((String)searchData.get(5)));
			cStmtObject.setInt(4,Integer.parseInt((String) searchData.get(6)));	
			cStmtObject.setString(5, switchToVal);
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(6);
			if(rs != null){
				while(rs.next()){
					activityDetailsVO = new ActivityDetailsVO();					
					activityDetailsVO.setActivityCode(rs.getString("ACTIVITY_CODE"));
					activityDetailsVO.setActivityCodeDesc(rs.getString("ACTIVITY_DESCRIPTION"));
					activityDetailsVO.setActivitySeqId(rs.getLong("act_mas_dtl_seq_id"));
					activityDetailsVO.setActivityTypeId(String.valueOf(rs.getLong("activity_type_seq_id")));
					activityDetailsVO.setServiceInternalCode(String.valueOf(rs.getLong("service_seq_id")));
//					activityDetailsVO.setInternalCode(rs.getString("INTERNAL_CODE"));
					alResultList.add(activityDetailsVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "onlineforms");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getActivityCodeList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
    
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PreAuthDAOImpl getActivityCodeList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getActivityCodeList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}// END OF getActivityCodeList

//Added for Pharmacy Tariff  Download
    
    public Object[] getPharmacyTariffUploadedData(String hospSeqId) throws TTKException {
        Connection conn = null;
        CallableStatement cStmtObject=null;
        ResultSet rs = null;
        Object[] objects	=	new Object[1];
        try{
        	conn = ResourceManager.getConnection();
        	ArrayList<String[]> alErrorMsg	=	new ArrayList<String[]>();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetPharmcyTariffUploadedData);
            cStmtObject.setString(1, hospSeqId);//batch Seq Id
            cStmtObject.registerOutParameter(2, OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				String[] strTemp	=	null;
				while(rs.next()){
					strTemp	=	new String[12];
					strTemp[0]	=	rs.getString("Activity_Code");
					strTemp[1]	=	rs.getString("Internal_Code");
					strTemp[2]	=	rs.getString("Service_Name");
					strTemp[3]	=	rs.getString("Internal_Description");
					strTemp[4]	=	rs.getString("Activity_Description");
					strTemp[5]	=	rs.getString("Unit_Price");
					strTemp[6]	=	rs.getString("Package_Price");
					strTemp[7]	=   rs.getString("pkgSize");
					strTemp[8]	=	rs.getString("Discount_Percentage");
					strTemp[9]	=	rs.getString("From_Date");
					strTemp[10]	=	rs.getString("Expiry_Date");
					strTemp[11]	=	rs.getString("Network_Type");
					alErrorMsg.add(strTemp);
				}
			}
			objects[0]	=	alErrorMsg;
			
            return objects;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in ClaimsDAOImpl getClaimSubmittedDetails()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ClaimsDAOImpl getClaimSubmittedDetails()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ClaimsDAOImpl getClaimSubmittedDetails()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getClaimSubmittedDetails(String batchNo)
      
    
public Clob uploadBulkModifyTariff(InputStream inputStream) throws TTKException{
	Connection conn = null;
	CallableStatement cStmtObject=null;
	String tariffLog = "";
	Reader reader	=	null;
	FileWriter fileWriter	=	null;
	Clob clob	=	null;
    try{

    	conn = ResourceManager.getConnection();
		cStmtObject=conn.prepareCall(strUploadBulkModify); 
		XMLType poXML = null;
		if(inputStream != null)
		{
			poXML = XMLType.createXML (((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), inputStream);
		}//end of if(mouDocument != null)
		Document podoc = (Document)poXML.getDOM();
		DOMSource domSource = new DOMSource(podoc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		//System.out.println("XML IN String format is: \n" + writer.toString());
		cStmtObject.setObject(1, poXML);
		cStmtObject.registerOutParameter(2, Types.CLOB);//out parameter which gives the Log file
		cStmtObject.execute();
        clob=cStmtObject.getClob(2);
        return clob;
    }//end of try
    catch (SQLException sqlExp)
    {
          throw new TTKException(sqlExp, "tariffplan");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
          throw new TTKException(exp, "tariffplan");
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
    			log.error("Error while closing the Statement in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
    			throw new TTKException(sqlExp, "tariffplan");
    		}//end of catch (SQLException sqlExp)
    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    		{
    			try
    			{
    				if(conn != null) conn.close();
    			}//end of try
    			
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Connection in TariffPlanDAOImpl updateAvailabilityStatus()",sqlExp);
    				throw new TTKException(sqlExp, "tariffplan");
    			}//end of catch (SQLException sqlExp)
    			
    			try{
    				if(reader!=null)
    					reader.close();
    			}
    			catch(IOException ioExp)
    			{
    				log.error("Error in Reader");
    			}
    			try{
    				if(fileWriter!=null)
    					fileWriter.close();
    			}catch(IOException ioExp)
    			{
    				log.error("Error in fileWriter");
    			}
    		}//end of finally Connection Close
    	}//end of try
    	catch (TTKException exp)
    	{
    		throw new TTKException(exp, "tariffplan");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		cStmtObject = null;
    		conn = null;
    	}//end of finally
	}//end of finally
}//end of uploadTariffEmpanelment(FileReader xmlFile)

@SuppressWarnings("unchecked")
	public String[] uploadingTariff(String batchRefNo, Long userSeqId) throws TTKException
   {
   	Connection conn 	= null;
   	CallableStatement cStmtObject=null;
   	Reader reader		=	null;
   	FileWriter fileWriter	=	null;
   	String[] batchNo		=	new String[10];
       try{
       	
           //------------
       	conn = ResourceManager.getConnection();
			cStmtObject=conn.prepareCall(strTariffupload); 
			
			cStmtObject.setLong(1,Long.parseLong(batchRefNo));
			cStmtObject.setLong(2,userSeqId);
			cStmtObject.registerOutParameter(1, OracleTypes.BIGINT);
			cStmtObject.registerOutParameter(3, OracleTypes.VARCHAR);
			cStmtObject.registerOutParameter(4, OracleTypes.VARCHAR);
			cStmtObject.registerOutParameter(5, OracleTypes.VARCHAR);
			/*	cStmtObject.registerOutParameter(5, OracleTypes.VARCHAR);
			cStmtObject.registerOutParameter(6, OracleTypes.VARCHAR);
			cStmtObject.registerOutParameter(7, OracleTypes.VARCHAR);
			cStmtObject.registerOutParameter(8, OracleTypes.VARCHAR);
			cStmtObject.registerOutParameter(9, OracleTypes.VARCHAR);
			cStmtObject.registerOutParameter(10, OracleTypes.VARCHAR);*/
			
			cStmtObject.execute();
			
			batchNo[0]=(""+cStmtObject.getInt(1)).trim();
			
			batchNo[1]=cStmtObject.getString(3);
			batchNo[2]=cStmtObject.getString(4);
			batchNo[3]=cStmtObject.getString(5);
	/*		batchNo[4]=cStmtObject.getString(5);
			batchNo[5]=cStmtObject.getString(6);
			batchNo[6]=cStmtObject.getString(7);
			batchNo[7]=cStmtObject.getString(8);
			batchNo[8]=cStmtObject.getString(9);
			batchNo[9]=cStmtObject.getString(10);
			*/
       }//end of try
       catch (SQLException sqlExp)
       {
             throw new TTKException(sqlExp, "tariffplan");
       }//end of catch (SQLException sqlExp)
       catch (Exception exp)
       {
             throw new TTKException(exp, "tariffplan");
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
       			log.error("Error while closing the Statement in TariffPlanDAOImpl uploadingTariff()",sqlExp);
       			throw new TTKException(sqlExp, "tariffplan");
       		}//end of catch (SQLException sqlExp)
       		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
       		{
       			try
       			{
       				if(conn != null) conn.close();
       			}//end of try
       			
       			catch (SQLException sqlExp)
       			{
       				log.error("Error while closing the Connection in TariffPlanDAOImpl uploadingTariff()",sqlExp);
       				throw new TTKException(sqlExp, "tariffplan");
       			}//end of catch (SQLException sqlExp)
       			
       			try{
       				if(reader!=null)
       					reader.close();
       			}
       			catch(IOException ioExp)
       			{
       				log.error("Error in Reader");
       			}
       			try{
       				if(fileWriter!=null)
       					fileWriter.close();
       			}catch(IOException ioExp)
       			{
       				log.error("Error in fileWriter");
       			}
       		}//end of finally Connection Close
       	}//end of try
       	catch (TTKException exp)
       	{
       		throw new TTKException(exp, "tariffplan");
       	}//end of catch (TTKException exp)
       	finally // Control will reach here in anycase set null to the objects 
       	{
       		cStmtObject = null;
       		conn = null;
       	}//end of finally
		}//end of finally
       return batchNo;
   } //end of saveProviderClaims(DiagnosisDetailsVO diagnosisDetailsVO)



//getClaimUploadErrorLogList
/**
 * This method returns the Arraylist of ClaimUploadErrorLogVO's  which contains Claim Benefit Details
 * @param alSearchCriteria ArrayList object which contains the search criteria
 * @return ArrayList of ClaimUploadErrorLogVO object which contains Claim Benefit details
 * @exception throws TTKException
 */
public ArrayList<Object> getTariffUploadErrorLogList(ArrayList<Object> alSearchCriteria) throws TTKException{
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	Collection<Object> alResultList = new ArrayList<Object>();
	ClaimUploadErrorLogVO claimUploadErrorLogVO = null;
	try{
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetTariffUploadLogList);		
		
		cStmtObject.setString(1,(String)alSearchCriteria.get(0));
		cStmtObject.setString(2,(String)alSearchCriteria.get(1));
		cStmtObject.setString(3,(String)alSearchCriteria.get(2));
		cStmtObject.setString(4,(String)alSearchCriteria.get(3));
		cStmtObject.setString(5,(String)alSearchCriteria.get(4));
		cStmtObject.setString(6,(String)alSearchCriteria.get(5));
		cStmtObject.setString(7,(String)alSearchCriteria.get(6));
		cStmtObject.setString(8,(String)alSearchCriteria.get(7));
		cStmtObject.setString(9,(String)alSearchCriteria.get(8));
		cStmtObject.setString(10,(String)alSearchCriteria.get(9));
		cStmtObject.setString(11,(String)alSearchCriteria.get(10));			
		cStmtObject.registerOutParameter(12,OracleTypes.CURSOR);
		cStmtObject.execute();
		rs = (java.sql.ResultSet)cStmtObject.getObject(12);
		
		if(rs != null){
			while(rs.next()){
				claimUploadErrorLogVO= new  ClaimUploadErrorLogVO();
				if(rs.getString("XML_SEQ_ID") != null){
					claimUploadErrorLogVO.setReferenceNo(""+new Long(rs.getLong("XML_SEQ_ID")));
				}
				claimUploadErrorLogVO.setFileName(TTKCommon.checkNull(rs.getString("FILE_NAME")));
				claimUploadErrorLogVO.setStrAddedDate(TTKCommon.checkNull(rs.getString("ADDED_DATE")));
				claimUploadErrorLogVO.setTariffType(TTKCommon.checkNull(rs.getString("TARIFF_TYPE")));
				if(claimUploadErrorLogVO.getTariffType().equals("PHARMA")){
					claimUploadErrorLogVO.setTariffTypeDesc("Pharmacy/Consumables Tariff");
				}if(claimUploadErrorLogVO.getTariffType().equals("ACT")){
					claimUploadErrorLogVO.setTariffTypeDesc("Activity Tariff");
				}
				alResultList.add(claimUploadErrorLogVO);
			}//end of if(rs != null)
		}//end of if(rs != null)
		return (ArrayList<Object>)alResultList;
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "batch");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "batch");
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
				log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimBatchList()",sqlExp);
				throw new TTKException(sqlExp, "batch");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{	
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimBatchList()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimBatchList()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
}//end of getClaimBatchList(ArrayList alSearchCriteria)


}
