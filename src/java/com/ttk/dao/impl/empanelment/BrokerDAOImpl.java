/**
 * @ (#) BrokerDAOImpl Oct 21, 2005
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

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.BrokerDetailVO;
import com.ttk.dto.empanelment.BrokerVO;

public class BrokerDAOImpl  implements BaseDAO, Serializable{

	private static Logger log = Logger.getLogger(BrokerDAOImpl.class);
	
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
	private static final int  Licence_No  = 28 ;
	private static final int  Reg_date  = 29 ;
	private static final int  LIC_EXP_DT  = 30 ; //kocBroker
	
	//intx
	private static final int  ISD_CODE = 31;
	private static final int  STD_CODE = 32;
	private static final int  PHONE1 = 33;
	private static final int  PHONE2 = 34;
		
	private static final int  ROW_PROCESSED = 37;

	private static final String strBrokerList="SELECT * FROM (SELECT INS_SEQ_ID, INS_COMP_NAME, EMPANELED_DATE, ACTIVE_YN, INS_COMP_CODE_NUMBER, DENSE_RANK() OVER (ORDER BY #, ROWNUM) Q FROM TPA_BRO_INFO WHERE INS_OFFICE_GENERAL_TYPE_ID = 'IHO'" ;
	//private static final String strInsureList="SELECT * FROM (SELECT INS_SEQ_ID, INS_COMP_NAME, EMPANELED_DATE, ACTIVE_YN, DENSE_RANK() OVER (ORDER BY #, ROWNUM) Q FROM TPA_INS_INFO WHERE INS_OFFICE_GENERAL_TYPE_ID = 'IHO' " ;

	//private static final String strInsuranceList="SELECT * FROM (SELECT INS_SEQ_ID, INS_COMP_NAME, EMPANELED_DATE, ACTIVE_YN, DENSE_RANK() OVER (ORDER BY #, ROWNUM) Q FROM TPA_INS_INFO WHERE INS_OFFICE_GENERAL_TYPE_ID = 'IHO' " ;
	//kocBroker
	
	private static final String strInsurancelDetail="SELECT INS_OFFICE_GENERAL_TYPE_ID,TPA_BRO_INFO.INS_PARENT_SEQ_ID,TPA_BRO_INFO.TPA_OFFICE_SEQ_ID,A.INS_COMP_NAME,ACTIVE_YN,INS_COMP_CODE_NUMBER,A.ABBREVATION_CODE,A.INS_SECTOR_GENERAL_TYPE_ID,EMPANELED_DATE,DISB_TYPE_ID,FLOAT_REPLENISHMENT_PERIOD ,FREQUENCY_TYPE_ID,ADDR_SEQ_ID,ADDRESS_1,ADDRESS_2,ADDRESS_3,STATE_TYPE_ID,CITY_TYPE_ID, PIN_CODE,EMAIL_ID, COUNTRY_ID,A.DESCRIPTION,Licence_No,REG_DATE,LIC_EXP_DT,ISD_CODE,STD_CODE,OFFICE_PHONE1,OFFICE_PHONE2 FROM TPA_BRO_INFO, TPA_BRO_ADDRESS,(SELECT INS_SEQ_ID,INS_COMP_NAME, ABBREVATION_CODE, INS_SECTOR_GENERAL_TYPE_ID,DESCRIPTION FROM TPA_BRO_INFO,TPA_GENERAL_CODE  WHERE INS_PARENT_SEQ_ID IS NULL AND TPA_BRO_INFO.INS_SECTOR_GENERAL_TYPE_ID = GENERAL_TYPE_ID AND INS_SEQ_ID = ?)A WHERE TPA_BRO_INFO.INS_SEQ_ID = TPA_BRO_ADDRESS.INS_SEQ_ID AND TPA_BRO_INFO.INS_SEQ_ID = ?";
	private static final String strInsuranceDetail2=" SELECT REMARKS,INACTIVE_FROM, INS_OFFICE_GENERAL_TYPE_ID,TPA_BRO_INFO.INS_PARENT_SEQ_ID,TPA_BRO_INFO.TPA_OFFICE_SEQ_ID,A.INS_COMP_NAME,ACTIVE_YN,INS_COMP_CODE_NUMBER,A.ABBREVATION_CODE,A.INS_SECTOR_GENERAL_TYPE_ID,EMPANELED_DATE,DISB_TYPE_ID,FLOAT_REPLENISHMENT_PERIOD ,FREQUENCY_TYPE_ID,ADDR_SEQ_ID,ADDRESS_1,ADDRESS_2,ADDRESS_3,STATE_TYPE_ID,CITY_TYPE_ID, PIN_CODE,EMAIL_ID, COUNTRY_ID,A.DESCRIPTION,Licence_No,REG_DATE,LIC_EXP_DT,ISD_CODE,STD_CODE,OFFICE_PHONE1,OFFICE_PHONE2 FROM TPA_BRO_INFO, TPA_BRO_ADDRESS,(SELECT INS_SEQ_ID,INS_COMP_NAME, ABBREVATION_CODE, INS_SECTOR_GENERAL_TYPE_ID,DESCRIPTION FROM TPA_BRO_INFO,TPA_GENERAL_CODE  WHERE INS_PARENT_SEQ_ID IS NULL AND TPA_BRO_INFO.INS_SECTOR_GENERAL_TYPE_ID = GENERAL_TYPE_ID AND INS_SEQ_ID = ?)A WHERE TPA_BRO_INFO.INS_SEQ_ID = TPA_BRO_ADDRESS.INS_SEQ_ID AND TPA_BRO_INFO.INS_SEQ_ID = ?";
	private static final String strAddUpdateInsuranceInfo="{CALL BROCOMP_EMPANEL_PKG.PR_BRO_INFO_SAVE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//KOC1179  //kocBroker Added one column for License Exp date- Added 4 columns for IntX for phone Nos
	private static final String strDeleteInsuranceInfo="{CALL BROCOMP_EMPANEL_PKG.PR_INS_INFO_DELETE(?,?)}";
	//private static final String strGetCompanyDetails = "SELECT * FROM (SELECT A.*,DENSE_RANK() OVER (ORDER BY INS_COMP_NAME,ROWNUM) Q FROM(SELECT INS_SEQ_ID, TPA_OFFICE_SEQ_ID,B.DESCRIPTION, INS_PARENT_SEQ_ID,A.INS_SECTOR_GENERAL_TYPE_ID,A.ABBREVATION_CODE, A.INS_COMP_NAME,CASE WHEN A.INS_OFFICE_GENERAL_TYPE_ID != 'IHO' THEN A.INS_COMP_NAME ||'-'|| A.INS_COMP_CODE_NUMBER ELSE A.INS_COMP_NAME END COMPANY,A.INS_COMP_CODE_NUMBER, INS_OFFICE_GENERAL_TYPE_ID FROM (SELECT  INS_SEQ_ID, TPA_OFFICE_SEQ_ID, INS_PARENT_SEQ_ID,INS_SECTOR_GENERAL_TYPE_ID,INS_COMP_CODE_NUMBER,ABBREVATION_CODE, INS_OFFICE_GENERAL_TYPE_ID ,INS_COMP_NAME FROM TPA_INS_INFO START WITH INS_SEQ_ID = ? ";
	// Shortfall CR KOC1179 added EMAIL_ID
	private static final String strGetCompanyDetails = "SELECT * FROM (SELECT A.*,DENSE_RANK() OVER (ORDER BY INS_COMP_NAME,ROWNUM) Q FROM(SELECT INS_SEQ_ID, TPA_OFFICE_SEQ_ID,B.DESCRIPTION, INS_PARENT_SEQ_ID,A.INS_SECTOR_GENERAL_TYPE_ID,A.ABBREVATION_CODE, A.INS_COMP_NAME,CASE WHEN A.INS_OFFICE_GENERAL_TYPE_ID != 'IHO' THEN A.INS_COMP_NAME ||'-'|| A.INS_COMP_CODE_NUMBER ELSE A.INS_COMP_NAME END COMPANY,A.INS_COMP_CODE_NUMBER, INS_OFFICE_GENERAL_TYPE_ID,EMAIL_ID FROM (SELECT  INS_SEQ_ID, TPA_OFFICE_SEQ_ID, INS_PARENT_SEQ_ID,INS_SECTOR_GENERAL_TYPE_ID,INS_COMP_CODE_NUMBER,ABBREVATION_CODE, INS_OFFICE_GENERAL_TYPE_ID ,INS_COMP_NAME,EMAIL_ID FROM TPA_BRO_INFO START WITH INS_SEQ_ID = ? ";
	// Shortfall CR KOC1179 getting EMAIL_ID
	/**
     * This method returns the ArrayList, which contains the list of InsuranceCompany which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of InsuranceCompany object's which contains the InsuranceCompany details
     * @exception throws TTKException
     */
    public ArrayList getBrokerCompanyList(ArrayList alSearchObjects) throws TTKException
	{ 
        StringBuffer sbfDynamicQuery = new StringBuffer();
        String strStaticQuery = strBrokerList;
        Collection<Object> resultList = new ArrayList<Object>();
        BrokerVO  brokerVO = null;
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
                	brokerVO = new BrokerVO();


                    if(rs.getString("INS_SEQ_ID") != null){
                    	brokerVO.setInsuranceSeqID(new Long(rs.getLong("INS_SEQ_ID")));
                    }//end of if(rs.getString("INS_SEQ_ID") != null)

                    brokerVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));

                    if(rs.getDate("EMPANELED_DATE") != null){
                    	brokerVO.setEmpanelmentDate(rs.getDate("EMPANELED_DATE"));
                    }//end of if(rs.getDate("EMPANELED_DATE") != null)

                    brokerVO.setCompanyStatus(TTKCommon.checkNull(rs.getString("ACTIVE_YN")));
                    brokerVO.setActiveInactive(TTKCommon.checkNull(rs.getString("ACTIVE_YN")).equalsIgnoreCase("Y")?"Active":"Inactive");
                    brokerVO.setCompanyCodeNbr(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
                    resultList.add(brokerVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)resultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "broker");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "broker");
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
					log.error("Error while closing the Resultset in BrokerDAOImpl getInsuranceComapanyList()",sqlExp);
					throw new TTKException(sqlExp, "broker");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in BrokerDAOImpl getInsuranceComapanyList()",sqlExp);
						throw new TTKException(sqlExp, "broker");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in BrokerDAOImpl getInsuranceComapanyList()",sqlExp);
							throw new TTKException(sqlExp, "broker");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "broker");
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
		BrokerVO brokerVO = null;
		ArrayList<Object> alBrokerList = null;
		try
		{
			conn = ResourceManager.getConnection();
			pStmt = conn.prepareStatement(strStaticQuery);

			rs = pStmt.executeQuery();
			if(rs!=null){
				alBrokerList = new ArrayList<Object>();
				while(rs.next()){
					brokerVO = new BrokerVO();
					brokerVO.setInsuranceSeqID(rs.getString("INS_SEQ_ID")!=null ? new Long(rs.getLong("INS_SEQ_ID")):null);
					brokerVO.setTTKBranchCode(rs.getString("TPA_OFFICE_SEQ_ID")!=null ? new Long(rs.getString("TPA_OFFICE_SEQ_ID")):null);
					brokerVO.setSectorTypeDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
					brokerVO.setParentSeqID(rs.getString("INS_PARENT_SEQ_ID")!=null ? new Long(rs.getString("INS_PARENT_SEQ_ID")):null);
					brokerVO.setSectorTypeCode(TTKCommon.checkNull(rs.getString("INS_SECTOR_GENERAL_TYPE_ID")));
					brokerVO.setCompanyAbbreviation(TTKCommon.checkNull(rs.getString("ABBREVATION_CODE")));
					brokerVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					brokerVO.setCompanyCodeNbr(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
					brokerVO.setOfficeType(TTKCommon.checkNull(rs.getString("INS_OFFICE_GENERAL_TYPE_ID")));
					brokerVO.setBranchName(TTKCommon.checkNull(rs.getString("COMPANY")));
					alBrokerList.add(brokerVO);
				}//end of while(rs.next())
			}// End of  if(rs!=null)
			return alBrokerList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "broker");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "broker");
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
					log.error("Error while closing the Resultset in BrokerDAOImpl getCompanyDetails()",sqlExp);
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
						log.error("Error while closing the Statement in BrokerDAOImpl getCompanyDetails()",sqlExp);
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
							log.error("Error while closing the Connection in BrokerDAOImpl getCompanyDetails()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "broker");
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
	public BrokerDetailVO getBrokerCompanyDetail(Long lInsuranceParentId,Long lInsuranceCompanyID) throws TTKException
	{
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		BrokerDetailVO  brokerDetailVO =new BrokerDetailVO();
		AddressVO addressVO =new AddressVO();
		try
		{
			conn = ResourceManager.getConnection();
			pStmt = conn.prepareStatement(strInsuranceDetail2);
			pStmt.setLong(1,lInsuranceParentId);  //head office ins_seq_id
			pStmt.setLong(2,lInsuranceCompanyID); //ins_seq_id of the office to be edited
			rs = pStmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					brokerDetailVO.setInsuranceSeqID(lInsuranceCompanyID);
					brokerDetailVO.setOfficeType(TTKCommon.checkNull(rs.getString("INS_OFFICE_GENERAL_TYPE_ID")));
					if (rs.getString("INS_PARENT_SEQ_ID")!=null)
					{
						brokerDetailVO.setParentInsSeqID(new Long(rs.getLong("INS_PARENT_SEQ_ID")));
					}//end of if (rs.getString("INS_PARENT_SEQ_ID")!=null)
					if (rs.getString("TPA_OFFICE_SEQ_ID")!= null)
					{
						brokerDetailVO.setTTKBranchCode(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					}//end of if (rs.getString("TPA_OFFICE_SEQ_ID")!= null)
					brokerDetailVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					brokerDetailVO.setCompanyStatus(TTKCommon.checkNull(rs.getString("ACTIVE_YN")));
					brokerDetailVO.setCompanyCode(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
					brokerDetailVO.setCompanyAbbreviation(TTKCommon.checkNull(rs.getString("ABBREVATION_CODE")));
					// Shortfall CR KOC1179
					brokerDetailVO.setCompanyEmailID(TTKCommon.checkNull(rs.getString("EMAIL_ID")));
					// End Shortfall CR KOC1179
					
					//bROKER 
					brokerDetailVO.setLicenseNo(TTKCommon.checkNull(rs.getString("Licence_No")));
					if(rs.getDate("REG_DATE")!=null)
					{
						brokerDetailVO.setRegDate(new java.util.Date(rs.getTimestamp("REG_DATE").getTime()));
					}//end of if(rs.getDate("REG_DATE")!=null)
					//kocBroker
					if(rs.getDate("LIC_EXP_DT")!=null)
					{
						brokerDetailVO.setLicenseExpDate(new java.util.Date(rs.getTimestamp("LIC_EXP_DT").getTime()));
					}//end of if(rs.getDate("REG_DATE")!=null)
					
					brokerDetailVO.setSectorTypeCode(TTKCommon.checkNull(rs.getString("INS_SECTOR_GENERAL_TYPE_ID")));
					brokerDetailVO.setSectorTypeDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
					if(rs.getDate("EMPANELED_DATE")!=null)
					{
						brokerDetailVO.setEmpanelmentDate(new java.util.Date(rs.getTimestamp("EMPANELED_DATE").getTime()));
					}//end of if(rs.getDate("EMPANELED_DATE")!=null)
					brokerDetailVO.setFundDisbursalCode(TTKCommon.checkNull(rs.getString("DISB_TYPE_ID")));
					if (rs.getString("FLOAT_REPLENISHMENT_PERIOD")!=null)
					{
						brokerDetailVO.setReplenishmentPeriod(new Long(rs.getLong("FLOAT_REPLENISHMENT_PERIOD")));
					}//end of if (rs.getString("FLOAT_REPLENISHMENT_PERIOD")!=null)
					brokerDetailVO.setFrequencyCode(TTKCommon.checkNull(rs.getString("FREQUENCY_TYPE_ID")));
					if(rs.getString("ADDR_SEQ_ID") != null)
					{
						addressVO.setAddrSeqId(new Long(rs.getLong("ADDR_SEQ_ID")));
					}//end of if(rs.getString("ADDR_SEQ_ID") != null)
					addressVO.setAddress1(TTKCommon.checkNull(rs.getString("ADDRESS_1")));
					addressVO.setAddress2(TTKCommon.checkNull(rs.getString("ADDRESS_2")));
					addressVO.setAddress3(TTKCommon.checkNull(rs.getString("ADDRESS_3")));
					addressVO.setStateCode(TTKCommon.checkNull(rs.getString("STATE_TYPE_ID")));
					addressVO.setCityCode(TTKCommon.checkNull(rs.getString("CITY_TYPE_ID")));
					addressVO.setPinCode(TTKCommon.checkNull(rs.getString("PIN_CODE")));
					addressVO.setCountryCode(TTKCommon.checkNull(rs.getString("COUNTRY_ID")));
					addressVO.setIsdCode(TTKCommon.getInt(TTKCommon.checkNull(rs.getString("ISD_CODE"))));
					addressVO.setStdCode(TTKCommon.getInt(TTKCommon.checkNull(rs.getString("STD_CODE"))));
					addressVO.setPhoneNo1(TTKCommon.checkNull(rs.getString("OFFICE_PHONE1")));
					addressVO.setPhoneNo2(TTKCommon.checkNull(rs.getString("OFFICE_PHONE2")));
					brokerDetailVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));
					
					if(rs.getTimestamp("INACTIVE_FROM")!=null)
					{brokerDetailVO.setInactiveDate(new java.util.Date(rs.getTimestamp("INACTIVE_FROM").getTime()));
					}
					brokerDetailVO.setAddress(addressVO);
				}//end of while(rs.next())
			}// End of  if(rs!=null)
			return brokerDetailVO;
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
					log.error("Error while closing the Resultset in BrokerDAOImpl getInsuranceCompanyDetail()",sqlExp);
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
						log.error("Error while closing the Statement in BrokerDAOImpl getInsuranceCompanyDetail()",sqlExp);
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
							log.error("Error while closing the Connection in BrokerDAOImpl getInsuranceCompanyDetail()",sqlExp);
							throw new TTKException(sqlExp, "insurance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "broker");
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
	public long addUpdateInsuranceCompany(BrokerDetailVO brokerDetailVO) throws TTKException
	{
		long lResult =0;
		AddressVO  addressVO = null;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			addressVO = brokerDetailVO.getAddress();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAddUpdateInsuranceInfo);
			if(brokerDetailVO.getInsuranceSeqID() == null)
			{
				cStmtObject.setLong(INS_SEQ_ID,0);//INS_SEQ_ID
			}//end of if(insuranceDetailVO.getInsuranceSeqID() == null)
			else
			{
				cStmtObject.setLong(INS_SEQ_ID,brokerDetailVO.getInsuranceSeqID());//INS_SEQ_ID
			}//end of else
			if(addressVO.getAddrSeqId()!=null)
			{
				cStmtObject.setLong(INS_ADDR_SEQ_ID,addressVO.getAddrSeqId());//INS_ADDR_SEQ_ID
			}//end of if(addressVO.getAddrSeqId()!=null)
			else
			{
				cStmtObject.setLong(INS_ADDR_SEQ_ID,0);
			}//end of else
			if (brokerDetailVO.getTTKBranchCode()!= null)
			{
				cStmtObject.setLong(TPA_OFFICE_SEQ_ID,brokerDetailVO.getTTKBranchCode());//TPA_OFFICE_SEQ_ID
			}//end of if (insuranceDetailVO.getTTKBranchCode()!= null)
			else
			{
				cStmtObject.setString(TPA_OFFICE_SEQ_ID,null);//TPA_OFFICE_SEQ_ID
			}//end of else
			cStmtObject.setString(INS_SECTOR_GENERAL_TYPE_ID,TTKCommon.checkNull(brokerDetailVO.getSectorTypeCode()));//INS_SECTOR_GENERAL_TYPE_ID
			cStmtObject.setString(INS_OFFICE_GENERAL_TYPE_ID,TTKCommon.checkNull(brokerDetailVO.getOfficeType()));//INS_OFFICE_GENERAL_TYPE_ID
			cStmtObject.setString(INS_COMP_NAME,TTKCommon.checkNull(brokerDetailVO.getCompanyName()));//INS_COMP_NAME
			cStmtObject.setString(ABBREVATION_CODE,TTKCommon.checkNull(brokerDetailVO.getCompanyAbbreviation()));//ABBREVATION_CODE
			cStmtObject.setString(INS_COMP_CODE_NUMBER,TTKCommon.checkNull(brokerDetailVO.getCompanyCode()));//INS_COMP_CODE_NUMBER
			// Shortfall CR KOC1179
			cStmtObject.setString(INS_COMPANY_EMAILID,TTKCommon.checkNull(brokerDetailVO.getCompanyEmailID()));//INS_COMPANY_EMAILID
			// End Shortfall CR KOC1179
			
			// Broker License No and Reg Date	
			 cStmtObject.setString(Licence_No,TTKCommon.checkNull(brokerDetailVO.getLicenseNo()));//INS_COMPANY_EMAILID
			
			if(brokerDetailVO.getEmpanelmentDate()!=null && !brokerDetailVO.getRegDate().equals(""))
			{
				cStmtObject.setTimestamp(Reg_date,(new Timestamp(brokerDetailVO.getRegDate().getTime())));//registration_DATE
			}//end of if(insuranceDetailVO.getEmpanelmentDate()!=null && !registration_DATE().equals(""))
			else
			{
				cStmtObject.setTimestamp(Reg_date,null);//registration_DATE
			}//end of else
			
			//kocBroker
			if(brokerDetailVO.getLicenseExpDate()!=null && !brokerDetailVO.getLicenseExpDate().equals(""))
			{
				cStmtObject.setTimestamp(LIC_EXP_DT,(new Timestamp(brokerDetailVO.getLicenseExpDate().getTime())));//License Expiry Date
			}//end of if(insuranceDetailVO.getLicenseExpDate()!=null && !License Expiry Date().equals(""))
			else
			{
				cStmtObject.setTimestamp(LIC_EXP_DT,null);//License Expiry Date
			}//end of else
			
			
			
			if(brokerDetailVO.getEmpanelmentDate()!=null && !brokerDetailVO.getEmpanelmentDate().equals(""))
			{
				cStmtObject.setTimestamp(EMPANELED_DATE,(new Timestamp(brokerDetailVO.getEmpanelmentDate().getTime())));//EMPANELED_DATE
			}//end of if(insuranceDetailVO.getEmpanelmentDate()!=null && !insuranceDetailVO.getEmpanelmentDate().equals(""))
			else
			{
				cStmtObject.setTimestamp(EMPANELED_DATE,null);//EMPANELED_DATE
			}//end of else
			cStmtObject.setString(FREQUENCY_TYPE_ID,TTKCommon.checkNull(brokerDetailVO.getFrequencyCode()));//FREQ_TYPE_ID
			cStmtObject.setString(DISB_TYPE_ID,TTKCommon.checkNull(brokerDetailVO.getFundDisbursalCode()));//DISB_TYPE_ID
			if (brokerDetailVO.getReplenishmentPeriod()== null)
			{
				cStmtObject.setLong(FLOAT_REPLENISHMENT_PERIOD,0);//FLOAT_REPLENISHMENT_PERIOD
			}//end of if (insuranceDetailVO.getReplenishmentPeriod()== null)
			else
			{
				cStmtObject.setLong(FLOAT_REPLENISHMENT_PERIOD,brokerDetailVO.getReplenishmentPeriod());
			}//end of else
			cStmtObject.setString(ACTIVE_YN,TTKCommon.checkNull(brokerDetailVO.getCompanyStatus()));//ACTIVE_YN
			if(brokerDetailVO.getParentInsSeqID()!=null)
			{
				cStmtObject.setLong(INS_PARENT_SEQ_ID,(brokerDetailVO.getParentInsSeqID()));//INS_PARENT_SEQ_ID
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
			if (brokerDetailVO.getOfficeSequenceID()!=null)
			{
				cStmtObject.setLong(INS_OFF_SEQ_ID,brokerDetailVO.getOfficeSequenceID());//INS_OFF_SEQ_ID
			}//end of if (insuranceDetailVO.getOfficeSequenceID()!=null)
			else
			{
				cStmtObject.setLong(INS_OFF_SEQ_ID,0);
			}//end of else
			if(brokerDetailVO.getStartDate()!=null && !brokerDetailVO.getStartDate().equals(""))
			{
				cStmtObject.setTimestamp(VALID_FROM,new Timestamp(brokerDetailVO.getStartDate().getTime()));//VALID_FROM
			}//end of if(insuranceDetailVO.getStartDate()!=null && !insuranceDetailVO.getStartDate().equals(""))
			else
			{
				cStmtObject.setTimestamp(VALID_FROM,null);
			}//end of else
			if(brokerDetailVO.getEndDate()!=null && !brokerDetailVO.getEndDate().equals(""))
			{	
				cStmtObject.setTimestamp(VALID_TO,new Timestamp(brokerDetailVO.getEndDate().getTime()));//VALID_TO
			}
			
			//end of if(insuranceDetailVO.getEndDate()!=null && !insuranceDetailVO.getEndDate().equals(""))
			else
			{
				cStmtObject.setTimestamp(VALID_TO,null);
			}//end of else
			cStmtObject.setLong(USER_SEQ_ID,brokerDetailVO.getUpdatedBy()); //USER_SEQ_ID
			
			cStmtObject.setInt(ISD_CODE,addressVO.getIsdCode());			
			cStmtObject.setInt(STD_CODE,addressVO.getStdCode());
			cStmtObject.setString(PHONE1,addressVO.getPhoneNo1());
			cStmtObject.setString(PHONE2,addressVO.getPhoneNo2());
			if(brokerDetailVO.getInactiveDate()!=null && !brokerDetailVO.getInactiveDate().equals("")){
			cStmtObject.setTimestamp(36, (new Timestamp(brokerDetailVO.getInactiveDate().getTime())));}
			else{
				cStmtObject.setTimestamp(36, null);
				
			}
			cStmtObject.setString(35, brokerDetailVO.getRemarks());
			
			cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);//ROW_PROCESSED
			cStmtObject.registerOutParameter(INS_SEQ_ID,Types.BIGINT);//ROW_PROCESSED
			cStmtObject.execute();
			lResult = cStmtObject.getLong(INS_SEQ_ID);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "broker");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "broker");
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
        			log.error("Error while closing the Statement in BrokerDAOImpl addUpdateInsuranceCompany()",sqlExp);
        			throw new TTKException(sqlExp, "broker");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in BrokerDAOImpl addUpdateInsuranceCompany()",sqlExp);
        				throw new TTKException(sqlExp, "broker");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "broker");
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
			throw new TTKException(sqlExp, "broker");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "broker");
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
        			log.error("Error while closing the Statement in BrokerDAOImpl deleteBrokerCompany()",sqlExp);
        			throw new TTKException(sqlExp, "broker");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in BrokerDAOImpl deleteBrokerCompany()",sqlExp);
        				throw new TTKException(sqlExp, "broker");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "broker");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of deleteInsuranceCompany(String strInsSeqID)
}// end of BrokerDAOImpl
