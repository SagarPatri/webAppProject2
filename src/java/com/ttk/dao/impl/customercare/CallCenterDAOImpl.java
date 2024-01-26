/**
 *  @ (#) CallCenterDAOImpl.java April 10, 2006
 *   Project 	   : TTK HealthCare Services
 *   File          : CallCenterDAOImpl.java
 *   Author        : RamaKrishna K M
 *   Company       : Span Systems Corporation
 *   Date Created  : April 10, 2006
 *
 *   @author       :  RamaKrishna K M
 *   Modified by   :
 *   Modified date :
 *   Reason        :
 */

package com.ttk.dao.impl.customercare;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Collection;

import oracle.jdbc.driver.OracleTypes;
import oracle.sql.BLOB;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.administration.OfficeVO;
import com.ttk.dto.claims.ClaimIntimationVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.customercare.CallCenterAssignmentVO;
import com.ttk.dto.customercare.CallCenterDetailVO;
import com.ttk.dto.customercare.CallCenterListVO;
import com.ttk.dto.customercare.CallCenterVO;
import com.ttk.dto.customercare.EnquiryDetailVO;
import com.ttk.dto.displayOfBenefits.BenefitsDetailsVO;

public class CallCenterDAOImpl implements BaseDAO, Serializable
{
	private static Logger log = Logger.getLogger(CallCenterDAOImpl.class );
    
	private static final String strCallList = "{CALL CALL_CENTER_PKG.SELECT_CALL_LOG_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//KOC FOR Grievance cigna 2 parameter
	private static final String strCallDetails = "{CALL CALL_CENTER_PKG.SELECT_CALL_LOG(?,?,?,?)}";
	private static final String strCallerTypeList = "SELECT GENERAL_TYPE_ID AS CALLER_TYPE_ID FROM TPA_GENERAL_CODE WHERE HEADER_TYPE = 'CALLER_TYPE'";
	private static final String strCallCategoryTypeList = "SELECT GENERAL_TYPE_ID AS CATEGORY_TYPE_ID FROM TPA_GENERAL_CODE WHERE HEADER_TYPE = 'CALL_CATEGORY'";
	private static final String strCategoryReasonList = "SELECT CALL_RSON_TYPE_ID,CALL_RSON_DESCRIPTION FROM TPA_CALL_REASON_CODE WHERE CALL_CAT_GENERAL_TYPE_ID = ?";//Based on CallCategoryTypeID
	private static final String strReasonList = "SELECT CALL_RSON_TYPE_ID,CALL_RSON_DESCRIPTION FROM TPA_CALL_REASON_CODE WHERE CALLER_TYPE_GENERAL_TYPE_ID = ? AND CALL_CAT_GENERAL_TYPE_ID = ?";//Based on CallerType and CallCategoryType
	private static final String strCallReasonList = "SELECT CALL_RSON_TYPE_ID FROM TPA_CALL_REASON_CODE";
	private static final String strCallAnswerList = "SELECT CALL_ANS_TYPE_ID,CALL_ANS_DESCRIPTION FROM TPA_CALL_ANSWER_CODE WHERE CALL_RSON_TYPE_ID =?";
	private static final String strSaveCallDetails = "{CALL CALL_CENTER_PKG.SAVE_CALL_LOG(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//shortfall phase1 one parameter we added
	private static final String strDeleteCall= "{CALL CALL_CENTER_PKG.DELETE_CALL_LOG(?,?,?)}";
	private static final String strAssignDepartment = "{CALL CALL_CENTER_PKG.SAVE_ASSIGN_DEPT(?,?,?,?,?,?,?)}";
	private static final String strSelectAssignTo = "{CALL CALL_CENTER_PKG.SELECT_ASSIGN_TO(?,?,?)}";
	private static final String strEnquiryDetail="{CALL CALL_CENTER_PKG.CALL_CENTER_MISCELLANEOUS(?,?,?)}";
    private static final String strTTKOfficeList="{CALL CALL_CENTER_PKG.TTK_OFFICE_LIST(?,?,?,?,?,?)}";
    private static final String strGetPolicyTobFile = "{CALL ADMINISTRATION_PKG.SELECT_POLICY_TOB_DOC(?,?)}";

	private static final String strGetbenefitDetails = "{CALL DISPLAY_OF_BENEFITS.GET_POLICY_TOB(?,?,?,?,?,?,?)}";

        
	private static final int CALL_LOG_SEQ_ID = 1;
	private static final int CALLER_TYPE_GENERAL_TYPE_ID = 2;
	private static final int CALL_RCVD_THRU_GENERAL_TYPE_ID = 3;
	private static final int CALL_LOG_TYPE_GENERAL_TYPE_ID = 4;
	private static final int CALLER_NAME = 5;
	private static final int MEMBER_SEQ_ID = 6;
	private static final int POLICY_SEQ_ID = 7;
	private static final int POLICY_NUMBER = 8;
	private static final int TPA_ENROLLMENT_ID = 9;
	private static final int CLAIMANT_NAME = 10;
	private static final int GROUP_REG_SEQ_ID = 11;
	private static final int HOSP_SEQ_ID = 12;
	private static final int INS_SEQ_ID = 13;
	private static final int OFF_PHONE_NO_1 =14;
	private static final int RES_PHONE_NO = 15;
	private static final int MOBILE_NO = 16;
	private static final int PRIMARY_EMAIL_ID = 17;
	private static final int CALL_CONTENT = 18;
	private static final int CALL_RSON_TYPE_ID = 19;
	private static final int CALL_SUB_RSON_TYPE_ID = 20;
	private static final int CALL_CAT_GENERAL_TYPE_ID = 21;
	private static final int CALL_SUB_CAT_GENERAL_TYPE_ID = 22;
	private static final int PRIORITY_GENERAL_TYPE_ID = 23;
	private static final int CALL_LOG_BY_GENERAL_TYPE_ID = 24;
	private static final int TPA_OFFICE_SEQ_ID = 25;
	private static final int POSSIBILITY_OF_FRAUD_YN = 26;
	private static final int CLAIM_INTIMATION_SEQ_ID = 27;
	private static final int ESTIMATED_AMOUNT = 28;
	private static final int LIKELY_DATE_OF_HOSPITALISATION = 29;
	private static final int AILMENT_DESCRIPTION = 30;
	private static final int HOSPITAL_NAME = 31;
	private static final int HOSPITAL_ADDRESS = 32;
	private static final int ACTION_GENERAL_TYPE_ID = 33;
	private static final int CALL_ANS_TYPE_ID = 34;
	private static final int CALL_REMARKS = 35;
	private static final int CONTACT_SEQ_ID = 36;
	private static final int PARENT_CALL_LOG_SEQ_ID = 37;
	private static final int CALL_LOG_NUMBER = 38;
	private static final int CHILD_CALL_CONTENT = 39;
	private static final int CHILD_MOBILE_NO = 40;
	private static final int INS_SCHEME = 41;
	private static final int CERTIFICATE_NO = 42;
	private static final int INS_CUSTOMER_CODE = 43;
	private static final int CREDITCARD_NO = 44;
	private static final int EMPANEL_NUMBER = 45;
	private static final int OFFICE_NAME = 46;
	private static final int GROUP_ID = 47;
	private static final int GROUP_NAME = 48;
	private static final int INS_COMP_CODE_NUMBER = 49;
	private static final int INS_COMP_NAME = 50;
	private static final int USER_SEQ_ID = 51;
	private static final int CLM_INT_DATE = 52; //shortfall phase1
	private static final int CALLBACK_YN = 53; //INTx
	private static final int CALL_TYPE	 = 54; //INTx
	private static final int ESCLATION_YN= 55; //INTx
	private static final int ROW_PROCESSED = 56;

	/**
     * This method returns the Arraylist of CallCenterVO's  which contains Customer Care Call Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of CallCenterVO object which contains Customer Care Call Details
     * @exception throws TTKException
     */
    public ArrayList getCallList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CallCenterVO callCenterVO = null;
        String strLink[]= {"5"};
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCallList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//CALLER_TYPE_GENERAL_TYPE_ID
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//SEQ_ID
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//CALLER_NAME
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//CALL_LOG_NUMBER
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));//CALL_CAT_GENERAL_TYPE_ID
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));//TPA_OFFICE_SEQ_ID
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));//ACTION_GENERAL_TYPE_ID
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));//POLICY_NUMBER
			cStmtObject.setString(9,(String)alSearchCriteria.get(8));//INS_SCHEME
			cStmtObject.setString(10,(String)alSearchCriteria.get(9));//CERTIFICATE_NO
			cStmtObject.setString(11,(String)alSearchCriteria.get(10));//CREDITCARD_NO
			cStmtObject.setString(12,(String)alSearchCriteria.get(11));//START_LIMIT_TYPE
			cStmtObject.setString(13,(String)alSearchCriteria.get(12));//SORT_VAR
			cStmtObject.setString(14,(String)alSearchCriteria.get(13));//SORT_ORDER
			cStmtObject.setString(15,(String)alSearchCriteria.get(16));//START_NUM
			//cStmtObject.setString(16,(String)alSearchCriteria.get(16));//END_NUM
			cStmtObject.setString(16,"DESC");//END_NUM
			cStmtObject.setString(17,(String)alSearchCriteria.get(18));//KOC FOR Grievance cign
			cStmtObject.setString(18,(String)alSearchCriteria.get(19));//KOC FOR Grievance cign
			cStmtObject.setLong(19,(Long)alSearchCriteria.get(15));//ADDED_BY		
			cStmtObject.setString(20,(String)alSearchCriteria.get(14));//call back Yes/No
			cStmtObject.registerOutParameter(21,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(21);
			if(rs != null){
				while(rs.next()){
					callCenterVO = new CallCenterVO();

					if(rs.getString("CALL_LOG_SEQ_ID") != null){
						callCenterVO.setLogSeqID(new Long(rs.getLong("CALL_LOG_SEQ_ID")));
					}//end of if(rs.getString("CALL_LOG_SEQ_ID") != null)

					if(rs.getString("PARENT_CALL_LOG_SEQ_ID") != null){
						callCenterVO.setParentCallLogSeqID(new Long(rs.getLong("PARENT_CALL_LOG_SEQ_ID")));
					}//end of if(rs.getString("PARENT_CALL_LOG_SEQ_ID") != null)

					callCenterVO.setLogNumber(TTKCommon.checkNull(rs.getString("CALL_LOG_NUMBER")));
					callCenterVO.setLogTypeDesc(TTKCommon.checkNull(rs.getString("LOG_TYPE")));
					callCenterVO.setCategoryDesc(TTKCommon.checkNull(rs.getString("CALL_CATEGORY")));
					callCenterVO.setRecordedBy(TTKCommon.checkNull(rs.getString("CALL_LOGGED_BY_USER")));

					if(rs.getString("CALL_RECORDED_DATE") != null){
						callCenterVO.setLogRecordDate(new Date(rs.getTimestamp("CALL_RECORDED_DATE").getTime()));
					}//end of if(rs.getString("CALL_RECORDED_DATE") != null)

					callCenterVO.setStatusDesc(TTKCommon.checkNull(rs.getString("STATUS")));
                    if(callCenterVO.getStatusDesc().equals("Closed")){
                    	callCenterVO.setSuppressLink(strLink);
                    }//end of if(callCenterVO.getStatusDesc().equals("Closed"))

					callCenterVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					callCenterVO.setPolicyNumber(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
					callCenterVO.setClaimantName(TTKCommon.checkNull(rs.getString("CLAIMANT_NAME")));
					//callCenterVO.setOfficeName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));

					/*if(rs.getString("TPA_OFFICE_SEQ_ID") != null){
						callCenterVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					}//end of if(rs.getString("TPA_OFFICE_SEQ_ID") != null)
*/
					/*if(rs.getString("CALL_CLARIFY_SEQ_ID") != null){
						callCenterVO.setClarifySeqID(new Long(rs.getLong("CALL_CLARIFY_SEQ_ID")));
					}//end of if(rs.getString("CALL_CLARIFY_SEQ_ID") != null)
*/
					callCenterVO.setInsCompName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					callCenterVO.setInsCompCodeNbr(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
					callCenterVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					//callCenterVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));
					callCenterVO.setHospName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
					callCenterVO.setEmpanelmentNbr(TTKCommon.checkNull(rs.getString("EMPANEL_NUMBER")));
					callCenterVO.setCallerName(TTKCommon.checkNull(rs.getString("CALLER_NAME")));
					//INTX
					callCenterVO.setCallbackYN("");
					if(("Y").equals(rs.getString("CALL_BCK_YN")))
					{
						callCenterVO.setPriorityImageName("callBackIcon1");
						callCenterVO.setPriorityImageTitle("Call Back");
					}
					if(rs.getString("POLICY_GROUP_SEQ_ID") != null){
						callCenterVO.setPolicyGrpSeqID(new Long(rs.getLong("POLICY_GROUP_SEQ_ID")));
					}//end of if(rs.getString("POLICY_GROUP_SEQ_ID") != null)
					
					callCenterVO.setTemplateName(TTKCommon.checkNull(rs.getString("TEMPLATE_NAME")));
					
					if(rs.getString("MEMBER_SEQ_ID") != null){
						callCenterVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
					}
					
					
					callCenterVO.setsQatarId(TTKCommon.checkNull(rs.getString("QATAR_ID")));
					
					alResultList.add(callCenterVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "callcenter");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "callcenter");
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
					log.error("Error while closing the Resultset in CallCenterDAOImpl getCallList()",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in CallCenterDAOImpl getCallList()",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in CallCenterDAOImpl getCallList()",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getCallList(ArrayList alSearchCriteria)

    /**
     * This method returns EnquiryDetailVO object containing enquiry details of Search criteria
     * @param strEnquiryType Flag for getting the Enquiry Details of Hospotal/Insurance/TTK Office
     * @param lngSeqID Seq ID of the Search Criteria
     * @return EnquiryDetailVO Object Containing the details
     * @throws TTKException if any runtime Exception occures
     */
    public EnquiryDetailVO getEnquiryDetail(String strEnquiryType,Long lngSeqID) throws TTKException
    {
        EnquiryDetailVO enquiryDetailVO=new EnquiryDetailVO();
        Connection conn = null;
        CallableStatement cStmtObject=null;
        ResultSet rs=null;
        try{
            conn = ResourceManager.getConnection();

            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strEnquiryDetail);
            cStmtObject.setString(1,strEnquiryType);
            cStmtObject.setLong(2,lngSeqID);
            cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(3);

            if(rs!=null)
            {
                while(rs.next())
                {
                    if(strEnquiryType.equals("HOS"))    //For getting Hospital Details
                    {
                        enquiryDetailVO.setName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
                        enquiryDetailVO.setStdCode(TTKCommon.checkNull(rs.getString("STD_CODE")));
                        enquiryDetailVO.setLandmark(TTKCommon.checkNull(rs.getString("LANDMARKS")));
                        enquiryDetailVO.setOfficePhone1(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_1")));
                        enquiryDetailVO.setOfficePhone2(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_2")));
                        enquiryDetailVO.setFax1(TTKCommon.checkNull(rs.getString("OFFICE_FAX_NO")));
                        enquiryDetailVO.setEmailID(TTKCommon.checkNull(rs.getString("PRIMARY_EMAIL_ID")));
                    }//end of if(strEnquiryType.equals("HOS"))

                    else if(strEnquiryType.equals("INS"))   //For getting the Insurance Details
                    {
                        enquiryDetailVO.setName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
                        enquiryDetailVO.setAbbrCode(TTKCommon.checkNull(rs.getString("ABBREVATION_CODE")));
                        enquiryDetailVO.setShortName(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
                    } //end of else if(strEnquiryType.equals("INS"))

                    else if (strEnquiryType.equals("TTK"))  //For getting the TTK Office Details
                    {
                        enquiryDetailVO.setName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));
                        enquiryDetailVO.setOfficeType(TTKCommon.checkNull(rs.getString("OFFICE_TYPE")));
                        enquiryDetailVO.setReportTo(TTKCommon.checkNull(rs.getString("REPORT_TO")));
                        enquiryDetailVO.setAbbrCode(TTKCommon.checkNull(rs.getString("OFFICE_CODE")));
                        if(TTKCommon.checkNull(rs.getString("ACTIVE_YN")).equalsIgnoreCase("Y"))
                        {
                            enquiryDetailVO.setActiveYN("Yes");
                        }//end of if(TTKCommon.checkNull(rs.getString("ACTIVE_YN")).equalsIgnoreCase("Y"))
                        else
                        {
                            enquiryDetailVO.setActiveYN("No");
                        }//end of else
                        enquiryDetailVO.setStdCode(TTKCommon.checkNull(rs.getString("STD_CODE")));
                        enquiryDetailVO.setEmailID(TTKCommon.checkNull(rs.getString("EMAIL_ID")));
                        enquiryDetailVO.setAlternateEmailID(TTKCommon.checkNull(rs.getString("ALT_EMAIL_ID")));
                        enquiryDetailVO.setOfficePhone1(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_1")));
                        enquiryDetailVO.setOfficePhone2(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_2")));
                        enquiryDetailVO.setFax1(TTKCommon.checkNull(rs.getString("OFFICE_FAX_NO_1")));
                        enquiryDetailVO.setAlternateFax(TTKCommon.checkNull(rs.getString("OFFICE_FAX_NO_2")));
                        enquiryDetailVO.setTollFreeNbr(TTKCommon.checkNull(rs.getString("TOLL_FREE_NO")));
                    }//end of else if (strEnquiryType.equals("TTK"))

                    //details common to all the types
                    enquiryDetailVO.setAddress1(TTKCommon.checkNull(rs.getString("ADDRESS_1")));
                    enquiryDetailVO.setAddress2(TTKCommon.checkNull(rs.getString("ADDRESS_2")));
                    enquiryDetailVO.setAddress3(TTKCommon.checkNull(rs.getString("ADDRESS_3")));
                    enquiryDetailVO.setCityDesc(TTKCommon.checkNull(rs.getString("CITY_DESCRIPTION")));
                    enquiryDetailVO.setStateName(TTKCommon.checkNull(rs.getString("STATE_NAME")));
                    enquiryDetailVO.setCountryName(TTKCommon.checkNull(rs.getString("COUNTRY_NAME")));
                    enquiryDetailVO.setPinCode(TTKCommon.checkNull(rs.getString("PIN_CODE")));
                }//end of while(rs.next())
            }//end of if(rs!=null)
            return enquiryDetailVO;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "callcenter");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "callcenter");
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
					log.error("Error while closing the Resultset in CallCenterDAOImpl getEnquiryDetail()",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in CallCenterDAOImpl getEnquiryDetail()",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in CallCenterDAOImpl getEnquiryDetail()",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getEnquiryDetail(String strEnquiryType,Long lngSeqID)

    /**
     * This method returns the CallCenterDetailVO, which contains all the Customer Care Call Details
     * @param lngLogSeqID long value contains SeqID to fetch the Customer Care Call Details
     * @param lngUserSeqID long value contains logged-in user seq id
     * @return CallCenterDetailVO object which contains all the Customer Care Call Details
     * @exception throws TTKException
     */
    public CallCenterDetailVO getCallDetail(long lngLogSeqID,long lngUserSeqID) throws TTKException {
    	CallCenterDetailVO callCenterDetailVO = new CallCenterDetailVO();
    	ClaimIntimationVO claimIntimationVO = null;
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs1 = null;
    	ResultSet rs2 = null;
    	ArrayList<Object> alCallCenterList = new ArrayList<Object>();
    	CallCenterListVO callcenterListVO = null;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCallDetails);
    		cStmtObject.setLong(1,lngLogSeqID);
    		cStmtObject.setLong(2,lngUserSeqID);
    		cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
    		cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
    		cStmtObject.execute();
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
			rs2 = (java.sql.ResultSet)cStmtObject.getObject(4);

			if(rs1 != null){
				while(rs1.next()){
					claimIntimationVO = new ClaimIntimationVO();

					if(rs1.getString("CALL_LOG_SEQ_ID") != null){
						callCenterDetailVO.setLogSeqID(new Long(rs1.getLong("CALL_LOG_SEQ_ID")));
					}//end of if(rs.getString("CALL_LOG_SEQ_ID") != null)

					if(rs1.getString("PARENT_CALL_LOG_SEQ_ID") != null){
						callCenterDetailVO.setParentCallLogSeqID(new Long(rs1.getLong("PARENT_CALL_LOG_SEQ_ID")));
					}//end of if(rs1.getString("PARENT_CALL_LOG_SEQ_ID") != null)

					callCenterDetailVO.setLogNumber(TTKCommon.checkNull(rs1.getString("CALL_LOG_NUMBER")));
					callCenterDetailVO.setCallerTypeID(TTKCommon.checkNull(rs1.getString("CALLER_TYPE_GENERAL_TYPE_ID")));
					callCenterDetailVO.setCallerTypeDesc(TTKCommon.checkNull(rs1.getString("CALLER_TYPE")));
					callCenterDetailVO.setSourceTypeID(TTKCommon.checkNull(rs1.getString("CALL_RCVD_THRU_GENERAL_TYPE_ID")));
					callCenterDetailVO.setSourceTypeDesc(TTKCommon.checkNull(rs1.getString("COMM_TYPE_DESC")));
					callCenterDetailVO.setLogTypeID(TTKCommon.checkNull(rs1.getString("CALL_LOG_TYPE_GENERAL_TYPE_ID")));
					callCenterDetailVO.setLogTypeDesc(TTKCommon.checkNull(rs1.getString("LOG_TYPE")));
					callCenterDetailVO.setCallerName(TTKCommon.checkNull(rs1.getString("CALLER_NAME")));
					callCenterDetailVO.setOfficePhoneNbr(TTKCommon.checkNull(rs1.getString("OFF_PHONE_NO_1")));
					callCenterDetailVO.setHomePhoneNbr(TTKCommon.checkNull(rs1.getString("RES_PHONE_NO")));
					callCenterDetailVO.setMobileNumber(TTKCommon.checkNull(rs1.getString("MOBILE_NO")));
					callCenterDetailVO.setEmailID(TTKCommon.checkNull(rs1.getString("PRIMARY_EMAIL_ID")));
					callCenterDetailVO.setCallContent(TTKCommon.checkNull(rs1.getString("CALL_CONTENT")));
					//KOC FOR Grievance
					callCenterDetailVO.setGenderDescription(TTKCommon.checkNull(rs1.getString("gender_general_type_id")));
					

					if(rs1.getString("MEM_AGE") != null){
						callCenterDetailVO.setAge(new Integer(rs1.getString("MEM_AGE")));
					}//end of if(rs.getString("MEM_AGE") != null)
					
					callCenterDetailVO.setSeniorCitizenYN(TTKCommon.checkNull(rs1.getString("senior_citizen_yn")));//koc for griavance
					

					//KOC FOR Grievance added for gopinath
					if(rs1.getString("CLM_INT_DATE") != null){
						callCenterDetailVO.setIntimationDate(new Date((rs1.getTimestamp("CLM_INT_DATE").getTime())));//shortfall phase1
						}
					
					
					if(rs1.getString("MEMBER_SEQ_ID") != null){
						callCenterDetailVO.setMemberSeqID(new Long(rs1.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs1.getString("MEMBER_SEQ_ID") != null)
					
					if(rs1.getString("POLICY_SEQ_ID") != null){
						callCenterDetailVO.setPolicySeqID(new Long(rs1.getLong("POLICY_SEQ_ID")));
					}//end of if(rs1.getString("POLICY_SEQ_ID") != null)

					if(TTKCommon.checkNull(rs1.getString("CALLER_TYPE_GENERAL_TYPE_ID")).equals("CTC")){
						callCenterDetailVO.setCorPolicyNumber(TTKCommon.checkNull(rs1.getString("POLICY_NUMBER")));
					}//end of if(TTKCommon.checkNull(rs1.getString("CALLER_TYPE_GENERAL_TYPE_ID")).equals("CTC"))
					else{
						callCenterDetailVO.setPolicyNumber(TTKCommon.checkNull(rs1.getString("POLICY_NUMBER")));
					}//end of else

					callCenterDetailVO.setEnrollmentID(TTKCommon.checkNull(rs1.getString("TPA_ENROLLMENT_ID")));
					callCenterDetailVO.setClaimantName(TTKCommon.checkNull(rs1.getString("CLAIMANT_NAME")));

					if(rs1.getString("GROUP_REG_SEQ_ID") != null){
						callCenterDetailVO.setGroupRegnSeqID(new Long(rs1.getLong("GROUP_REG_SEQ_ID")));
					}//end of if(rs1.getString("GROUP_REG_SEQ_ID") != null)

					callCenterDetailVO.setGroupID(TTKCommon.checkNull(rs1.getString("GROUP_ID")));
					callCenterDetailVO.setGroupName(TTKCommon.checkNull(rs1.getString("GROUP_NAME")));

					if(rs1.getString("HOSP_SEQ_ID") != null){
						callCenterDetailVO.setHospSeqID(new Long(rs1.getLong("HOSP_SEQ_ID")));
					}//end of if(rs1.getString("HOSP_SEQ_ID") != null)

					callCenterDetailVO.setHospName(TTKCommon.checkNull(rs1.getString("HOSP_NAME")));
					callCenterDetailVO.setEmpanelmentNbr(TTKCommon.checkNull(rs1.getString("EMPANEL_NUMBER")));

					if(rs1.getString("INS_SEQ_ID") != null){
						callCenterDetailVO.setInsSeqID(new Long(rs1.getLong("INS_SEQ_ID")));
					}//end of if(rs1.getString("INS_SEQ_ID") != null)

					callCenterDetailVO.setInsCompName(TTKCommon.checkNull(rs1.getString("INS_COMP_NAME")));
					callCenterDetailVO.setInsCompCodeNbr(TTKCommon.checkNull(rs1.getString("INS_COMP_CODE_NUMBER")));
					callCenterDetailVO.setReasonTypeID(TTKCommon.checkNull(rs1.getString("CALL_RSON_TYPE_ID")));
					callCenterDetailVO.setReasonTypeDesc(TTKCommon.checkNull(rs1.getString("CALL_RSON_DESCRIPTION")));
					callCenterDetailVO.setSubReasonTypeID(TTKCommon.checkNull(rs1.getString("CALL_SUB_RSON_TYPE_ID")));
					callCenterDetailVO.setSubReasonDesc(TTKCommon.checkNull(rs1.getString("CALL_SUB_RSON_DESCRIPTION")));
					callCenterDetailVO.setCategoryTypeID(TTKCommon.checkNull(rs1.getString("CALL_CAT_GENERAL_TYPE_ID")));
					callCenterDetailVO.setCategoryDesc(TTKCommon.checkNull(rs1.getString("CATEGORY")));
					callCenterDetailVO.setSubCategoryTypeID(TTKCommon.checkNull(rs1.getString("CALL_SUB_CAT_GENERAL_TYPE_ID")));
					callCenterDetailVO.setSubCategoryDesc(TTKCommon.checkNull(rs1.getString("SUB_CATEGORY")));
					callCenterDetailVO.setPriorityTypeID(TTKCommon.checkNull(rs1.getString("PRIORITY_GENERAL_TYPE_ID")));
					callCenterDetailVO.setPriorityDesc(TTKCommon.checkNull(rs1.getString("PRIORITY")));
					callCenterDetailVO.setLoggedByTypeID(TTKCommon.checkNull(rs1.getString("CALL_LOG_BY_GENERAL_TYPE_ID")));
					callCenterDetailVO.setLoggedBy(TTKCommon.checkNull(rs1.getString("LOGGED_BY")));
					if(rs1.getString("TPA_OFFICE_SEQ_ID") != null){
						callCenterDetailVO.setOfficeSeqID(new Long(rs1.getLong("TPA_OFFICE_SEQ_ID")));
					}//end of if(rs1.getString("TPA_OFFICE_SEQ_ID") != null)

					callCenterDetailVO.setOfficeName(TTKCommon.checkNull(rs1.getString("OFFICE_NAME")));
					callCenterDetailVO.setFraudYN(TTKCommon.checkNull(rs1.getString("POSSIBILITY_OF_FRAUD_YN")));
					callCenterDetailVO.setStatusTypeID(TTKCommon.checkNull(rs1.getString("ACTION_GENERAL_TYPE_ID")));
					callCenterDetailVO.setCallAnswerTypeID(TTKCommon.checkNull(rs1.getString("CALL_ANS_TYPE_ID")));
					callCenterDetailVO.setAnswerDesc(TTKCommon.checkNull(rs1.getString("CALL_ANS_DESCRIPTION")));
					callCenterDetailVO.setCallRemarks(TTKCommon.checkNull(rs1.getString("CALL_REMARKS")));
					callCenterDetailVO.setInsScheme(TTKCommon.checkNull(rs1.getString("INS_SCHEME")));
					callCenterDetailVO.setCertificateNo(TTKCommon.checkNull(rs1.getString("CERTIFICATE_NO")));
					callCenterDetailVO.setInsCustCode(TTKCommon.checkNull(rs1.getString("INS_CUSTOMER_CODE")));
					callCenterDetailVO.setCreditCardNbr(TTKCommon.checkNull(rs1.getString("CREDITCARD_NO")));
					callCenterDetailVO.setTemplateName(TTKCommon.checkNull(rs1.getString("TEMPLATE_NAME")));

					if(rs1.getString("CONTACT_SEQ_ID") != null){
						callCenterDetailVO.setContactSeqID(new Long(rs1.getLong("CONTACT_SEQ_ID")));
					}//end of if(rs1.getString("CONTACT_SEQ_ID") != null)

					callCenterDetailVO.setRecordedBy(TTKCommon.checkNull(rs1.getString("CALL_LOGGED_BY_USER")));

					if(rs1.getString("CALL_RECORDED_DATE") != null){
						callCenterDetailVO.setLogRecordDate(new Date(rs1.getTimestamp("CALL_RECORDED_DATE").getTime()));
						callCenterDetailVO.setLogRecordTime(TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("CALL_RECORDED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("CALL_RECORDED_DATE").getTime())).split(" ")[1]:"");
						callCenterDetailVO.setLogRecordDay(TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("CALL_RECORDED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("CALL_RECORDED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(rs1.getString("CALL_RECORDED_DATE") != null)
					
					callCenterDetailVO.setProdPolicySeqId(rs1.getInt("PROD_POLICY_SEQ_ID"));
					
					if(rs1.getString("POLICY_GROUP_SEQ_ID") != null){
						callCenterDetailVO.setPolicyGrpSeqID(new Long(rs1.getLong("POLICY_GROUP_SEQ_ID")));
					}//end of if(rs1.getString("POLICY_GROUP_SEQ_ID") != null)

					if(rs1.getString("CLAIM_INTIMATION_SEQ_ID") != null){
						claimIntimationVO.setIntimationSeqID(new Long(rs1.getLong("CLAIM_INTIMATION_SEQ_ID")));
					}//end of if(rs1.getString("CLAIM_INTIMATION_SEQ_ID") != null)

					if(rs1.getString("ESTIMATED_AMOUNT") != null){
						claimIntimationVO.setEstimatedAmt(new BigDecimal(rs1.getString("ESTIMATED_AMOUNT")));
					}//end of if(rs1.getString("ESTIMATED_AMOUNT") != null)

					if(rs1.getString("LIKELY_DATE_OF_HOSPITALISATION") != null){
						claimIntimationVO.setHospitalizationDate(new Date(rs1.getTimestamp("LIKELY_DATE_OF_HOSPITALISATION").getTime()));
						claimIntimationVO.setHospitalizationTime(TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("LIKELY_DATE_OF_HOSPITALISATION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("LIKELY_DATE_OF_HOSPITALISATION").getTime())).split(" ")[1]:"");
						claimIntimationVO.setHospitalizationDay(TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("LIKELY_DATE_OF_HOSPITALISATION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("LIKELY_DATE_OF_HOSPITALISATION").getTime())).split(" ")[2]:"");
					}//end of if(rs1.getString("LIKELY_DATE_OF_HOSPITALISATION") != null)

					claimIntimationVO.setAilmentDesc(TTKCommon.checkNull(rs1.getString("AILMENT_DESCRIPTION")));
					claimIntimationVO.setHospitalName(TTKCommon.checkNull(rs1.getString("HOSPITAL_NAME")));
					claimIntimationVO.setHospitalAaddress(TTKCommon.checkNull(rs1.getString("HOSPITAL_ADDRESS")));
					callCenterDetailVO.setCallbackYN(TTKCommon.checkNull(rs1.getString("CALL_BCK_YN")));//INTx
					callCenterDetailVO.setClaimIntimationVO(claimIntimationVO);
				}//end of while(rs1.next())
			}//end of if(rs1 != null)

			if(rs2 != null){
				while(rs2.next()){
					callcenterListVO = new CallCenterListVO();
					callcenterListVO.setLogNumber(TTKCommon.checkNull(rs2.getString("CALL_LOG_NUMBER")));

					if(rs2.getString("CALL_RECORDED_DATE") != null){
						callcenterListVO.setLogDate(new Date(rs2.getTimestamp("CALL_RECORDED_DATE").getTime()));
						callcenterListVO.setLogTime(TTKCommon.getFormattedDateHour(new Date(rs2.getTimestamp("CALL_RECORDED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs2.getTimestamp("CALL_RECORDED_DATE").getTime())).split(" ")[1]:"");
						callcenterListVO.setLogDay(TTKCommon.getFormattedDateHour(new Date(rs2.getTimestamp("CALL_RECORDED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs2.getTimestamp("CALL_RECORDED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(rs2.getString("CALL_RECORDED_DATE") != null)

					callcenterListVO.setRecordedBy(TTKCommon.checkNull(rs2.getString("CALL_LOGGED_BY_USER")));
					callcenterListVO.setStatusDesc(TTKCommon.checkNull(rs2.getString("STATUS")));
					callcenterListVO.setStatusTypeID(TTKCommon.checkNull(rs2.getString("ACTION_GENERAL_TYPE_ID")));

					if(TTKCommon.checkNull(rs2.getString("STATUS")).equals("Closed")){
						callCenterDetailVO.setStatusDesc(TTKCommon.checkNull(rs2.getString("STATUS")));
					}//end of if(TTKCommon.checkNull(rs2.getString("STATUS")).equals("Closed"))

					callcenterListVO.setDescription(TTKCommon.checkNull(rs2.getString("CALL_REMARKS")));
					callcenterListVO.setChildCallContent(TTKCommon.checkNull(rs2.getString("CALL_CONTENT")));
					callcenterListVO.setChildMobileNbr(TTKCommon.checkNull(rs2.getString("MOBILE_NO")));
					callcenterListVO.setChildCallType(TTKCommon.checkNull(rs2.getString("CALL_TYPE")));
					callcenterListVO.setEscalationYN(TTKCommon.checkNull(rs2.getString("CALL_ESCALATION")));//INTx
					alCallCenterList.add(callcenterListVO);
				}//end of while(rs2.next())
				callCenterDetailVO.setCallCenterListVO(alCallCenterList);
			}//end of if(rs2 != null)
			rs2.close();
			rs1.close();
			cStmtObject.close();
			conn.close();
    		return callCenterDetailVO;
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "callcenter");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "callcenter");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the second result set
			{
				try
				{
					if (rs2 != null) rs2.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Second Resultset in CallCenterDAOImpl getCallDetail()",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if second result set is not closed, control reaches here. Try closing the first resultset now.
				{
					try{
						if (rs1 != null) rs1.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the First Resultset in CallCenterDAOImpl getCallDetail()",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if first result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in CallCenterDAOImpl getCallDetail()",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in CallCenterDAOImpl getCallDetail()",sqlExp);
								throw new TTKException(sqlExp, "callcenter");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of finally
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs2 = null;
				rs1 = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getCallDetail(long lngShortfallSeqID,long lngUserSeqID,long lngPreauthSeqID)

    /**
	 * This method returns the HashMap,which contains the Reasons related to Callertype and Category
	 * @return HashMap contains Reasons related to Callertype and Category
	 * @exception throws TTKException
	 */
    public HashMap getCategoryInfo() throws TTKException {
    	Connection conn = null;
    	PreparedStatement pStmt1 = null;
     	PreparedStatement pStmt2 = null;
     	PreparedStatement pStmt3 = null;
    	ResultSet rs1 = null;
    	ResultSet rs2 = null;
    	ResultSet rs3 = null;
    	CacheObject cacheObject = null;
    	HashMap<Object,Object> hmReasonList = null;
    	ArrayList<Object> alReasonList = null;
    	String strCallerTypeID = "";
    	String strCallCategoryTypeID = "";
    	String strCallerCategoryType = "";
    	try{
    		conn = ResourceManager.getConnection();
    		pStmt1=conn.prepareStatement(strCallerTypeList);
    		rs1= pStmt1.executeQuery();
    		pStmt2=conn.prepareStatement(strCallCategoryTypeList);
    		rs2= pStmt2.executeQuery();
    		pStmt3 = conn.prepareStatement(strReasonList);
    		if(rs1 != null && rs2 != null){
    			while(rs1.next() && rs2.next()){
    				if(hmReasonList == null){
    					hmReasonList=new HashMap<Object,Object>();
    				}//end of if(hmReasonList == null)
    					
    				strCallerTypeID = TTKCommon.checkNull(rs1.getString("CALLER_TYPE_ID"));
    				strCallCategoryTypeID = TTKCommon.checkNull(rs2.getString("CATEGORY_TYPE_ID"));
    				strCallerCategoryType = strCallerTypeID.concat(strCallCategoryTypeID);
    				pStmt3.setString(1,strCallerTypeID);
    				pStmt3.setString(2,strCallCategoryTypeID);
    	    		rs3= pStmt3.executeQuery();
    	    		alReasonList = null;
    	    		if(rs3 != null){
    	    			while(rs3.next()){
    	    				cacheObject = new CacheObject();
    	    				if(alReasonList == null)
    	    				{
    							alReasonList = new ArrayList<Object>();
    	    				}//end of if(alReasonList == null)
    	    				cacheObject.setCacheId(TTKCommon.checkNull(rs3.getString("CALL_RSON_TYPE_ID")));
    						cacheObject.setCacheDesc(TTKCommon.checkNull(rs3.getString("CALL_RSON_DESCRIPTION")));
    						alReasonList.add(cacheObject);
    	    			}//end of while(rs3.next())
    	    		}//end of if(rs3 != null)
    	    		hmReasonList.put(strCallerCategoryType,alReasonList);
    	    		if (rs3 != null){
                      rs3.close(); 
                    }//end of if (rs3 != null)
                    rs3=null;
                }//end of while(rs1.next() && rs2.next())
    		}//end of if(rs1 != null && rs2 != null)
    		if (pStmt3 != null){
                pStmt3.close();
            }//end of if (pStmt3 != null)
    		pStmt3 = null;
    		return hmReasonList;
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "callcenter");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "callcenter");
    	}//end of catch (Exception exp)
    	finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the third result set
			{
				try
				{
					if (rs3 != null) rs3.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Third Resultset in CallCenterDAOImpl getCategoryInfo()",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if third result set is not closed, control reaches here. Try closing the second resultset now.
				{
					try
					{
						if (rs2 != null) rs2.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Second Resultset in CallCenterDAOImpl getCategoryInfo()",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if Second Resultset is not closed, control reaches here. Try closing the first Resultset now.
					{
						try
						{
							if(rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the first Resultset in CallCenterDAOImpl getCategoryInfo()",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
						finally // Even if first Resultset is not closed, control reaches here. Try closing the third Statement now.
						{
							try
							{
								if(pStmt3 != null) pStmt3.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the third Statement in CallCenterDAOImpl getCategoryInfo()",sqlExp);
								throw new TTKException(sqlExp, "callcenter");
							}//end of catch (SQLException sqlExp)
							finally // Even if third Statement is not closed, control reaches here. Try closing the Second Statement now.
							{
								try{
									if(pStmt2 != null) pStmt2.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Second Statement in CallCenterDAOImpl getCategoryInfo()",sqlExp);
									throw new TTKException(sqlExp, "callcenter");
								}//end of catch (SQLException sqlExp)
								finally{ // Even if second Statement is not closed, control reaches here. Try closing the first Statement now.
									try{
										if(pStmt1 != null) pStmt1.close();
									}
									catch (SQLException sqlExp)
									{
										log.error("Error while closing the First Statement in CallCenterDAOImpl getCategoryInfo()",sqlExp);
										throw new TTKException(sqlExp, "callcenter");
									}//end of catch (SQLException sqlExp)
									finally{ // Even if first Statement is not closed, control reaches here. Try closing the Connection now.
										try{
											if(conn != null) conn.close();
										}//end of try
										catch (SQLException sqlExp)
										{
											log.error("Error while closing the Connection in CallCenterDAOImpl getCategoryInfo()",sqlExp);
											throw new TTKException(sqlExp, "callcenter");
										}//end of catch (SQLException sqlExp)
									}//end of finally Connection Close
								}//end of finally
							}//end of finally
						}//end of finally
					}//end of finally 
				}//end of finally
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs3 = null;
	    		rs2 = null;
	            rs1 = null;
	            pStmt3 = null;
	            pStmt2 = null;
	            pStmt1 = null;
	            conn = null;
			}//end of finally
		}//end of finally
    }//end of getCategoryInfo()

    /**
	 * This method returns the HashMap,which contains the Reasons related to CategoryType
	 * @return HashMap contains Reasons related to CategoryType
	 * @exception throws TTKException
	 */
    public HashMap getCategoryReasonInfo() throws TTKException {
    	Connection conn = null;
    	PreparedStatement pStmt1 = null;
     	PreparedStatement pStmt2 = null;
    	ResultSet rs1 = null;
    	ResultSet rs2 = null;
    	CacheObject cacheObject = null;
    	HashMap<Object,Object> hmReasonList = null;
    	ArrayList<Object> alReasonList = null;
    	String strCategoryTypeId = "";
    	try{
    		conn = ResourceManager.getConnection();
    		pStmt1=conn.prepareStatement(strCallCategoryTypeList);
    		pStmt2 = conn.prepareStatement(strCategoryReasonList);
    		rs1= pStmt1.executeQuery();
    		if(rs1 != null){
    			while(rs1.next()){
    				if(hmReasonList == null)
    				{
    					hmReasonList = new HashMap<Object,Object>();
    				}//end of if(hmReasonList == null)
    				strCategoryTypeId = TTKCommon.checkNull(rs1.getString("CATEGORY_TYPE_ID"));
    				pStmt2.setString(1,strCategoryTypeId);
    				rs2=pStmt2.executeQuery();
    				alReasonList=null;
    				if (rs2!=null)
    				{
    					while(rs2.next()){
    						cacheObject = new CacheObject();
    						if(alReasonList == null){
    							alReasonList = new ArrayList<Object>();
    						}//end of if(alReasonList == null)
    							
    						cacheObject.setCacheId(TTKCommon.checkNull(rs2.getString("CALL_RSON_TYPE_ID")));
    						cacheObject.setCacheDesc(TTKCommon.checkNull(rs2.getString("CALL_RSON_DESCRIPTION")));
    						alReasonList.add(cacheObject);
    					}// end of inner while(rs2.next())
    				}//end of  inner if (rs2!=null)
    				hmReasonList.put(strCategoryTypeId,alReasonList);
    				if (rs2 != null){
                      rs2.close(); 
                    }//end of if (rs2 != null)
                    rs2=null;
                }//end of while(rs1.next())
    		}//end of if(rs1 != null)
    		if (pStmt2 != null){
                pStmt2.close();
            }//end of if (pStmt1 != null)
    		pStmt2 = null;
    		return hmReasonList;
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "callcenter");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "callcenter");
    	}//end of catch (Exception exp)
    	finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the second result set
			{
				try
				{
					if (rs2 != null) rs2.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Second Resultset in CallCenterDAOImpl getCategoryReasonInfo()",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if second result set is not closed, control reaches here. Try closing the first resultset now.
				{
					try{
						if (rs1 != null) rs1.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the First Resultset in CallCenterDAOImpl getCategoryReasonInfo()",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if first result set is not closed, control reaches here. Try closing the second statement now.
					{
						try
						{
							if (pStmt2 != null) pStmt2.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the second Statement in CallCenterDAOImpl getCategoryReasonInfo()",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
						finally // Even if second statement is not closed, control reaches here. Try closing the first statement now.
						{
							try{
								if (pStmt1 != null) pStmt1.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the first Statement in CallCenterDAOImpl getCategoryReasonInfo()",sqlExp);
								throw new TTKException(sqlExp, "callcenter");
							}//end of catch (SQLException sqlExp)
							finally{ // Even if first statement is not closed, control reaches here. Try closing the connection now.
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in CallCenterDAOImpl getCategoryReasonInfo()",sqlExp);
									throw new TTKException(sqlExp, "callcenter");
								}//end of catch (SQLException sqlExp)
							}//end of finally
						}//end of finally
					}//end of finally
				}//end of finally
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs2 = null;
	    		rs1 = null;
	    		pStmt2 = null;
	    		pStmt1 = null;
	    		conn = null;
			}//end of finally
		}//end of finally
    }//end of getCategoryReasonInfo()

    /**
	 * This method returns the HashMap,which contains the ReasonInfo,SubReasonInfo and AnswerInfo
	 * @return HashMap containing ReasonInfo,SubReasonInfo and AnswerInfo
	 * @exception throws TTKException
	 */
    public HashMap getReasonInfo() throws TTKException {
    	Connection conn = null;
    	PreparedStatement pStmt1 = null;
     	PreparedStatement pStmt2 = null;
     	ResultSet rs1 = null;
    	ResultSet rs2 = null;
    	CacheObject cacheObject = null;
    	HashMap<Object,Object> hmReasonList = null;
    	ArrayList<Object> alAnswerList = null;
    	String strReasonTypeId = "";
    	try{
    		conn = ResourceManager.getConnection();
    		pStmt1=conn.prepareStatement(strCallReasonList);
    		pStmt2 = conn.prepareStatement(strCallAnswerList);
    		rs1= pStmt1.executeQuery();
    		if(rs1 != null){
    			while(rs1.next()){
    				if(hmReasonList == null){
    					hmReasonList=new HashMap<Object,Object>();
    				}//end of if(hmReasonList == null)
    					
    				strReasonTypeId = TTKCommon.checkNull(rs1.getString("CALL_RSON_TYPE_ID"));
    				pStmt2.setString(1,strReasonTypeId);
    				rs2=pStmt2.executeQuery();
    				alAnswerList = null;
    				if (rs2!=null)
    				{
    					while(rs2.next()){
    						cacheObject = new CacheObject();
    						if(alAnswerList == null)
    						{
    							alAnswerList = new ArrayList<Object>();
    						}//end of if(alAnswerList == null)
    						cacheObject.setCacheId(TTKCommon.checkNull(rs2.getString("CALL_ANS_TYPE_ID")));
    						cacheObject.setCacheDesc(TTKCommon.checkNull(rs2.getString("CALL_ANS_DESCRIPTION")));
    						alAnswerList.add(cacheObject);
    					}// end of inner while(rs2.next())
    				}//end of  inner if (rs2!=null)
    				hmReasonList.put(strReasonTypeId,alAnswerList);
    				if (rs2 != null){
                      rs2.close(); 
                    }//end of if (rs2 != null)
                    rs2=null;
                }//end of ouiter while(rs1.next())
    		}//end of outer if(rs1 != null)
    		if (pStmt2 != null){
                pStmt2.close();
            }//end of if (pStmt2 != null)
    		pStmt2 = null;
    		return hmReasonList;
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "callcenter");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "callcenter");
    	}//end of catch (Exception exp)
    	finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the second result set
			{
				try
				{
					if (rs2 != null) rs2.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Second Resultset in CallCenterDAOImpl getReasonInfo()",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if second result set is not closed, control reaches here. Try closing the first resultset now.
				{
					try{
						if (rs1 != null) rs1.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the First Resultset in CallCenterDAOImpl getReasonInfo()",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if first result set is not closed, control reaches here. Try closing the second statement now.
					{
						try
						{
							if (pStmt2 != null) pStmt2.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the second Statement in CallCenterDAOImpl getReasonInfo()",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
						finally // Even if second statement is not closed, control reaches here. Try closing the first statement now.
						{
							try{
								if (pStmt1 != null) pStmt1.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the first Statement in CallCenterDAOImpl getReasonInfo()",sqlExp);
								throw new TTKException(sqlExp, "callcenter");
							}//end of catch (SQLException sqlExp)
							finally{ // Even if first statement is not closed, control reaches here. Try closing the connection now.
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in CallCenterDAOImpl getReasonInfo()",sqlExp);
									throw new TTKException(sqlExp, "callcenter");
								}//end of catch (SQLException sqlExp)
							}//end of finally
						}//end of finally
					}//end of finally
				}//end of finally
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs2 = null;
	    		rs1 = null;
	    		pStmt2 = null;
	    		pStmt1 = null;
	    		conn = null;
			}//end of finally
		}//end of finally
    }//end of getReasonInfo()
    
    /**
     * This method saves the Customer Care Call information
     * @param callCenterDetailVO CallCenterDetailVO contains Customer Care Call information
     * @return long value which contains Call Log Seq ID
     * @exception throws TTKException
     */
    public long saveCallDetail(CallCenterDetailVO callCenterDetailVO) throws TTKException {
    	long lngCallLogSeqID = 0;
    	ClaimIntimationVO claimIntimationVO = null;
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveCallDetails);
    		claimIntimationVO = callCenterDetailVO.getClaimIntimationVO();

			cStmtObject.setLong(CALL_LOG_SEQ_ID,0);
			cStmtObject.setString(CALLER_TYPE_GENERAL_TYPE_ID,callCenterDetailVO.getCallerTypeID());
			cStmtObject.setString(CALL_RCVD_THRU_GENERAL_TYPE_ID,callCenterDetailVO.getSourceTypeID());
			cStmtObject.setString(CALL_LOG_TYPE_GENERAL_TYPE_ID,callCenterDetailVO.getLogTypeID());
			cStmtObject.setString(CALLER_NAME,callCenterDetailVO.getCallerName());

			if(callCenterDetailVO.getMemberSeqID() != null){
				cStmtObject.setLong(MEMBER_SEQ_ID,callCenterDetailVO.getMemberSeqID());
			}//end of if(callCenterDetailVO.getMemberSeqID() != null)
			else{
				cStmtObject.setString(MEMBER_SEQ_ID,null);
			}//end of else
			
			if(callCenterDetailVO.getPolicySeqID() != null){
				cStmtObject.setLong(POLICY_SEQ_ID,callCenterDetailVO.getPolicySeqID());
			}//end of if(callCenterDetailVO.getPolicySeqID() != null)
			else{
				cStmtObject.setString(POLICY_SEQ_ID,null);
			}//end of else

			if(callCenterDetailVO.getCallerTypeID().equals("CTC")){
				cStmtObject.setString(POLICY_NUMBER,callCenterDetailVO.getCorPolicyNumber());
			}//end of if(callCenterDetailVO.getCallerTypeID().equals("CTC"))
			else{
				cStmtObject.setString(POLICY_NUMBER,callCenterDetailVO.getPolicyNumber());
			}//end of else

			cStmtObject.setString(TPA_ENROLLMENT_ID,callCenterDetailVO.getEnrollmentID());
			cStmtObject.setString(CLAIMANT_NAME,callCenterDetailVO.getClaimantName());

			if(callCenterDetailVO.getGroupRegnSeqID() != null){
				cStmtObject.setLong(GROUP_REG_SEQ_ID,callCenterDetailVO.getGroupRegnSeqID());
			}//end of if(callCenterDetailVO.getGroupRegnSeqID() != null)
			else{
				cStmtObject.setString(GROUP_REG_SEQ_ID,null);
			}//end of else

			if(callCenterDetailVO.getHospSeqID() != null){
				cStmtObject.setLong(HOSP_SEQ_ID,callCenterDetailVO.getHospSeqID());
			}//end of if(callCenterDetailVO.getHospSeqID() != null)
			else{
				cStmtObject.setString(HOSP_SEQ_ID,null);
			}//end of else

			if(callCenterDetailVO.getInsSeqID() != null){
				cStmtObject.setLong(INS_SEQ_ID,callCenterDetailVO.getInsSeqID());
			}//end of if(callCenterDetailVO.getInsSeqID() != null)
			else{
				cStmtObject.setString(INS_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(OFF_PHONE_NO_1,callCenterDetailVO.getOfficePhoneNbr());
			cStmtObject.setString(RES_PHONE_NO,callCenterDetailVO.getHomePhoneNbr());
			cStmtObject.setString(MOBILE_NO,callCenterDetailVO.getMobileNumber());
			cStmtObject.setString(PRIMARY_EMAIL_ID,callCenterDetailVO.getEmailID());
			cStmtObject.setString(CALL_CONTENT,callCenterDetailVO.getCallContent());
			cStmtObject.setString(CALL_RSON_TYPE_ID,callCenterDetailVO.getReasonTypeID());
			cStmtObject.setString(CALL_SUB_RSON_TYPE_ID,callCenterDetailVO.getSubReasonTypeID());
			cStmtObject.setString(CALL_CAT_GENERAL_TYPE_ID,callCenterDetailVO.getCategoryTypeID());
			cStmtObject.setString(CALL_SUB_CAT_GENERAL_TYPE_ID,callCenterDetailVO.getSubCategoryTypeID());
			cStmtObject.setString(PRIORITY_GENERAL_TYPE_ID,callCenterDetailVO.getPriorityTypeID());
			cStmtObject.setString(CALL_LOG_BY_GENERAL_TYPE_ID,callCenterDetailVO.getLoggedByTypeID());

			if(callCenterDetailVO.getOfficeSeqID() != null){
				cStmtObject.setLong(TPA_OFFICE_SEQ_ID,callCenterDetailVO.getOfficeSeqID());
			}//end of if(callCenterDetailVO.getOfficeSeqID() != null)
			else{
				cStmtObject.setString(TPA_OFFICE_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(POSSIBILITY_OF_FRAUD_YN,callCenterDetailVO.getFraudYN());

			if(claimIntimationVO != null){
				if(claimIntimationVO.getIntimationSeqID() != null){
					cStmtObject.setLong(CLAIM_INTIMATION_SEQ_ID,claimIntimationVO.getIntimationSeqID());
				}//end of if(claimIntimationVO.getIntimationSeqID() != null)
				else{
					cStmtObject.setLong(CLAIM_INTIMATION_SEQ_ID,0);
				}//end of else

				if(claimIntimationVO.getEstimatedAmt() != null){
					cStmtObject.setBigDecimal(ESTIMATED_AMOUNT,claimIntimationVO.getEstimatedAmt());
				}//end of if(claimIntimationVO.getEstimatedAmt() != null)
				else{
					cStmtObject.setString(ESTIMATED_AMOUNT,null);
				}//end of else

				if(claimIntimationVO.getHospitalizationDate() != null){
			    	cStmtObject.setTimestamp(LIKELY_DATE_OF_HOSPITALISATION,new Timestamp(TTKCommon.getOracleDateWithTime(claimIntimationVO.getDateOfHospitalization(),claimIntimationVO.getHospitalizationTime(),claimIntimationVO.getHospitalizationDay()).getTime()));
			    }//end of if(claimIntimationVO.getHospitalizationDate() != null)
			    else{
			    	cStmtObject.setTimestamp(LIKELY_DATE_OF_HOSPITALISATION, null);
			    }//end of else

				cStmtObject.setString(AILMENT_DESCRIPTION,claimIntimationVO.getAilmentDesc());
				cStmtObject.setString(HOSPITAL_NAME,claimIntimationVO.getHospitalName());
				cStmtObject.setString(HOSPITAL_ADDRESS,claimIntimationVO.getHospitalAaddress());
			}//end of if(claimIntimationVO != null)
			else{
				cStmtObject.setLong(CLAIM_INTIMATION_SEQ_ID,0);
				cStmtObject.setString(ESTIMATED_AMOUNT,null);
				cStmtObject.setTimestamp(LIKELY_DATE_OF_HOSPITALISATION, null);
				cStmtObject.setString(AILMENT_DESCRIPTION,null);
				if(callCenterDetailVO.getHospName()==null||callCenterDetailVO.getHospName().trim().equals(""))
				cStmtObject.setString(HOSPITAL_NAME,null);
				else
				cStmtObject.setString(HOSPITAL_NAME,callCenterDetailVO.getHospName());
				cStmtObject.setString(HOSPITAL_ADDRESS,null);
			}//end of else

			cStmtObject.setString(ACTION_GENERAL_TYPE_ID,callCenterDetailVO.getStatusTypeID());
			cStmtObject.setString(CALL_ANS_TYPE_ID,callCenterDetailVO.getCallAnswerTypeID());
			cStmtObject.setString(CALL_REMARKS,callCenterDetailVO.getCallRemarks());
			cStmtObject.setLong(CONTACT_SEQ_ID,callCenterDetailVO.getUpdatedBy());

			if(callCenterDetailVO.getLinkIdentifier().equals("ADD")){
				cStmtObject.setString(PARENT_CALL_LOG_SEQ_ID,null);
			}//end of if(callCenterDetailVO.getLinkIdentifier().equals("ADD"))
			else if(callCenterDetailVO.getLinkIdentifier().equals("EDIT")){
				cStmtObject.setLong(PARENT_CALL_LOG_SEQ_ID,callCenterDetailVO.getLogSeqID());
			}//end of else if(callCenterDetailVO.getLinkIdentifier().equals("EDIT"))

			cStmtObject.setString(CALL_LOG_NUMBER,callCenterDetailVO.getLogNumber());
			cStmtObject.setString(CHILD_CALL_CONTENT,callCenterDetailVO.getChildCallContent());
			cStmtObject.setString(CHILD_MOBILE_NO,callCenterDetailVO.getChildMobileNbr());
			cStmtObject.setString(INS_SCHEME,callCenterDetailVO.getInsScheme());
			cStmtObject.setString(CERTIFICATE_NO,callCenterDetailVO.getCertificateNo());
			cStmtObject.setString(INS_CUSTOMER_CODE,callCenterDetailVO.getInsCustCode());
			cStmtObject.setString(CREDITCARD_NO,callCenterDetailVO.getCreditCardNbr());
			cStmtObject.setString(EMPANEL_NUMBER,callCenterDetailVO.getEmpanelmentNbr());
			cStmtObject.setString(OFFICE_NAME,callCenterDetailVO.getOfficeName());
			cStmtObject.setString(GROUP_ID,callCenterDetailVO.getGroupID());
			cStmtObject.setString(GROUP_NAME,callCenterDetailVO.getGroupName());
			cStmtObject.setString(INS_COMP_CODE_NUMBER,callCenterDetailVO.getInsCompCodeNbr());
			cStmtObject.setString(INS_COMP_NAME,callCenterDetailVO.getInsCompName());
			cStmtObject.setLong(USER_SEQ_ID,callCenterDetailVO.getUpdatedBy());
			cStmtObject.setTimestamp(CLM_INT_DATE,new Timestamp(callCenterDetailVO.getIntimationDate().getTime()));
			
	    	//cStmtObject.setTimestamp(CLM_INT_DATE,new Timestamp(TTKCommon.getFormattedDate(callCenterDetailVO.getIntimationDate()).getTime()));

			//cStmtObject.setTimestamp(CLM_INT_DATE,callCenterDetailVO.getIntimationDate());//shortfall phase1
			cStmtObject.registerOutParameter(CALL_LOG_SEQ_ID,Types.BIGINT);
			cStmtObject.setString(CALLBACK_YN,callCenterDetailVO.getCallbackYN());
			cStmtObject.setString(CALL_TYPE,callCenterDetailVO.getCallType());
			cStmtObject.setString(ESCLATION_YN,callCenterDetailVO.getEscalationYN());
    		cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);
    		cStmtObject.execute();
			lngCallLogSeqID = cStmtObject.getLong(CALL_LOG_SEQ_ID);
		}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "callcenter");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "callcenter");
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
        			log.error("Error while closing the Statement in CallCenterDAOImpl saveCallDetail()",sqlExp);
        			throw new TTKException(sqlExp, "callcenter");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in CallCenterDAOImpl saveCallDetail()",sqlExp);
        				throw new TTKException(sqlExp, "callcenter");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "callcenter");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return lngCallLogSeqID;
    }//end of saveCallDetail(ShortfallVO shortfallVO,String strSelectionType)

    /**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deleteCall(ArrayList alDeleteList) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteCall);
            cStmtObject.setString(1, (String)alDeleteList.get(0));//CONCATENATED STRING OF  delete SEQ_IDS
			cStmtObject.setLong(2,(Long)alDeleteList.get(1));//ADDED_BY
			cStmtObject.registerOutParameter(3, Types.INTEGER);//ROWS_PROCESSED
			cStmtObject.execute();
			iResult = cStmtObject.getInt(3);//ROWS_PROCESSED
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "callcenter");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "callcenter");
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
        			log.error("Error while closing the Statement in CallCenterDAOImpl deleteCall()",sqlExp);
        			throw new TTKException(sqlExp, "callcenter");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in CallCenterDAOImpl deleteCall()",sqlExp);
        				throw new TTKException(sqlExp, "callcenter");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "callcenter");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
	}//end of deleteCall(ArrayList alDeleteList)

	/**
     * This method assigns the CallCenter information to the corresponding Department
     * @param callcenterAssignmentVO CallCenterAssignmentVO which contains CallCenter information to assign the corresponding Department
     * @return long value which contains Clarify Seq ID
     * @exception throws TTKException
     */
    public long assignDepartment(CallCenterAssignmentVO callcenterAssignmentVO) throws TTKException {
    	long lngClarifySeqID = 0;
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAssignDepartment);
			if(callcenterAssignmentVO.getClarifySeqID() != null){
				cStmtObject.setLong(1,callcenterAssignmentVO.getClarifySeqID());
			}//end of if(callcenterAssignmentVO.getClarifySeqID() != null)
			else{
				cStmtObject.setLong(1,0);
			}//end of else

            if(callcenterAssignmentVO.getLogSeqID() != null){
                cStmtObject.setLong(2,callcenterAssignmentVO.getLogSeqID());
            }//end of if(callcenterAssignmentVO.getLogSeqID() != null)
            else{
                cStmtObject.setString(2,null);
            }//end of else

			if(callcenterAssignmentVO.getOfficeSeqID() != null){
				cStmtObject.setLong(3,callcenterAssignmentVO.getOfficeSeqID());
			}//end of if(callcenterAssignmentVO.getOfficeSeqID() != null)
			else{
				cStmtObject.setString(3,null);
			}//end of else

			cStmtObject.setString(4,callcenterAssignmentVO.getDeptTypeID());

			cStmtObject.setString(5,callcenterAssignmentVO.getRemarks());
			cStmtObject.setLong(6,callcenterAssignmentVO.getUpdatedBy());
			cStmtObject.registerOutParameter(7,Types.INTEGER);
			cStmtObject.registerOutParameter(1,Types.BIGINT);
			cStmtObject.execute();
			lngClarifySeqID = cStmtObject.getLong(1);//CLARIFY_SEQ_ID
		}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "callcenter");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "callcenter");
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
        			log.error("Error while closing the Statement in CallCenterDAOImpl assignDepartment()",sqlExp);
        			throw new TTKException(sqlExp, "callcenter");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in CallCenterDAOImpl assignDepartment()",sqlExp);
        				throw new TTKException(sqlExp, "callcenter");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "callcenter");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return lngClarifySeqID;
    }//end of assignDepartment(CallCenterAssignmentVO callcenterAssignmentVO)

    /**
     * This method returns the CallCenterAssignmentVO which contains Assignment details
     * @param lngClarifySeqID long value contains Clarify Seq ID
     * @param lngUserSeqID long value contains Logged-in User Seq ID
     * @return CallCenterAssignmentVO object which contains Assignment details
     * @exception throws TTKException
     */
    public CallCenterAssignmentVO getAssignTo(long lngClarifySeqID,long lngUserSeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	CallCenterAssignmentVO callcenterAssignmentVO = null;
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectAssignTo);
			cStmtObject.setLong(1,lngClarifySeqID);
			cStmtObject.setLong(2,lngUserSeqID);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs=(java.sql.ResultSet)cStmtObject.getObject(3);

			if(rs != null){
				while(rs.next()){
					callcenterAssignmentVO = new CallCenterAssignmentVO();

					if(rs.getString("CALL_CLARIFY_SEQ_ID") != null){
						callcenterAssignmentVO.setClarifySeqID(new Long(rs.getLong("CALL_CLARIFY_SEQ_ID")));
					}//end of if(rs.getString("CALL_CLARIFY_SEQ_ID") != null)

					if(rs.getString("CALL_LOG_SEQ_ID") != null){
						callcenterAssignmentVO.setLogSeqID(new Long(rs.getLong("CALL_LOG_SEQ_ID")));
					}//end of if(rs.getString("CALL_LOG_SEQ_ID") != null)

					if(rs.getString("TPA_OFFICE_SEQ_ID") != null){
						callcenterAssignmentVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					}//end of if(rs.getString("TPA_OFFICE_SEQ_ID") != null)

					callcenterAssignmentVO.setDeptTypeID(TTKCommon.checkNull(rs.getString("DEPT_GENERAL_TYPE_ID")));
					callcenterAssignmentVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));
					callcenterAssignmentVO.setLogNumber(TTKCommon.checkNull(rs.getString("CALL_LOG_NUMBER")));
				}//end of while(rs.next())
			}//end of if(rs != null)
			return callcenterAssignmentVO;
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "callcenter");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "callcenter");
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
					log.error("Error while closing the Resultset in CallCenterDAOImpl getAssignTo()",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in CallCenterDAOImpl getAssignTo()",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in CallCenterDAOImpl getAssignTo()",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getAssignTo(long lngClarifySeqID,long lngUserSeqID)

    /**
     * This method returns the Arraylist of OfficeVO's  which contains TTK office detail
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of OfficeVO object which contains TTK office Details
     * @exception throws TTKException
     */
    public ArrayList getTTKOfficeList(ArrayList alSearchCriteria) throws TTKException
    {
        Collection<Object> alResultList = new ArrayList<Object>();
        Connection conn = null;
        CallableStatement cStmtObject=null;
        ResultSet rs = null;
        OfficeVO officeVO =null;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTTKOfficeList);
            cStmtObject.setString(1,(String)alSearchCriteria.get(1));
            cStmtObject.setString(2,(String)alSearchCriteria.get(2));
            cStmtObject.setString(3,(String)alSearchCriteria.get(3));
            cStmtObject.setString(4,(String)alSearchCriteria.get(4));
            cStmtObject.setLong(5,(Long)alSearchCriteria.get(0));
            cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(6);
            if(rs != null){
                while(rs.next()){
                    officeVO= new OfficeVO();
                    if(rs.getString("TPA_OFFICE_SEQ_ID")!=null){
                    	officeVO.setOfficeSeqId(TTKCommon.getLong(rs.getString("TPA_OFFICE_SEQ_ID")));
                    }//end of if(rs.getString("TPA_OFFICE_SEQ_ID")!=null)
                        
                    officeVO.setOfficeCode(TTKCommon.checkNull(rs.getString("OFFICE_CODE")));
                    officeVO.setOfficeName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));
                    alResultList.add(officeVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "callcenter");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "callcenter");
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
					log.error("Error while closing the Resultset in CallCenterDAOImpl getTTKOfficeList()",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in CallCenterDAOImpl getTTKOfficeList()",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in CallCenterDAOImpl getTTKOfficeList()",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getTTKOfficeList(ArrayList alSearchCriteria)
    
    public InputStream getPolicyTobFile(String seqId)throws TTKException{
		
		Connection conn = null;
		CallableStatement cStmtObject=null;
//		ResultSet rs = null;
		Blob blob	=	null;
		InputStream iStream	=	new ByteArrayInputStream(new String("").getBytes());
		try{
			conn = ResourceManager.getConnection();
	                 
	         cStmtObject = conn.prepareCall(strGetPolicyTobFile);
	       
	        cStmtObject.setString(1, seqId);
			cStmtObject.registerOutParameter(2,Types.BLOB);
			cStmtObject.execute();
			
		//	rs = (java.sql.ResultSet)cStmtObject.getObject(2);

			
				//if(rs != null){
				//	if(rs.next()){
					 blob	=	(BLOB) cStmtObject.getBlob(2);
					 iStream	=	blob.getBinaryStream();
					
					
					//}
				//	}//end of if(rs != null)
		    	//return (ArrayList<Object>)alMouList;
					 
	    }//end of try
	    catch (SQLException sqlExp) 
	    {
	        throw new TTKException(sqlExp, "callcenter");
	    }//end of catch (SQLException sqlExp)
		
		catch (NullPointerException nullExp)
		{
			TTKException expTTK = new TTKException();
			expTTK.setMessage("error.policyTobFile.notFound");
			throw expTTK;
		}
		
		
	    catch (Exception exp) 
	    {
	        throw new TTKException(exp, "callcenter");
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
	    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderDocs()",sqlExp);
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
	    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderDocs()",sqlExp);
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
	    return iStream;
 }
    
    public ArrayList<Object> getBenefitDetails(ArrayList<Object> alSearchCriteria) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ResultSet rs1=null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		boolean recors=false;
		ArrayList<Object> alRes=new ArrayList<>();
		Collection<Object> alResultList = new ArrayList<Object>();
		try{
			conn = ResourceManager.getConnection();
			
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetbenefitDetails);
			if(alSearchCriteria.get(0)!= null)cStmtObject.setLong(1,new Long((String) alSearchCriteria.get(0)));
			else cStmtObject.setString(1, null);
			if (alSearchCriteria.get(1)!= null)cStmtObject.setLong(2,new Long((String) alSearchCriteria.get(1)));
			else cStmtObject.setString(2, null);
			if(alSearchCriteria.get(2)!= null)cStmtObject.setString(3, (String) alSearchCriteria.get(2));
			else cStmtObject.setString(3, null);
			if(alSearchCriteria.get(3)!= null) cStmtObject.setLong(4,new Long((String)alSearchCriteria.get(3)));
			else cStmtObject.setString(4, null);
			if(alSearchCriteria.get(4)!= null) cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			else cStmtObject.setString(5, null);
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
		/*	cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(9,OracleTypes.CURSOR);*/
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(6);
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(7);
			/*rs2 = (java.sql.ResultSet)cStmtObject.getObject(8);
			rs3 = (java.sql.ResultSet)cStmtObject.getObject(9);*/
			if(rs != null){
				while(rs.next()){
					recors=true;	
					BenefitsDetailsVO benefitDetailVo=new BenefitsDetailsVO();
					benefitDetailVo.setBenefitName(TTKCommon.checkNull(rs.getString("CLAUSE_NAME")));
				//	benefitDetailVo.setSubBenefit(TTKCommon.checkNull(rs.getString("SUB_BENF_NAME")));
				
					benefitDetailVo.setStrConfigration(rs.getString("RULE_CONFIG")==null?"-":rs.getString("RULE_CONFIG"));
				//	benefitDetailVo.setCoverage(rs.getString("COND_NAME")==null?"-":rs.getString("COND_NAME"));
					benefitDetailVo.setCondition(rs.getString("COND_NAME")==null?"-":rs.getString("COND_NAME"));

					benefitDetailVo.setLimit(rs.getString("LIMIT")==null?"-":rs.getString("LIMIT"));
					benefitDetailVo.setCopay(rs.getString("COPAY_PERC")==null?"-":rs.getString("COPAY_PERC"));
					benefitDetailVo.setDeductible(rs.getString("DEDUCTABLE")==null?"-":rs.getString("DEDUCTABLE"));
					benefitDetailVo.setWaitingPeriod(rs.getString("WAITING_PERIOD")==null?"-":rs.getString("WAITING_PERIOD"));
				    benefitDetailVo.setSessionAllowed(rs.getString("SESSIONS_BENIFIT")==null?"-":rs.getString("SESSIONS_BENIFIT"));
					
					benefitDetailVo.setModeType(rs.getString("MODE_OF_BENIFIT")==null?"-":rs.getString("MODE_OF_BENIFIT"));
				//	benefitDetailVo.setOtherRemarks(rs.getString("OTHER_REMARKS"));
					benefitDetailVo.setLimitUtilised(rs.getString("UTILIZED_AMNT")==null?"-":rs.getString("UTILIZED_AMNT"));
					benefitDetailVo.setLimitAvailable(rs.getString("BALNC_AMNT")==null?"-":rs.getString("BALNC_AMNT"));
					alResultList.add(benefitDetailVo);
				}//end of while(rs.next())
			}//end of if(rs != null)
			//if(!recors)	alResultList.add(new BenefitDetailVo());
			
			alRes.add(alResultList);
			//recors=false;
			/*String sumInsured=null;
			if(rs2 != null){
				while(rs2.next()){
					recors=true;
					sumInsured = rs2.getString("sum_insured"); 
					alRes.add(rs2.getString("available_sum_insured"));
				}
			}*/
			
			if(rs1 != null){
				HashMap<String, String> map = new HashMap<String,String>();
				while(rs1.next()){
					map.put("enrollmentId", rs1.getString("tpa_enrollment_id"));
					map.put("policyIssueDate", rs1.getString("policy_issue_date"));
					map.put("policyNumber", rs1.getString("policy_number"));
					map.put("effectiveFromDate", rs1.getString("effective_from_date"));
					map.put("productCatTypeID", rs1.getString("PRODUCT_CAT_TYPE_ID"));
					map.put("effectiveToDate", rs1.getString("effective_to_date"));
					map.put("sumInsured", rs1.getString("SUM_INSURED"));
					map.put("utilizedsumInsured", rs1.getString("UTILISED_SUM_INSURED"));
					map.put("availablesumInsured", rs1.getString("AVAILABLE_SUM_INSURED"));
				}
			alRes.add(map);	
			}
			//if(!recors) alRes.add(null);
			//recors=false;
		/*	if(rs3!=null){
				while(rs3.next()){
					recors=true;
					alRes.add(rs3.getString("OVERALL_REMARKS"));
				}
			};*/
			 if(!recors) alRes.add(null);
			return alRes;
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "callcenter");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "callcenter");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
					if (rs1 != null) rs1.close();
					if (rs2 != null) rs2.close();
					if (rs3 != null) rs3.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in CallCenterDAOImpl getBenefitDetails",sqlExp);
					throw new TTKException(sqlExp, "callcenter");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in CallCenterDAOImpl getBenefitDetails",sqlExp);
						throw new TTKException(sqlExp, "callcenter");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in CallCenterDAOImpl getBenefitDetails",sqlExp);
							throw new TTKException(sqlExp, "callcenter");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "callcenter");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				rs1 =null;
				rs2 =null;
				rs3 =null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getBenefitDetails(ArrayList alSearchCriteria)
}//end of CallCenterDAOImpl