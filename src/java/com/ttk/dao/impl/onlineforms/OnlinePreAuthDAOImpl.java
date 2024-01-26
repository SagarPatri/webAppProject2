/**
 * @ (#) OnlinePreAuthDAOImpl.java Jul 19, 2007
 * Project 	     : TTK HealthCare Services
 * File          : OnlinePreAuthDAOImpl.java
 * Author        : Kishor kumar S H
 * Company       : RCS Technologies
 * Date Created  : 29th April 2015
 *
 * @author       :  Kishor kumar S H
 * Modified by   :  
 * Modified date :
 * Reason        :
 */

package com.ttk.dao.impl.onlineforms;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.common.security.Cache;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.common.CommonClosure;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.empanelment.LabServiceVO;
import com.ttk.dto.onlineforms.providerLogin.ServiceDetailsVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.DentalOrthoVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.DrugDetailsVO;
import com.ttk.dto.usermanagement.UserListVO;
//import com.ttk.dto.preauth.PreAuthVO;
//import com.ttk.dto.security.GroupVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ttk.business.onlineforms.OnlinePreAuthManager;
public class OnlinePreAuthDAOImpl implements BaseDAO, Serializable{

	private static Logger log = Logger.getLogger(OnlinePreAuthDAOImpl.class);
	private static final String strOnlinePreAuth	=	"onlinePreAuth";
	private static String vip_query="select c.vip_yn from tpa_enr_policy_member c where c.tpa_enrollment_id = ?";
	
   //intx
   private static final String strValidateEnrollID 		= "{CALL HOSP_DIAGNOSYS_PKG.check_enrollment_id(?,?)}";
   private static final String strGetMemberOnEnroll 	= "{CALL HOSP_DIAGNOSYS_PKG.get_mem_benifit_details(?,?,?)}";
   
//   private String strGetLabDetails						= "SELECT MC.MEDICAL_TYPE_ID,MC.ACTIVITY_CODE,MC.MEDICAL_DESCRIPTION,'NA' AS PRICE FROM APP.TPA_HOSP_PRESC_MEDICAL_CODE MC LEFT OUTER JOIN APP.TPA_HOSP_PRESCRIPTION_DETAILS PD ON (pd.medical_type_id=mc.medical_type_id) WHERE PD.PAT_INTIMATION_SEQ_ID=? AND MC.MEDICAL_TYPE_ID IN ";
//   private String strGetLabDetails						= 	"select MC.ACTIVITY_CODE,MC.MEDICAL_DESCRIPTION,'NA' AS PRICE from APP.TPA_HOSP_PRESC_MED_CODE MC where mc.activity_code in ";
   private String strGetLabDetails						= 	"{CALL HOSP_DIAGNOSYS_PKG.select_provider_activity (?,?,?,?)}";
   private static final String strSavePreAuth 			= "{CALL HOSP_DIAGNOSYS_PKG.SAVE_PAT_ONLINE_INTIMATION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
   private static final String strGetConsumableDetails	= "select ad.activity_code, tsd.service_name, ad.activity_seq_id from app.tpa_activity_details ad join app.tpa_service_details tsd on ad.service_seq_id=tsd.service_seq_id where ad.activity_description=?";
   private static final String strGetPreAuthDetails		= "{CALL HOSP_DIAGNOSYS_PKG.select_pat_intimation_details(?,?)}";
   private static final String strSaveConsumables		= "{CALL HOSP_DIAGNOSYS_PKG.add_consumable_details(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
   private static final String strGetConsumablesList	= "{CALL HOSP_DIAGNOSYS_PKG.get_consumable_details(?,?)}";
   private static final String strGetPharmacyDetails	= "SELECT D.ACTIVITY_CODE,D.ACTIVITY_DESCRIPTION,D.INGREDIENT_STRENGTH,D.DOSAGE_FORM_PACKAGE FROM APP.TPA_ACTIVITY_MASTER_DETAILS D WHERE D.ACTIVITY_DESCRIPTION= ?";
   private static final String strSaveDiagnosisDetails	= "{CALL HOSP_DIAGNOSYS_PKG.SAVE_DIAGNOSYS_DETAILS(?,?,?,?,?)}";
   private static final String strGetDiagDetails		= "{CALL AUTHORIZATION_PKG.SELECT_DIAGNOSYS_DETAILS (?,?,?)}";
   private static final String strGetIcdCode			= "{CALL AUTHORIZATION_PKG.SELECT_ICD (?,?,?,?)}";
   private static final String strDeleteDiagnosisDetails= "{CALL AUTHORIZATION_PKG.DELETE_DIAGNOSYS_DETAILS(?,?,?,?)}";
   private static final String strPreAuthorization 		= "{CALL AUTHORIZATION_PKG.SELECT_PAT_AUTH_DETAILS(?,?,?,?,?,?)}";
   private static final String strActivityCodeList 		= "{CALL HOSP_DIAGNOSYS_PKG.SELECT_ACTIVITY_LIST(?,?,?,?,?,?,?,?,?,?,?,?)}";
   private static final String strActivityCodeDetails	= "{CALL HOSP_DIAGNOSYS_PKG.SELECT_ACTIVITY_CODE(?,?,?,?,?,?,?)}";
   private static final String strDrugCodeList 			= "{CALL HOSP_DIAGNOSYS_PKG.SELECT_DDC_LIST (?,?,?,?,?,?,?,?,?,?)}";
   private static final String strSaveActivitiesDetails	= "{CALL HOSP_DIAGNOSYS_PKG.SAVE_ACTIVITY_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
   private static final String strSaveDrugDetails		= "{CALL HOSP_DIAGNOSYS_PKG.SAVE_DDC_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
   private static final String strDiagCodeList			= "{CALL HOSP_DIAGNOSYS_PKG.SELECT_DIAGNOSYS_LIST  (?,?,?)}";
   private static final String strSavePartialPreAuth 	= "{CALL HOSP_DIAGNOSYS_PKG.SAVE_PAT_ONLINE_INTIMATION1(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
   private static final String strSavePartnerPartialPreAuth 	= "{CALL ONLINE_PARTNER_LOGIN. PRE_AUTH_SUBMISSION_SAVE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
   private static final String strOnlinePreAuthDetails 	= "{CALL HOSP_DIAGNOSYS_PKG.SELECT_PAT_AUTH_DETAILS(?,?,?,?,?,?,?)}";
   private static final String strOnlinePartnerPreAuthDetails = "{CALL ONLINE_PARTNER_LOGIN.SELECT_PAT_AUTH_DETAILS(?,?,?,?,?,?,?,?)}";
   private static final String strDeleteExistingDataForPreAuth 	= "{CALL HOSP_DIAGNOSYS_PKG.DELETE_DAIG_ACT_PREV(?,?,?,?,?,?)}";
   private static final String strProviderUploadedDocsDBFile = "SELECT S.IMAGE_FILE FROM MOU_DOCS_INFO1 S WHERE S.MOU_DOC_SEQ_ID=?";
   private static final String strEncounterTypeList		=	"{CALL AUTHORIZATION_PKG.SELECT_ENCOUNTER_TYPES(?,?)}";
   private static final String strSavePreAuthDentalDetails = "{CALL AUTHORIZATION_PKG.SAVE_ORTHODONTIC_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
   private static final String strGetAppealComments = "{CALL HOSP_DIAGNOSYS_PKG.SAVE_APPEAL_REMARK(?,?)}";
   private static final String strDeleteProviderDetail = "{CALL HOSP_DIAGNOSYS_PKG.delete_preauth_dtl(?,?,?)}";
	 //intX
    /**
     * This method returns the MemberSeqID, to validate a Enrollment ID
     * @param strGroupID It contains the Group ID
     * @return ArrayList object which contains Policy No's corresponding toGroup ID
     * @exception throws TTKException
     */
    public String getValidateVidalId(String vidalId) throws TTKException {
    	ResultSet rs = null;

        ArrayList<Object> alcacheObj = new ArrayList<Object>();
        CacheObject cacheObject = null;
        Connection conn = null;
        PreparedStatement pStmt = null;
        OracleCallableStatement oCstmt = null;
        
        String flag="";
       
        try{
            conn = ResourceManager.getConnection();
            //oCstmt=conn.prepareStatement(strValidateEnrollID);
            
		
		oCstmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strValidateEnrollID);
		oCstmt.setString(1, vidalId);//vidalId
		oCstmt.registerOutParameter(2,Types.VARCHAR);
		
		oCstmt.execute();
		
		flag		=	oCstmt.getString(2);
		

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
    				log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl getValidateEnrollId()",sqlExp);
    				throw new TTKException(sqlExp, "onlineforms");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
    			{
    				try
    				{
    					if (oCstmt != null) oCstmt.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getValidateEnrollId()",sqlExp);
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
    						log.error("Error while closing the Connection in OnlinePreAuthDAOImpl getValidateEnrollId()",sqlExp);
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
    			oCstmt=null;
    			conn = null;
    		}//end of finally
    	}//end of finally
		return flag;
    }//end of getSelectedPolicyNumber(String strGroupID)

    
    /*
     * Method to get the Member details, Member Eligibility and other details from Enrollment iD
     */

    	public CashlessDetailVO geMemberDetailsOnEnrollId(String EnrollId,String benifitTypeVal) throws TTKException {
    		CashlessDetailVO cashlessDetailVO	= null;
    		Connection conn = null;
    		CallableStatement cStmtObject=null;
    		ResultSet rs	=	null;
    		try{
    			conn = ResourceManager.getConnection();
    				
    				
    				cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strGetMemberOnEnroll);
    				cStmtObject.setString(1,EnrollId);//EnrollId
    				cStmtObject.setString(2,benifitTypeVal);//benifitTypeVal
    				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
    				cStmtObject.execute();
    				rs = (java.sql.ResultSet)cStmtObject.getObject(3);
    			if(rs != null)
    			{
    				if(rs.next())
    				{
    					cashlessDetailVO=new CashlessDetailVO();
    					cashlessDetailVO.setMemberName(rs.getString("MEM_NAME"));
    					cashlessDetailVO.setAge(rs.getLong("MEM_AGE"));
    					cashlessDetailVO.setGender(rs.getString("GENDER"));
    					cashlessDetailVO.setPayer(rs.getString("PAYER_NAME"));
    					cashlessDetailVO.setEligibility(rs.getString("ELIGIBILITY"));
    					cashlessDetailVO.setDeductable(rs.getString("DEDUCTIBLE"));
    					cashlessDetailVO.setCoPay(rs.getString("CO_PARTICIPATION"));
    					//cashlessDetailVO.setApplProcudure(rs.getString("APPLICABLE_PROCEDURE"));
    					cashlessDetailVO.setExclusions(rs.getString("EXCLUSIONS"));
    					cashlessDetailVO.setInsurredName(rs.getString("INSURED_NAME"));
    					cashlessDetailVO.setMemberSeqID(rs.getLong("MEMBER_SEQ_ID"));
    					cashlessDetailVO.setInsuranceSeqID(rs.getLong("INS_SEQ_ID"));
    					
    				}//end of while(rs.next())
    			}//end of if(rs != null)
    			
    			
    				//lResult++;
    		}//end of try
    		catch (SQLException sqlExp)
    		{
    			throw new TTKException(sqlExp, "user");
    		}//end of catch (SQLException sqlExp)
    		
    		catch (Exception exp)
    		{
    			throw new TTKException(exp, "user");
    		}//end of catch (Exception exp)
    		
    		finally
    		{
    	    	/* Nested Try Catch to ensure resource closure */ 
    	    	try // First try closing the Statement
    	    	{
    	    		try
    				{
    					if (rs != null) rs.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl getValidateEnrollId()",sqlExp);
    					throw new TTKException(sqlExp, "onlineforms");
    				}//end of catch (SQLException sqlExp)
    	    		
    	    		try
    	    		{
    	    			if (cStmtObject != null) cStmtObject.close();
    	    		}//end of try
    	    		catch (SQLException sqlExp)
    	    		{
    	    			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl saveContact()",sqlExp);
    	    			throw new TTKException(sqlExp, "user");
    	    		}//end of catch (SQLException sqlExp)
    	    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    	    		{
    	    			try
    	    			{
    	    				if(conn != null) conn.close();
    	    			}//end of try
    	    			catch (SQLException sqlExp)
    	    			{
    	    				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl saveContact()",sqlExp);
    	    				throw new TTKException(sqlExp, "user");
    	    			}//end of catch (SQLException sqlExp)
    	    		}//end of finally Connection Close
    	    	}//end of try
    	    	catch (TTKException exp)
    	    	{
    	    		throw new TTKException(exp, "user");
    	    	}//end of catch (TTKException exp)
    	    	finally // Control will reach here in anycase set null to the objects 
    	    	{
    	    		rs=null;
    	    		cStmtObject = null;
    	    		conn = null;
    	    	}//end of finally
    		}//end of finally
    		return cashlessDetailVO;
    	}//end of geMemberDetailsOnEnrollId()
    	
    	
    	/*
         * Method to get the Lab details which are selected
         */

        	public ArrayList getSelectedLabDetails(String selectedLabIds,Long PAT_INTIMATION_SEQ_ID,String enrollId) throws TTKException {
        		Connection conn 				= 	null;
        		CallableStatement cStmtObject	=	null;
        		ResultSet rs					=	null;
                PreparedStatement pStmt 		= 	null;
                LabServiceVO labServiceVO		=	null;
                ArrayList<LabServiceVO> alLabs				=	new ArrayList<LabServiceVO>();
        		try{
                    conn = ResourceManager.getConnection();
                    //strGetLabDetails	=	strGetLabDetails+" ( "+selectedLabIds+" )";
                    pStmt=conn.prepareStatement(strGetLabDetails);
                    //pStmt.setLong(1, PAT_INTIMATION_SEQ_ID);
                    
                    
                    //*************************************
            		cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strGetLabDetails);
    				cStmtObject.setString(1,selectedLabIds);//selectedLabIds
    				cStmtObject.setString(2,enrollId);//EnrollId
    				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
    				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
    				cStmtObject.execute();
    				rs = (java.sql.ResultSet)cStmtObject.getObject(3);
    			if(rs != null)
    			{
    				while(rs.next())
    				{
    					labServiceVO = new LabServiceVO();
                    	//labServiceVO.setMedicalTypeId(TTKCommon.checkNull(rs.getString("MEDICAL_TYPE_ID")));
                        labServiceVO.setActivityCode(TTKCommon.checkNull(rs.getString("ACTIVITY_CODE")));
                        labServiceVO.setShortDesc(TTKCommon.checkNull(rs.getString("MEDICAL_DESCRIPTION")));
                       // labServiceVO.setPrice(TTKCommon.checkNull(rs.getString("PRICE")));
                        alLabs.add(labServiceVO);
    					
    				}//end of while(rs.next())
    			}//end of if(rs != null)
                    //*************************************
    			
                   /* rs = pStmt.executeQuery();
                    if(rs != null){
                        while(rs.next()){
                        	labServiceVO = new LabServiceVO();
                        	//labServiceVO.setMedicalTypeId(TTKCommon.checkNull(rs.getString("MEDICAL_TYPE_ID")));
                            labServiceVO.setActivityCode(TTKCommon.checkNull(rs.getString("ACTIVITY_CODE")));
                            labServiceVO.setShortDesc(TTKCommon.checkNull(rs.getString("MEDICAL_DESCRIPTION")));
                            labServiceVO.setPrice(TTKCommon.checkNull(rs.getString("PRICE")));
                            alLabs.add(labServiceVO);
                        }//end of while(rs.next())
                    }*///end of if(rs != null)
                    return alLabs;
                }//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "user");
        		}//end of catch (SQLException sqlExp)
        		
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "user");
        		}//end of catch (Exception exp)
        		
        		
        		finally
        		{
        	    	/* Nested Try Catch to ensure resource closure */ 
        	    	try // First try closing the Statement
        	    	{
        	    		try
        				{
        					if (rs != null) rs.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthList()",sqlExp);
        					throw new TTKException(sqlExp, "preauth");
        				}//end of catch (SQLException sqlExp)
        	    		
        	    		try
        	    		{
        	    			if (cStmtObject != null) cStmtObject.close();
        	    		}//end of try
        	    		catch (SQLException sqlExp)
        	    		{
        	    			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl saveContact()",sqlExp);
        	    			throw new TTKException(sqlExp, "user");
        	    		}//end of catch (SQLException sqlExp)
        	    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        	    		{
        	    			try
        	    			{
        	    				if(conn != null) conn.close();
        	    			}//end of try
        	    			catch (SQLException sqlExp)
        	    			{
        	    				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl saveContact()",sqlExp);
        	    				throw new TTKException(sqlExp, "user");
        	    			}//end of catch (SQLException sqlExp)
        	    		}//end of finally Connection Close
        	    	}//end of try
        	    	catch (TTKException exp)
        	    	{
        	    		throw new TTKException(exp, "user");
        	    	}//end of catch (TTKException exp)
        	    	finally // Control will reach here in anycase set null to the objects 
        	    	{
        	    		rs=null;
        	    		cStmtObject = null;
        	    		conn = null;
        	    	}//end of finally
        		}//end of finally
        	}//end of getSelectedLabDetails(String selectedLabIds
	

        	
        	/*
             * Method to get the Consumable Details
             */

    	public LabServiceVO getConsumableDetails(String consDesc) throws TTKException {
    		Connection conn 				= 	null;
    		CallableStatement cStmtObject	=	null;
    		ResultSet rs					=	null;
            PreparedStatement pStmt 		= 	null;
            LabServiceVO labServiceVO		=	null;
            ArrayList<LabServiceVO> alLabs				=	new ArrayList<LabServiceVO>();
    		try{
                conn = ResourceManager.getConnection();
                pStmt=conn.prepareStatement(strGetConsumableDetails);
                pStmt.setString(1, consDesc);
                rs = pStmt.executeQuery();
                if(rs != null){
                    if(rs.next()){
                    	labServiceVO = new LabServiceVO();
                        labServiceVO.setActivityCode(TTKCommon.checkNull(rs.getString("activity_code")));
                        labServiceVO.setServiceName(TTKCommon.checkNull(rs.getString("service_name")));
                        labServiceVO.setActivitySeqId(TTKCommon.getLong(rs.getString("activity_seq_id")));
                        alLabs.add(labServiceVO);
                    }//end of while(rs.next())
                }//end of if(rs != null)
                return labServiceVO;
            }//end of try
    		catch (SQLException sqlExp)
    		{
    			throw new TTKException(sqlExp, "user");
    		}//end of catch (SQLException sqlExp)
    		
    		catch (Exception exp)
    		{
    			throw new TTKException(exp, "user");
    		}//end of catch (Exception exp)
    		
    		
    		finally
    		{
    	    	/* Nested Try Catch to ensure resource closure */ 
    	    	try // First try closing the Statement
    	    	{
    	    		try
    				{
    					if (rs != null) rs.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthList()",sqlExp);
    					throw new TTKException(sqlExp, "preauth");
    				}//end of catch (SQLException sqlExp)
    	    		
    	    		try
    	    		{
    	    			if (cStmtObject != null) cStmtObject.close();
    	    		}//end of try
    	    		catch (SQLException sqlExp)
    	    		{
    	    			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl saveContact()",sqlExp);
    	    			throw new TTKException(sqlExp, "user");
    	    		}//end of catch (SQLException sqlExp)
    	    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    	    		{
    	    			try
    	    			{
    	    				if(conn != null) conn.close();
    	    			}//end of try
    	    			catch (SQLException sqlExp)
    	    			{
    	    				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl saveContact()",sqlExp);
    	    				throw new TTKException(sqlExp, "user");
    	    			}//end of catch (SQLException sqlExp)
    	    		}//end of finally Connection Close
    	    	}//end of try
    	    	catch (TTKException exp)
    	    	{
    	    		throw new TTKException(exp, "user");
    	    	}//end of catch (TTKException exp)
    	    	finally // Control will reach here in anycase set null to the objects 
    	    	{
    	    		rs=null;
    	    		cStmtObject = null;
    	    		conn = null;
    	    	}//end of finally
    		}//end of finally
    	}//end of getConsumableDetails(String consDesc)
        	
        	/**
             * This method saves the Pre Auth Information
             * @param CashlessDetailVO cashlessDetailVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public String[] savePreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,UserSecurityProfile userSecurityProfile,String benifitTypeVal,String SubmitYN,String loginFlag,HttpServletRequest request) throws TTKException
            {
            	//int iResult = 0;
                Connection conn = null;
                CallableStatement cStmtObject=null;
                Long lPreAuthSeqId= null;
                int rowsProcessed	=	0;
                String[] resArr	=	new String[2];
                OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
                HttpSession session	=	request.getSession();
                try {
                	
                    conn = ResourceManager.getConnection();
                    cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePreAuth);
                    if(cashlessDetailVO.getPreAuthSeqID()==null)
                    	cStmtObject.setLong(1, 0);
                    else
                    	cStmtObject.setLong(1, cashlessDetailVO.getPreAuthSeqID());
                    cStmtObject.setLong(2, cashlessDetailVO.getMemberSeqID());//member Seq Id

                    if(cashlessDetailVO.getTreatmentDate()!=null)
                    	cStmtObject.setTimestamp(3,new Timestamp(cashlessDetailVO.getTreatmentDate().getTime()));//TreatmentDate
                    else
                    	cStmtObject.setDate(3, null);
                    
                    if(cashlessDetailVO.getDischargeDate()!=null)
                    	cStmtObject.setTimestamp(4,new Timestamp(cashlessDetailVO.getDischargeDate().getTime()));//TreatmentDate
                    else
                    	cStmtObject.setDate(4, null);
                    String temp	=	"";
                    temp	=	cashlessDetailVO.getClinicianName();
                    if(temp!=null || temp!="")
                    	if(temp.indexOf("::")!=-1)
                    		temp	=	temp.substring(0, temp.indexOf("::"));
                    cStmtObject.setString(5, temp);//clinician Name
                    cStmtObject.setString(6, cashlessDetailVO.getClinicianId());
                    cStmtObject.setString(7, cashlessDetailVO.getSpeciality());
                    cStmtObject.setString(8, cashlessDetailVO.getConsultation());
                    
                    cStmtObject.setString(7, cashlessDetailVO.getPresentingComplaints());
                    cStmtObject.setString(8, cashlessDetailVO.getPastHistory());
                    cStmtObject.setString(9, cashlessDetailVO.getAilmentDurationText());
                    cStmtObject.setString(10, cashlessDetailVO.getAilmentDuration());
                    cStmtObject.setString(11, cashlessDetailVO.getIllnessDurationText());
                    cStmtObject.setString(12, cashlessDetailVO.getIllnessDuration());
                    cStmtObject.setString(13, cashlessDetailVO.getTreatmentType());
                    cStmtObject.setString(14, cashlessDetailVO.getEncounterType());
                    cStmtObject.registerOutParameter(15, Types.VARCHAR);//preauth No
                    cStmtObject.setLong(16, userSecurityProfile.getUSER_SEQ_ID());
                    cStmtObject.setLong(17, userSecurityProfile.getHospSeqId());
                    cStmtObject.setString(18, cashlessDetailVO.getEnrollId());
                    cStmtObject.setString(19, benifitTypeVal);
                    cStmtObject.setString(20, cashlessDetailVO.getSpeciality());
                    cStmtObject.setString(21, cashlessDetailVO.getConsultation());
                    cStmtObject.registerOutParameter(22, Types.INTEGER);//rows processed);
                    cStmtObject.setString(23, "Y");//flag
                    cStmtObject.setString(24, cashlessDetailVO.getFileName());//rows processed);
                    if(cashlessDetailVO.getDateOfLMP()!=null)
                    	cStmtObject.setTimestamp(25,new Timestamp(cashlessDetailVO.getDateOfLMP().getTime()));//cashlessDetailVO.getDateOfLMP
                    else
                    	cStmtObject.setDate(25, null);
                    cStmtObject.setString(26, cashlessDetailVO.getNatureOfConception());
                    cStmtObject.setString(27, cashlessDetailVO.getSystemOfMedicine());
                    cStmtObject.setString(28, cashlessDetailVO.getModeofdelivery());
                    cStmtObject.registerOutParameter(1, Types.INTEGER);
                    cStmtObject.setString(29, SubmitYN);
                    cStmtObject.execute();
                    rowsProcessed = cStmtObject.getInt(22);
                    //if(rowsProcessed>0)
                	lPreAuthSeqId	=	cStmtObject.getLong(1);
                	resArr[0]		=	lPreAuthSeqId.toString();
                	resArr[1]		=	cStmtObject.getString(15);
                }//end of try
        		catch (SQLException sqlExp)
        		{	
        			if("providerLogin".equals(loginFlag))
	        		{
        				System.out.println("------------------ Calling : M6/M7 :  savePreAuthDetails ------------------"); System.out.println("loginFlag : "+loginFlag);
        				Long lPreAuthIntSeqId = (cashlessDetailVO.getPreAuthSeqID()==null) ? 0: cashlessDetailVO.getPreAuthSeqID();
	        			Long userSeqId = (userSecurityProfile.getUSER_SEQ_ID()==null) ? 0: userSecurityProfile.getUSER_SEQ_ID();
	        			
	        			System.out.println("SQLException raised...");
            			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
            			System.out.println("userSeqId 			= "+userSeqId);
	        			
	        			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
	        			session.setAttribute("lpreAuthIntSeqId", null);
	        		}
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			if("providerLogin".equals(loginFlag))
	        		{
        				System.out.println("------------------ Calling : M6/M7 :  savePreAuthDetails ------------------"); System.out.println("loginFlag : "+loginFlag);
        				Long lPreAuthIntSeqId = (cashlessDetailVO.getPreAuthSeqID()==null) ? 0: cashlessDetailVO.getPreAuthSeqID();
	        			Long userSeqId = (userSecurityProfile.getUSER_SEQ_ID()==null) ? 0: userSecurityProfile.getUSER_SEQ_ID();
	        			
	        			System.out.println("Java Exception raised...");
            			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
            			System.out.println("userSeqId 			= "+userSeqId);
	        			
	        			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
	        			session.setAttribute("lpreAuthIntSeqId", null);
	        		}
        			throw new TTKException(exp, "onlineformsemp");
        		}//end of catch (Exception exp)
        		finally
        		{
        			CommonClosure.closeOpenResources(conn, cStmtObject, this, "savePreAuthDetails");
        		}//end of finally
        		return resArr;
             } //end of savePreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,long userId)
            
            
            /**
             * This method saves the Pre Auth Information
             * @param CashlessDetailVO cashlessDetailVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public String[] savePartialPreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,UserSecurityProfile userSecurityProfile,String benifitTypeVal,String enhanceYN) throws TTKException
            {
                //int iResult = 0;
                Connection conn = null;
                CallableStatement cStmtObject=null;
                Long lPreAuthSeqId= null;
                int rowsProcessed	=	0;
                String[] resArr	=	new String[2];
                try {
                	
                    conn = ResourceManager.getConnection();
                    cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePartialPreAuth);
                    if(cashlessDetailVO.getPreAuthSeqID()==null)
                    	cStmtObject.setLong(1, 0);
                    else
                    	cStmtObject.setLong(1, cashlessDetailVO.getPreAuthSeqID());
                    cStmtObject.setLong(2, cashlessDetailVO.getMemberSeqID());//member Seq Id

                    if(cashlessDetailVO.getTreatmentDate()!=null)
                    	cStmtObject.setTimestamp(3,new Timestamp(cashlessDetailVO.getTreatmentDate().getTime()));//TreatmentDate
                    else
                    	cStmtObject.setDate(3, null);
                    
                    if(cashlessDetailVO.getDischargeDate()!=null)
                    	cStmtObject.setTimestamp(4,new Timestamp(cashlessDetailVO.getDischargeDate().getTime()));//TreatmentDate
                    else
                    	cStmtObject.setDate(4, null);
                    String temp	=	"";
                    temp	=	cashlessDetailVO.getClinicianName();
                    if(temp!=null || temp!="")
                    	if(temp.indexOf("::")!=-1)
                    		temp	=	temp.substring(0, temp.indexOf("::"));
                    cStmtObject.setString(5, temp);//clinician Name
                    cStmtObject.setString(6, cashlessDetailVO.getClinicianId());
//                    cStmtObject.setString(7, cashlessDetailVO.getSpeciality());
//                    cStmtObject.setString(8, cashlessDetailVO.getConsultation());
                    
                    cStmtObject.setString(7, cashlessDetailVO.getPresentingComplaints());
                    cStmtObject.setString(8, cashlessDetailVO.getPastHistory());
                    cStmtObject.setString(9, cashlessDetailVO.getAilmentDurationText());
                    cStmtObject.setString(10, cashlessDetailVO.getAilmentDuration());
                    cStmtObject.setString(11, cashlessDetailVO.getIllnessDurationText());
                    cStmtObject.setString(12, cashlessDetailVO.getIllnessDuration());
                    cStmtObject.setString(13, cashlessDetailVO.getTreatmentType());
                    cStmtObject.setString(14, cashlessDetailVO.getEncounterType());
                    cStmtObject.registerOutParameter(15, Types.VARCHAR);//preauth No
                    cStmtObject.setLong(16, userSecurityProfile.getUSER_SEQ_ID());
                    cStmtObject.setLong(17, userSecurityProfile.getHospSeqId());
                    cStmtObject.setString(18, cashlessDetailVO.getEnrollId());
                    cStmtObject.setString(19, cashlessDetailVO.getBenifitTypeCode());
                    cStmtObject.setString(20, cashlessDetailVO.getSpeciality());
                    cStmtObject.setString(21, cashlessDetailVO.getConsultation());
                    cStmtObject.registerOutParameter(22, Types.INTEGER);//rows processed);
                   /*Passing null value for file related fields.Because this file is storing common table*/
                    cStmtObject.setString(23, null);//File Name
                    cStmtObject.setString(24, enhanceYN);
                    cStmtObject.setString(25, cashlessDetailVO.getDescription());//File Type
                    cStmtObject.setBytes(26, null);//FILE BLOB



                    if(cashlessDetailVO.getDateOfLMP()!=null)
                    	cStmtObject.setTimestamp(27,new Timestamp(cashlessDetailVO.getDateOfLMP().getTime()));//cashlessDetailVO.getDateOfLMP
                    else
                    	cStmtObject.setDate(27, null);
                    cStmtObject.setString(28, cashlessDetailVO.getNatureOfConception());
                    cStmtObject.setString(29, cashlessDetailVO.getSystemOfMedicine());
                    cStmtObject.setString(30, cashlessDetailVO.getModeofdelivery());
                    cStmtObject.setString(31, cashlessDetailVO.getEventReferenceNo());
                    
                    
                    //Inpatient New Columns  E N D S                    
                    
                    cStmtObject.registerOutParameter(1, Types.INTEGER);
                    cStmtObject.execute();
                    rowsProcessed = cStmtObject.getInt(22);
                    //if(rowsProcessed>0)
                    	lPreAuthSeqId	=	cStmtObject.getLong(1);
                    	resArr[0]		=	lPreAuthSeqId.toString();
                    	resArr[1]		=	cStmtObject.getString(15);
                }//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlineformsemp");
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
                			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl savePreAuthDetails()",sqlExp);
                			throw new TTKException(sqlExp, "onlineformsemp");
                		}//end of catch (SQLException sqlExp)
                		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
                		{
                			try
                			{
                				if(conn != null) conn.close();
                			}//end of try
                			catch (SQLException sqlExp)
                			{
                				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl savePreAuthDetails()",sqlExp);
                				throw new TTKException(sqlExp, "onlineformsemp");
                			}//end of catch (SQLException sqlExp)
                		}//end of finally Connection Close
                	}//end of try
                	catch (TTKException exp)
                	{
                		throw new TTKException(exp, "onlineformsemp");
                	}//end of catch (TTKException exp)
                	finally // Control will reach here in anycase set null to the objects
                	{
                		cStmtObject = null;
                		conn = null;
                	}//end of finally
        		}//end of finally
        		return resArr;
             } //end of savePartialPreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,long userId)
            
            /**
             * This method saves the Pre Auth Information
             * @param CashlessDetailVO cashlessDetailVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public String[] savePartnerPartialPreAuthDetails(CashlessDetailVO cashlessDetailVO,UserSecurityProfile userSecurityProfile,String benifitTypeVal,String enhanceYN,FormFile formFile,String fileName,String description) throws TTKException
            {
                //int iResult = 0;
                Connection conn = null;
                CallableStatement cStmtObject=null;
                Long lPreAuthSeqId= null;
                int rowsProcessed	=	0;
                String[] resArr	=	new String[2];
              
                try {
                	
                    conn = ResourceManager.getConnection();
                    cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePartnerPartialPreAuth);
                    cStmtObject.setString(1, userSecurityProfile.getEmpanelNumber());
                    if(cashlessDetailVO.getPreAuthSeqID()!=null)
                    cStmtObject.setLong(2, cashlessDetailVO.getMemberSeqID()); //member Seq Id
                    else
                    cStmtObject.setLong(2, 0);	
                    cStmtObject.setString(3, cashlessDetailVO.getEnrollId());
                    cStmtObject.setString(4, cashlessDetailVO.getBenifitTypeCode());
                    cStmtObject.setString(5,TTKCommon.getConvertDateToString(cashlessDetailVO.getTreatmentDate())+" "+cashlessDetailVO.getReceiveTime()+" "+cashlessDetailVO.getReceiveDay());
                    cStmtObject.setString(6,TTKCommon.getConvertDateToString(cashlessDetailVO.getDischargeDate())+" "+cashlessDetailVO.getDischargeTime()+" "+cashlessDetailVO.getDischargeDay());
                    cStmtObject.setDouble(7, cashlessDetailVO.getEstimatedCost());
                    cStmtObject.setString(8,cashlessDetailVO.getProviderName());
                    cStmtObject.setLong(9,Long.parseLong(cashlessDetailVO.getCountry()));
                    cStmtObject.setString(10,cashlessDetailVO.getCurrency());
                    cStmtObject.setString(11,cashlessDetailVO.getLineOfTreatment());
                   if(formFile==null){
                	   cStmtObject.setBytes(12, null);
                   }else{
                	   cStmtObject.setBytes(12, formFile.getFileData());
                   }
                    if(cashlessDetailVO.getGenratedReferenceId()!=null)
                    cStmtObject.setString(13, cashlessDetailVO.getGenratedReferenceId());
                    else
                    	cStmtObject.setString(13, "");
                    cStmtObject.setLong(14,userSecurityProfile.getPtrContactSeqID());
                    cStmtObject.setString(15,cashlessDetailVO.getPtrEncounterType());
                    cStmtObject.setString(16,fileName);
                    cStmtObject.setString(17,description);
                    cStmtObject.registerOutParameter(18, Types.INTEGER);
                    cStmtObject.registerOutParameter(2, Types.INTEGER);
                    cStmtObject.registerOutParameter(13, Types.VARCHAR);
                    cStmtObject.registerOutParameter(19,OracleTypes.CURSOR);
                    cStmtObject.execute();
                    rowsProcessed = cStmtObject.getInt(18);
                    ResultSet resultSet = (java.sql.ResultSet)cStmtObject.getObject(19);
                    //if(rowsProcessed>0)
                    	lPreAuthSeqId	=	cStmtObject.getLong(2);
                    	resArr[0]		=	lPreAuthSeqId.toString();
                    	resArr[1]		=	cStmtObject.getString(13);
                }//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlineformsemp");
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
                			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl savePreAuthDetails()",sqlExp);
                			throw new TTKException(sqlExp, "onlineformsemp");
                		}//end of catch (SQLException sqlExp)
                		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
                		{
                			try
                			{
                				if(conn != null) conn.close();
                			}//end of try
                			catch (SQLException sqlExp)
                			{
                				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl savePreAuthDetails()",sqlExp);
                				throw new TTKException(sqlExp, "onlineformsemp");
                			}//end of catch (SQLException sqlExp)
                		}//end of finally Connection Close
                	}//end of try
                	catch (TTKException exp)
                	{
                		throw new TTKException(exp, "onlineformsemp");
                	}//end of catch (TTKException exp)
                	finally // Control will reach here in anycase set null to the objects
                	{
                		cStmtObject = null;
                		conn = null;
                	}//end of finally
        		}//end of finally
        		return resArr;
             } //end of savePartialPreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,long userId)
            
            /**
             * This method saves the Pre Auth Information
             * @param CashlessDetailVO cashlessDetailVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public CashlessDetailVO getPreAuthDetails(Long lPreAuthIntSeqId) throws TTKException
            {
                //int iResult = 0;
            	Connection conn = null;
            	CallableStatement cStmtObject=null;
            	ResultSet rs = null;
            	CashlessDetailVO cashlessDetailVO	=	null;
                try {
                	
                	conn = ResourceManager.getConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetPreAuthDetails);
            		cStmtObject.setLong(1, lPreAuthIntSeqId);//pat_intimation_seq_id

            		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        			cStmtObject.execute();
        			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
        			if(rs != null){
        				while(rs.next()){
        					
        					cashlessDetailVO	=	new CashlessDetailVO();
        					cashlessDetailVO.setMemberSeqID(TTKCommon.getLong(rs.getString("MEMBER_SEQ_ID")));
        					cashlessDetailVO.setEnrollId(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
        					cashlessDetailVO.setMemberName(TTKCommon.checkNull(rs.getString("INSURED_NAME")));
        					cashlessDetailVO.setMemDob(rs.getDate("MEM_DOB"));
        					cashlessDetailVO.setGender(TTKCommon.checkNull(rs.getString("GENDER")));
        					if(rs.getTimestamp("HOSPITALIZATION_DATE") != null)
    	                    {
        						 cashlessDetailVO.setTreatmentDate(new java.util.Date(rs.getTimestamp("HOSPITALIZATION_DATE").getTime()));
    	                    }//end of if(rs.getTimestamp("HOSPITALIZATION_DATE") != null)
        					if(rs.getTimestamp("DISCHARGE_DATE") != null)
    	                    {
        						 cashlessDetailVO.setDischargeDate(new java.util.Date(rs.getTimestamp("DISCHARGE_DATE").getTime()));
    	                    }//end of if(rs.getTimestamp("DISCHARGE_DATE") != null)
        					cashlessDetailVO.setClinicianId(TTKCommon.checkNull(rs.getString("CLINICIAN_ID")));
        					cashlessDetailVO.setClinicianName(TTKCommon.checkNull(rs.getString("CLINICIAN_NAME")));
        					cashlessDetailVO.setSpeciality(TTKCommon.checkNull(rs.getString("CLINICIAN_SPECIALITY")));
        					cashlessDetailVO.setPresentingComplaints(TTKCommon.checkNull(rs.getString("PRESENTING_COMPLAINTS")));
        					cashlessDetailVO.setConsultation(TTKCommon.checkNull(rs.getString("CONSULTATION")));
        					cashlessDetailVO.setBenifitType(TTKCommon.checkNull(rs.getString("BENIFIT_TYPE")));
        					cashlessDetailVO.setAilmentDurationText(TTKCommon.checkNull(rs.getString("SINCE_WHEN")));
        					cashlessDetailVO.setAilmentDuration(TTKCommon.checkNull(rs.getString("SINCE_WHEN_TIME")));
        					cashlessDetailVO.setIllnessDurationText(TTKCommon.checkNull(rs.getString("DURATION_ABOVE_ILLNESS")));
        					cashlessDetailVO.setIllnessDuration(TTKCommon.checkNull(rs.getString("DURATION_ABOVE_ILLNESS_TIME")));
        					cashlessDetailVO.setTreatmentType(TTKCommon.checkNull(rs.getString("SYSTEM_OF_MEDICINE_TYPE_ID")));
        					cashlessDetailVO.setEncounterType(TTKCommon.checkNull(rs.getString("ENCOUNTER_TYPE_ID")));
        				}
        			}
                }//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlineformsemp");
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
        					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        					throw new TTKException(sqlExp, "hospital");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (cStmtObject != null) cStmtObject.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        						throw new TTKException(sqlExp, "hospital");
        					}//end of catch (SQLException sqlExp)
        					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        					{
        						try
        						{
        							if(conn != null) conn.close();
        						}//end of try
        						catch (SQLException sqlExp)
        						{
        							log.error("Error while closing the Connection in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        							throw new TTKException(sqlExp, "hospital");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "hospital");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        		return cashlessDetailVO;
             } //end of getPreAuthDetails(iPreAuthIntSeqId)
            
            
            /**
             * This method saves the Pre Auth  Concumables
             * @param LabServiceVO labServiceVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public int savePreAuthConsumables(LabServiceVO labServiceVO,Long intimationSeqID) throws TTKException
            {
                //int iResult = 0;
                Connection conn = null;
                CallableStatement cStmtObject=null;
                int iConsId= 0;
                try {
                	
                    conn = ResourceManager.getConnection();
                    cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveConsumables);

                    cStmtObject.setLong(1, 0);									//CONSUMABLE_SEQ_ID 
                    cStmtObject.setLong(2,intimationSeqID);						//PAT_INTIMATION_SEQ_ID 
                    cStmtObject.setLong(3,labServiceVO.getActivitySeqId()); 	//ACTIVITY_SEQ_ID 
                    cStmtObject.setString(4,labServiceVO.getActivityCode());	//ACTIVITY_CODE 
                    cStmtObject.setBigDecimal(5,labServiceVO.getUnitPrice());	//UNIT_PRICE 
                    cStmtObject.setInt(6,labServiceVO.getQuantity());			//QUANTITY 
                    cStmtObject.setBigDecimal(7,labServiceVO.getGross());		//GROSS 
                    cStmtObject.setBigDecimal(8,labServiceVO.getDiscount());			//DISCOUNT 
                    cStmtObject.setBigDecimal(9,labServiceVO.getDiscGross());	//DISCOUNTED_GROSS 
                    cStmtObject.setBigDecimal(10,labServiceVO.getPatientShare());//PATIENT_SHARE 
                    cStmtObject.setBigDecimal(11,labServiceVO.getNetAmount());	//NET_AMOUNT 
                    cStmtObject.setLong(12,labServiceVO.getAddedBy());			//ADDED_BY 
                    cStmtObject.registerOutParameter(13,Types.INTEGER);			//ROW_PROCESSED
                    cStmtObject.execute();
                    iConsId = cStmtObject.getInt(13);
                }//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlineformsemp");
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
                			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl savePreAuthConsumables()",sqlExp);
                			throw new TTKException(sqlExp, "onlineformsemp");
                		}//end of catch (SQLException sqlExp)
                		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
                		{
                			try
                			{
                				if(conn != null) conn.close();
                			}//end of try
                			catch (SQLException sqlExp)
                			{
                				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl savePreAuthConsumables()",sqlExp);
                				throw new TTKException(sqlExp, "onlineformsemp");
                			}//end of catch (SQLException sqlExp)
                		}//end of finally Connection Close
                	}//end of try
                	catch (TTKException exp)
                	{
                		throw new TTKException(exp, "onlineformsemp");
                	}//end of catch (TTKException exp)
                	finally // Control will reach here in anycase set null to the objects
                	{
                		cStmtObject = null;
                		conn = null;
                	}//end of finally
        		}//end of finally
        		return iConsId;
             } //end of savePreAuthConsumables(LabServiceVO labServiceVO)
            
            
            /**
             * This method saves the Pre Auth Information
             * @param CashlessDetailVO cashlessDetailVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public ArrayList getPreAuthConsumables(Long intimationSeqID) throws TTKException
            {
                //int iResult = 0;
            	Connection conn = null;
            	CallableStatement cStmtObject=null;
            	ResultSet rs = null;
            	LabServiceVO	labServiceVO		=	null;
            	ArrayList alConsumablesList	=	new ArrayList();	
                try {
                	
                	conn = ResourceManager.getConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetConsumablesList);
            		cStmtObject.setLong(1, intimationSeqID);//pat_intimation_seq_id
            		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        			cStmtObject.execute();
        			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
        			if(rs != null){
        				while(rs.next()){
        					labServiceVO	=	new LabServiceVO();
        					labServiceVO.setActivityCode(TTKCommon.checkNull(rs.getString("ACTIVITY_CODE")));
        					labServiceVO.setServiceName(TTKCommon.checkNull(rs.getString("SERVICE_NAME")));
        					labServiceVO.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));
        					labServiceVO.setQuantity(rs.getInt("QUANTITY"));
        					labServiceVO.setGross(rs.getBigDecimal("GROSS"));
        					labServiceVO.setDiscount(rs.getBigDecimal("DISCOUNT"));
        					labServiceVO.setDiscGross(rs.getBigDecimal("DISCOUNTED_GROSS"));
        					labServiceVO.setPatientShare(rs.getBigDecimal("PATIENT_SHARE"));
        					labServiceVO.setNetAmount(rs.getBigDecimal("NET_AMOUNT"));
        					alConsumablesList.add(labServiceVO);
        				}
        			}
                }//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlineformsemp");
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
        					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        					throw new TTKException(sqlExp, "hospital");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (cStmtObject != null) cStmtObject.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        						throw new TTKException(sqlExp, "hospital");
        					}//end of catch (SQLException sqlExp)
        					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        					{
        						try
        						{
        							if(conn != null) conn.close();
        						}//end of try
        						catch (SQLException sqlExp)
        						{
        							log.error("Error while closing the Connection in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        							throw new TTKException(sqlExp, "hospital");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "hospital");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        		return alConsumablesList;
             } //end of getPreAuthConsumables(intimationSeqID)
            
            
            
            
            
            /**
             * This method get The Pharmacy Details
             * @param CashlessDetailVO cashlessDetailVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public LabServiceVO getPreAuthPharamcyDetails(String pharmacyDesc) throws TTKException
            {
                //int iResult = 0;
            	Connection conn = null;
            	CallableStatement cStmtObject=null;
            	ResultSet rs = null;
            	LabServiceVO	labServiceVO		=	null;
            	ArrayList alPharmacy	=	new ArrayList();
            	PreparedStatement pStmt 		= 	null;
                try {
                    conn = ResourceManager.getConnection();
                    pStmt=conn.prepareStatement(strGetPharmacyDetails);
                    pStmt.setString(1, pharmacyDesc);
                    rs = pStmt.executeQuery();
                    if(rs != null){
                        while(rs.next()){
                        	labServiceVO = new LabServiceVO();
                            labServiceVO.setActivityCode(TTKCommon.checkNull(rs.getString("ACTIVITY_CODE")));
                            labServiceVO.setPharmacyDesc(TTKCommon.checkNull(rs.getString("ACTIVITY_DESCRIPTION")));
                            labServiceVO.setStrength(TTKCommon.checkNull(rs.getString("INGREDIENT_STRENGTH")));
                        }//end of while(rs.next())
                    }//end of if(rs != null)
                }//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlineformsemp");
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
        					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        					throw new TTKException(sqlExp, "hospital");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (pStmt != null) pStmt.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        						throw new TTKException(sqlExp, "hospital");
        					}//end of catch (SQLException sqlExp)
        					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        					{
        						try
        						{
        							if(conn != null) conn.close();
        						}//end of try
        						catch (SQLException sqlExp)
        						{
        							log.error("Error while closing the Connection in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
        							throw new TTKException(sqlExp, "hospital");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "hospital");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				pStmt = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        		return labServiceVO;
             } //end of getPreAuthPharamcyDetails(String pharmacyDesc)
            
            
            /**
             * This method saves the Pre Auth  Concumables
             * @param LabServiceVO labServiceVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public int saveDiagnosisDetails(ArrayList<DiagnosisDetailsVO> diagnosis,Long lPreAuthIntSeqId,Long userSeqId) throws TTKException
            {
                //int iResult = 0;
                Connection conn = null;
                int iConsId= 0;
        		PreparedStatement pStmt 		= 	null;
                try {
                    conn = ResourceManager.getConnection();
                    DiagnosisDetailsVO diagnosisDetailsVO	=	null;
        			conn.setAutoCommit(false);
        			pStmt	=	conn.prepareStatement(strSaveDiagnosisDetails);
        			if(diagnosis != null){
        				for(int i=0;i<diagnosis.size();i++){
        					/*sbfSQL = new StringBuffer();
        					diagnosisDetailsVO = (DiagnosisDetailsVO)diagnosis.get(i);
        					  
        					if(diagnosisDetailsVO.getDiagSeqId()==null)
        						sbfSQL = sbfSQL.append("'"+0+"',");
        					else
        						sbfSQL = sbfSQL.append("'"+diagnosisDetailsVO.getDiagSeqId()+"',");
        					sbfSQL = sbfSQL.append("'"+lPreAuthIntSeqId+"',");
        					sbfSQL = sbfSQL.append("'"+diagnosisDetailsVO.getIcdCode()+"',");
        					sbfSQL = sbfSQL.append("'"+diagnosisDetailsVO.getPrimaryAilment()+"',");
        					sbfSQL = sbfSQL.append("'"+userSeqId+"')}");*/
        					diagnosisDetailsVO = (DiagnosisDetailsVO)diagnosis.get(i);
        					if(diagnosisDetailsVO.getDiagSeqId()==null)
        						pStmt.setInt(1,0);
        					else
        						pStmt.setLong(1,diagnosisDetailsVO.getDiagSeqId());
        					pStmt.setLong(2,lPreAuthIntSeqId);
        					pStmt.setString(3,diagnosisDetailsVO.getIcdCode());
        					pStmt.setString(4,diagnosisDetailsVO.getPrimaryAilment());
        					pStmt.setLong(5,userSeqId);
        					
        					pStmt.addBatch();
      				}//end of for
        			}//end of if(diagnosis != null)
        			pStmt.executeBatch();
        			conn.commit();
                    
                }//end of try
                catch (SQLException sqlExp) 
                { 
                    try {
                        conn.rollback();
                        throw new TTKException(sqlExp, "onlineformsemp");
                    } //end of try
                    catch (SQLException sExp) {
                        throw new TTKException(sExp, "onlineformsemp");   
                    }//end of catch (SQLException sExp)
                }//end of catch (SQLException sqlExp) 
            	 catch (Exception exp) 
                 {
                     try {
                         conn.rollback();
                         throw new TTKException(exp, "onlineformsemp");
                     } //end of try
                     catch (SQLException sqlExp) {
                         throw new TTKException(sqlExp, "onlineformsemp");
                     }//end of catch (SQLException sqlExp)
                 }//end of catch (Exception exp)
        		finally
        		{
                /* Nested Try Catch to ensure resource closure */ 
                	try // First try closing the Statement
                	{
                		try
                		{
                                if (pStmt != null) pStmt.close();
                		}//end of try
                		catch (SQLException sqlExp)
                		{
                			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl savePreAuthConsumables()",sqlExp);
                			throw new TTKException(sqlExp, "onlineformsemp");
                		}//end of catch (SQLException sqlExp)
                		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
                		{
                			try
                			{
                				if(conn != null) conn.close();
                			}//end of try
                			catch (SQLException sqlExp)
                			{
                                        log.error("Error while closing the Statement in OnlinePreAuthDAOImpl savePreAuthConsumables()",sqlExp);
                				throw new TTKException(sqlExp, "onlineformsemp");
                			}//end of catch (SQLException sqlExp)
                		}//end of finally Connection Close
                	}//end of try
                	catch (TTKException exp)
                	{
                                throw new TTKException(exp, "hospital");
                	}//end of catch (TTKException exp)
                        finally // Control will reach here in anycase set null to the objects 
                	{
                        	pStmt = null;
                		conn = null;
                	}//end of finally
        		}//end of finally
        		return iConsId;
             } //end of DiagnosisDetailsVO saveDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO)
            
            
             public int saveActivitiesDetails(ArrayList<ActivityDetailsVO> alActivityDetails,Long lPreAuthIntSeqId,Long userSeqId,String treatmentDate,String clinicianId,String loginFlag,HttpServletRequest request) throws TTKException
            {
            	 //int iResult = 0;
                Connection conn = null;
                int iConsId= 0;
                PreparedStatement pStmt 		= 	null;
                OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
                HttpSession session	=	request.getSession();
                try {
                	
                    conn = ResourceManager.getConnection();
                    ActivityDetailsVO activityDetailsVO	=	null;
        			conn.setAutoCommit(false);
        			pStmt = conn.prepareStatement(strSaveActivitiesDetails);
        			if(alActivityDetails != null){
        				for(int i=0;i<alActivityDetails.size();i++){
        					activityDetailsVO = (ActivityDetailsVO)alActivityDetails.get(i);
        					if(activityDetailsVO.getActivitySeqId()==null)
        						pStmt.setInt(1, 0);
        					else
        						pStmt.setLong(1,activityDetailsVO.getActivitySeqId());
        					pStmt.setLong(2,lPreAuthIntSeqId);
        					pStmt.setString(3,activityDetailsVO.getActivityCode());
        					pStmt.setFloat(4,activityDetailsVO.getQuantity());
        					pStmt.setBigDecimal(5,activityDetailsVO.getGrossAmount());
        					pStmt.setBigDecimal(6,activityDetailsVO.getDiscount());
        					if(activityDetailsVO.getDiscountedGross()==null || "null".equals(activityDetailsVO.getDiscountedGross()))
        						pStmt.setInt(7,0);
        					else
        						pStmt.setBigDecimal(7,activityDetailsVO.getDiscountedGross());
        					
        					if(activityDetailsVO.getApprovedAmount()==null || "null".equals(activityDetailsVO.getApprovedAmount()))
        						pStmt.setInt(8, 0);
        					else
        						pStmt.setBigDecimal(8,activityDetailsVO.getApprovedAmount());
        					pStmt.setString(9,treatmentDate);
        					pStmt.setString(10,"Y");
        					pStmt.setString(11,clinicianId);
        					pStmt.setLong(12,userSeqId);
        					pStmt.setString(13,activityDetailsVO.getActivityTypeId());
        					pStmt.setString(14,activityDetailsVO.getInternalCode());
        					pStmt.setString(15,activityDetailsVO.getToothNo());
        					pStmt.setString(16,activityDetailsVO.getInternalDesc());
        					pStmt.setString(17,activityDetailsVO.getOverrideYN());
        					pStmt.setString(18,activityDetailsVO.getOverrideRemarks());
        					pStmt.setString(19,activityDetailsVO.getDenialCode());
        					pStmt.setString(20,activityDetailsVO.getDenialDescription());
        					pStmt.setString(21,activityDetailsVO.getOverrideRemarksCode());
        					pStmt.setString(22,activityDetailsVO.getOtherRemarks());
        					pStmt.setString(23,activityDetailsVO.getEnhancedFlag());
        					
        					//pStmt.setBigDecimal(21,activityDetailsVO.getApprovedAmount());
        					//pStmt.setString(22,activityDetailsVO.getActivityTypeId());     					
        					pStmt.addBatch();
        				}//end of for
        			}//end of if(alActivityDetails != null)
        			pStmt.executeBatch();
        			conn.commit();
                    
                }//end of try
                catch (SQLException sqlExp) 
                { 
                    try {
                    	if("providerLogin".equals(loginFlag))
                		{
                    		System.out.println("------------------ Calling : M3 :  saveActivitiesDetails ------------------"); System.out.println("loginFlag : "+loginFlag);
                    		lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
                			userSeqId = (userSeqId==null) ? 0: userSeqId;
                			
                			System.out.println("SQLException 1 raised...");
                			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
                			System.out.println("userSeqId 			= "+userSeqId);
                			
                			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
                			session.setAttribute("lpreAuthIntSeqId", null);
                		}
                        conn.rollback();
                        throw new TTKException(sqlExp, "onlineformsemp");
                    } //end of try
                    catch (SQLException sExp) {
                    	if("providerLogin".equals(loginFlag))
                		{
                    		System.out.println("------------------ Calling : M3 :  saveActivitiesDetails ------------------"); System.out.println("loginFlag : "+loginFlag);
                    		lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
                			userSeqId = (userSeqId==null) ? 0: userSeqId;
                			
                			System.out.println("SQLException 2 raised...");
                			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
                			System.out.println("userSeqId 			= "+userSeqId);

                			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
                			session.setAttribute("lpreAuthIntSeqId", null);
                		}
                    	throw new TTKException(sExp, "onlineformsemp");   
                    }//end of catch (SQLException sExp)
                }//end of catch (SQLException sqlExp) 
            	 catch (Exception exp) 
                 {
                     try {
                    	 if("providerLogin".equals(loginFlag))
              			{
                    		 	System.out.println("------------------ Calling : M3 :  saveActivitiesDetails ------------------"); System.out.println("loginFlag : "+loginFlag);	
                    		 	lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
	                 			userSeqId = (userSeqId==null) ? 0: userSeqId;
	                 			
	                 			System.out.println("jAVA Exception  raised...");
	                			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
	                			System.out.println("userSeqId 			= "+userSeqId);
	                			
	                 			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
	                 			session.setAttribute("lpreAuthIntSeqId", null);
              			}
                         conn.rollback();
                         throw new TTKException(exp, "onlineformsemp");
                     } //end of try
                     catch (SQLException sqlExp) {
                    	 if("providerLogin".equals(loginFlag))
               			{
                    		 System.out.println("------------------ Calling : M3 :  saveActivitiesDetails ------------------"); System.out.println("loginFlag : "+loginFlag);
                    		 	lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
	                 			userSeqId = (userSeqId==null) ? 0: userSeqId;
	                 			
	                 			System.out.println("SQLException 3 raised...");
	                			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
	                			System.out.println("userSeqId 			= "+userSeqId);
	                			
	                			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
	                			session.setAttribute("lpreAuthIntSeqId", null);
               			} 
                         throw new TTKException(sqlExp, "onlineformsemp");
                     }//end of catch (SQLException sqlExp)
                 }//end of catch (Exception exp)
        		finally
        		{
                        /* Nested Try Catch to ensure resource closure */ 
                	try // First try closing the Statement
                	{
                		try
                		{
                                                        if (pStmt != null) pStmt.close();
                		}//end of try
                		catch (SQLException sqlExp)
                		{
                			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl savePreAuthConsumables()",sqlExp);
                			throw new TTKException(sqlExp, "onlineformsemp");
                		}//end of catch (SQLException sqlExp)
                		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
                		{
                			try
                			{
                				if(conn != null) conn.close();
                			}//end of try
                			catch (SQLException sqlExp)
                			{
                				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl savePreAuthConsumables()",sqlExp);
                				throw new TTKException(sqlExp, "onlineformsemp");
                			}//end of catch (SQLException sqlExp)
                		}//end of finally Connection Close
                	}//end of try
                	catch (TTKException exp)
                	{
                                        throw new TTKException(exp, "hospital");
                	}//end of catch (TTKException exp)
                        finally // Control will reach here in anycase set null to the objects 
                	{
                        	pStmt = null;
                		conn = null;
                	}//end of finally
        		}//end of finally
        		return iConsId;
             } //end of saveActivitiesDetails(ArrayList<ActivityDetailsVO> diagnosis,Long lPreAuthIntSeqId,Long userSeqId
            
            
            public int saveDrugDetails(ArrayList<DrugDetailsVO> alDrugDetails,Long lPreAuthIntSeqId,Long userSeqId,String treatmentDate,String clinicianId,String loginFlag,HttpServletRequest request) throws TTKException
            {
            	//int iResult = 0;
                Connection conn = null;
                int iConsId= 0;
                PreparedStatement pStmt 		= 	null;
                OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
                HttpSession session	=	request.getSession();
                try {
                	
                    conn = ResourceManager.getConnection();
                    DrugDetailsVO alDrugDetailsVO	=	null;
        			conn.setAutoCommit(false);
        			pStmt =conn.prepareStatement(strSaveDrugDetails);
        			if(alDrugDetails != null){
        				for(int i=0;i<alDrugDetails.size();i++){
        					
        					alDrugDetailsVO = (DrugDetailsVO)alDrugDetails.get(i);

        					if(alDrugDetailsVO.getDrugSeqId()==null)
        						pStmt.setInt(1,0);
        					else
        						pStmt.setLong(1,alDrugDetailsVO.getDrugSeqId());
        					pStmt.setLong(2,lPreAuthIntSeqId);
        					pStmt.setString(3,alDrugDetailsVO.getDrugCode());
        					pStmt.setFloat(4,alDrugDetailsVO.getDrugqty());
        					pStmt.setString(5,alDrugDetailsVO.getPosology());
        					pStmt.setString(6,alDrugDetailsVO.getDays());
        					pStmt.setString(7,alDrugDetailsVO.getDrugunit());
        					
        					if(alDrugDetailsVO.getGrossAmount()==null || "null".equals(alDrugDetailsVO.getGrossAmount()))
        						pStmt.setInt(8,0);
        					else
        						pStmt.setBigDecimal(8,alDrugDetailsVO.getGrossAmount());
        					
        					if(alDrugDetailsVO.getDiscount()==null || "null".equals(alDrugDetailsVO.getDiscount()))
        						pStmt.setInt(9,0);
        					else
        						pStmt.setBigDecimal(9,alDrugDetailsVO.getDiscount());
        					
        					if(alDrugDetailsVO.getNetPriceAftDisc()==null || "null".equals(alDrugDetailsVO.getNetPriceAftDisc()))
        						pStmt.setInt(10,0);
        					else
        						pStmt.setBigDecimal(10,alDrugDetailsVO.getNetPriceAftDisc());
        					
        					if(alDrugDetailsVO.getTtlAmnt()==null || "null".equals(alDrugDetailsVO.getTtlAmnt()))
        						pStmt.setInt(11, 0);
        					else
        						pStmt.setBigDecimal(11, alDrugDetailsVO.getTtlAmnt());
        					pStmt.setString(12, treatmentDate);
        					pStmt.setString(13,"Y"); 
        					pStmt.setString(14,clinicianId);
        					pStmt.setLong(15,userSeqId);
        					pStmt.setString(16,alDrugDetailsVO.getEnhancedYN() );
        					pStmt.addBatch();
        				}//end of for
        			}//end of if(alActivityDetails != null)
        			pStmt.executeBatch();
        			conn.commit();
                    
                }//end of try
                catch (SQLException sqlExp) 
                { 
                    try {
                    	if("providerLogin".equals(loginFlag))
                		{
                    		System.out.println("------------------ Calling : M4 :  saveDrugDetails() ------------------"); System.out.println("loginFlag : "+loginFlag);
                    		lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
                			userSeqId = (userSeqId==null) ? 0: userSeqId;
                			
                			System.out.println("SQLException 1 raised...");
                			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
                			System.out.println("userSeqId 			= "+userSeqId);
                			
                			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
                			session.setAttribute("lpreAuthIntSeqId", null);
                		}
                        conn.rollback();
                        throw new TTKException(sqlExp, "onlineformsemp");
                    } //end of try
                    catch (SQLException sExp) {
                    	if("providerLogin".equals(loginFlag))
                		{
                    		System.out.println("------------------ Calling : M4 :  saveDrugDetails() ------------------"); System.out.println("loginFlag : "+loginFlag);
                    		lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
                			userSeqId = (userSeqId==null) ? 0: userSeqId;
                			
                			System.out.println("SQLException 2 raised...");
                			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
                			System.out.println("userSeqId 			= "+userSeqId);
                			
                			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
                			session.setAttribute("lpreAuthIntSeqId", null);
                		}
                        throw new TTKException(sExp, "onlineformsemp");   
                    }//end of catch (SQLException sExp)
                }//end of catch (SQLException sqlExp) 
            	 catch (Exception exp) 
                 {
                     try {
                    	 if("providerLogin".equals(loginFlag))
                 		 {
                    		 System.out.println("------------------ Calling : M4 :  saveDrugDetails() ------------------"); System.out.println("loginFlag : "+loginFlag);
                    		 lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
                 			userSeqId = (userSeqId==null) ? 0: userSeqId;
                 			
                 			System.out.println("JAVA Exception raised...");
                 			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
                 			System.out.println("userSeqId 			= "+userSeqId);
                 			
                 			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
                 			session.setAttribute("lpreAuthIntSeqId", null);
                 		 }
                         conn.rollback();
                         throw new TTKException(exp, "onlineformsemp");
                     } //end of try
                     catch (SQLException sqlExp) {
                    	 if("providerLogin".equals(loginFlag))
                  		{
                    		 System.out.println("------------------ Calling : M4 :  saveDrugDetails() ------------------"); System.out.println("loginFlag : "+loginFlag);
                    		 lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
                  			userSeqId = (userSeqId==null) ? 0: userSeqId;
                  			
                  			System.out.println("SQLException 3 raised...");
                  			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
                  			System.out.println("userSeqId 			= "+userSeqId);
                  			
                  			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
                  			session.setAttribute("lpreAuthIntSeqId", null);
                  		}
                         throw new TTKException(sqlExp, "onlineformsemp");
                     }//end of catch (SQLException sqlExp)
                 }//end of catch (Exception exp)
        		finally
        		{
                        /* Nested Try Catch to ensure resource closure */ 
                	try // First try closing the Statement
                	{
                		try
                		{
                                                        if (pStmt != null) pStmt.close();
                		}//end of try
                		catch (SQLException sqlExp)
                		{
                			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl savePreAuthConsumables()",sqlExp);
                			throw new TTKException(sqlExp, "onlineformsemp");
                		}//end of catch (SQLException sqlExp)
                		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
                		{
                			try
                			{
                				if(conn != null) conn.close();
                			}//end of try
                			catch (SQLException sqlExp)
                			{
                				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl savePreAuthConsumables()",sqlExp);
                				throw new TTKException(sqlExp, "onlineformsemp");
                			}//end of catch (SQLException sqlExp)
                		}//end of finally Connection Close
                	}//end of try
                	catch (TTKException exp)
                	{
                                        throw new TTKException(exp, "hospital");
                	}//end of catch (TTKException exp)
                        finally // Control will reach here in anycase set null to the objects 
                	{
                        	pStmt = null;
                		conn = null;
                	}//end of finally
        		}//end of finally
        		return iConsId;
             } //end of saveDrugDetails(ArrayList<DrugDetailsVO> alDrugDetails,Long lPreAuthIntSeqId,Long userSeqId
            
            
            /**
             * This method get The Diagnostics Details
             * @param CashlessDetailVO cashlessDetailVO which contains the PreAuth Information
             * @return lngPolicySeqID long Object, 
             * @exception throws TTKException
             */
            public ArrayList<DiagnosisDetailsVO> getDiagDetails(Long lPreAuthIntSeqId) throws TTKException
            {
                //int iResult = 0;
            	Connection conn = null;
            	CallableStatement cStmtObject=null;
            	ResultSet rs = null;
            	DiagnosisDetailsVO	diagnosisDetailsVO		=	null;
            	ArrayList<DiagnosisDetailsVO> alDiagnosis	=	new ArrayList<DiagnosisDetailsVO>();
                try {
                	
                    conn = ResourceManager.getConnection();
                    
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDiagDetails);
            		cStmtObject.setLong(1, lPreAuthIntSeqId);//pat_intimation_seq_id
            		cStmtObject.setString(2, "PAT");//pat_intimation_seq_id
            		cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        			cStmtObject.execute();
        			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
        			if(rs != null){
                        while(rs.next()){
                        	diagnosisDetailsVO = new DiagnosisDetailsVO();
                        	diagnosisDetailsVO.setIcdCode(TTKCommon.checkNull(rs.getString("DIAGNOSYS_CODE")));
                        	diagnosisDetailsVO.setAilmentDescription(TTKCommon.checkNull(rs.getString("ICD_DESCRIPTION")));
                        	diagnosisDetailsVO.setPrimaryAilment(TTKCommon.checkNull(rs.getString("PRIMARY_AILMENT_YN")));
                        	diagnosisDetailsVO.setDiagSeqId((Long) TTKCommon.checkNull(rs.getLong("DIAG_SEQ_ID")));
                            alDiagnosis.add(diagnosisDetailsVO);
                        }//end of while(rs.next())
                    }//end of if(rs != null)
                }//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlineformsemp");
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
        					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl getDiagDetails()",sqlExp);
        					throw new TTKException(sqlExp, "hospital");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (cStmtObject != null) cStmtObject.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getDiagDetails()",sqlExp);
        						throw new TTKException(sqlExp, "hospital");
        					}//end of catch (SQLException sqlExp)
        					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        					{
        						try
        						{
        							if(conn != null) conn.close();
        						}//end of try
        						catch (SQLException sqlExp)
        						{
        							log.error("Error while closing the Connection in OnlinePreAuthDAOImpl getDiagDetails()",sqlExp);
        							throw new TTKException(sqlExp, "hospital");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "hospital");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        		return alDiagnosis;
             } //end of getDiagDetails(Long lPreAuthIntSeqId)
            
            
            public Object[] deleteDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException {
            	Object[] objArrayResult = new Object[2];
            	Connection conn = null;
            	CallableStatement cStmtObject=null;
            	try{		
            		conn = ResourceManager.getConnection();
            		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteDiagnosisDetails);
            		if("PAT".equals(diagnosisDetailsVO.getAuthType()))cStmtObject.setLong(1,diagnosisDetailsVO.getPreAuthSeqID());
            		else if("CLM".equals(diagnosisDetailsVO.getAuthType())) cStmtObject.setLong(1,diagnosisDetailsVO.getClaimSeqID());
            			cStmtObject.setLong(2,diagnosisDetailsVO.getDiagSeqId());
            		   cStmtObject.setString(3,diagnosisDetailsVO.getAuthType());
            		   cStmtObject.registerOutParameter(4,Types.INTEGER);
            		   cStmtObject.execute();			
            		  objArrayResult[0] = cStmtObject.getInt(4);		
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
            			{
            				if (cStmtObject != null) cStmtObject.close();
            			}//end of try
            			catch (SQLException sqlExp)
            			{
            				log.error("Error while closing the Statement in PreAuthDAOImpl deleteDiagnosisDetails()",sqlExp);
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
            					log.error("Error while closing the Connection in PreAuthDAOImpl deleteDiagnosisDetails()",sqlExp);
            					throw new TTKException(sqlExp, "preauth");
            				}//end of catch (SQLException sqlExp)
            			}//end of finally Connection Close
            		}//end of try
            		catch (TTKException exp)
            		{
            			throw new TTKException(exp, "preauth");
            		}//end of catch (TTKException exp)
            		finally // Control will reach here in anycase set null to the objects 
            		{
            			cStmtObject = null;
            			conn = null;
            		}//end of finally
            	}//end of finally
            	return objArrayResult;
            }//end of deleteDiagnosisDetails(PreAuthDetailVO preAuthDetailVO,String strSelectionType)          
            
            
            
            public Object[] getOnlinePreAuthDetails(Long preAuthSeqID) throws TTKException{
        		Connection conn = null;
        		CallableStatement cStmtObject=null;
        		ResultSet prs=null;ResultSet drs=null;ResultSet ars = null;ResultSet shrs = null;
        		CashlessDetailVO cahlCashlessDetailVO  = new CashlessDetailVO();
        		Object[] preauthAllResult=new Object[4];
        		ArrayList<DiagnosisDetailsVO> diagnosis=new ArrayList<DiagnosisDetailsVO>();
        		ArrayList<ActivityDetailsVO> activities=new ArrayList<ActivityDetailsVO>();
        		ArrayList<String[]> shortfalls=null;
        		try{
        			conn = ResourceManager.getConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthorization);
        			cStmtObject.setLong(1,preAuthSeqID);			
        			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
        			cStmtObject.execute();
        			prs = (java.sql.ResultSet)cStmtObject.getObject(2);//preauth Details
        			drs = (java.sql.ResultSet)cStmtObject.getObject(3);//diagnosis Details
        			ars = (java.sql.ResultSet)cStmtObject.getObject(4);//activity Details
        			shrs = (java.sql.ResultSet)cStmtObject.getObject(6);//Shortfalls Details
        			
        			
        		if(drs!=null){			
        		while(drs.next()){	
        			String diagCode=drs.getString("DIAGNOSYS_CODE")==null?"":drs.getString("DIAGNOSYS_CODE");
        			String desc=drs.getString("ICD_DESCRIPTION")==null?"":drs.getString("ICD_DESCRIPTION");
        			String primAil=drs.getString("PRIMARY_AILMENT_YN")==null?"":drs.getString("PRIMARY_AILMENT_YN");
        			Long diagSeqId=drs.getLong("DIAG_SEQ_ID");
        			Long icdCodeSeqId=drs.getLong("ICD_CODE_SEQ_ID");		
        			diagnosis.add(new DiagnosisDetailsVO(diagCode,desc,primAil,diagSeqId,icdCodeSeqId));
        		   }			
        		}
        			
        			preauthAllResult[0]=cahlCashlessDetailVO;
        			preauthAllResult[1]=diagnosis;
        		
        			return preauthAllResult;
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
        			try // First try closing the result set
        			{
        				try
        				{
        					if (prs != null) prs.close();
        					if (drs != null) drs.close();
        					if (ars != null) ars.close();
        					if (shrs != null) shrs.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
        					throw new TTKException(sqlExp, "preauth");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (cStmtObject != null) cStmtObject.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
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
        							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
        							throw new TTKException(sqlExp, "preauth");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "preauth");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				prs = null;
        				drs = null;
        				ars = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        	}//end of getPreAuthDetails(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)
        	
            public ArrayList getActivityCodeList(ArrayList alSearchCriteria,String actOrDrug) throws TTKException {
        		Collection<Object> alResultList = new ArrayList<Object>();
        		Connection conn = null;
        		CallableStatement cStmtObject=null;
        		ResultSet rs = null;
        		ActivityDetailsVO activityDetailsVO = null;
        		try{
        			conn	=	ResourceManager.getConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strActivityCodeList);
        			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
        			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
        			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
        			cStmtObject.setLong(4,TTKCommon.getLong(alSearchCriteria.get(3).toString()));
        			cStmtObject.setString(5,(String)alSearchCriteria.get(4));	
        			cStmtObject.setString(6,(String)alSearchCriteria.get(5));//(String)alSearchCriteria.get(5)
        			cStmtObject.setString(7,(String)alSearchCriteria.get(6));//(String)alSearchCriteria.get(6)
        			cStmtObject.setString(8,(String)alSearchCriteria.get(7));
        			cStmtObject.setString(9,(String)alSearchCriteria.get(8));
        			cStmtObject.setString(10,(String)alSearchCriteria.get(9));
        			cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
        			cStmtObject.setString(12,actOrDrug);
        			cStmtObject.execute();
        			rs = (java.sql.ResultSet)cStmtObject.getObject(11);
        			if(rs != null){
        				while(rs.next()){
        					activityDetailsVO = new ActivityDetailsVO();					
        					activityDetailsVO.setActivityCode(rs.getString("ACTIVITY_CODE"));
        					activityDetailsVO.setActivityCodeDesc(rs.getString("ACTIVITY_DESCRIPTION"));
        					activityDetailsVO.setActivityDtlSeqId(rs.getLong("ACT_MAS_DTL_SEQ_ID"));;
        					if("TAR".equals((String)alSearchCriteria.get(2)))
        						activityDetailsVO.setTariffSeqId(rs.getLong("HOSP_TARIFF_SEQ_ID"));
        					if(rs.getString("INTERNAL_CODE")==null)
        						activityDetailsVO.setInternalCode("-");
        					else
        						activityDetailsVO.setInternalCode(rs.getString("INTERNAL_CODE"));
        					activityDetailsVO.setToothNoReqYN(rs.getString("TOOTH_REQ_YN"));
    						activityDetailsVO.setProviderRequestedAmt(rs.getBigDecimal("REQUESTED_AMT"));
    						activityDetailsVO.setActivityicdcountflag(rs.getLong("rcnt"));
    						
    						if("TAR".equals((String)alSearchCriteria.get(2))){
    							activityDetailsVO.setNetworkType(rs.getString("Network_Type"));
    						}
        					alResultList.add(activityDetailsVO);
        				}//end of while(rs.next())
        			}//end of if(rs != null)
        			
        			return (ArrayList)alResultList;
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlinepreauth");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlinepreauth");
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
        					throw new TTKException(sqlExp, "onlinepreauth");
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
        						throw new TTKException(sqlExp, "onlinepreauth");
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
        							throw new TTKException(sqlExp, "onlinepreauth");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "onlinepreauth");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        	}//end of getActivityCodeList(ArrayList alSearchCriteria)
            
            public ArrayList getDrugCodeList(ArrayList alSearchCriteria) throws TTKException {
        		Collection<Object> alResultList = new ArrayList<Object>();
        		Connection conn = null;
        		CallableStatement cStmtObject=null;
        		ResultSet rs = null;
        		DrugDetailsVO drugDetailsVO = null;
        		try{
        			conn	=	ResourceManager.getConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDrugCodeList);
        			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
        			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
        			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
        			cStmtObject.setLong(4,TTKCommon.getLong(alSearchCriteria.get(3).toString()));
        			cStmtObject.setString(5,(String)alSearchCriteria.get(4));	
        			cStmtObject.setString(6,"");//(String)alSearchCriteria.get(5)
        			cStmtObject.setString(7,"");//(String)alSearchCriteria.get(6)
        			cStmtObject.setLong(8,TTKCommon.getLong(alSearchCriteria.get(8).toString()));
        			cStmtObject.setLong(9,TTKCommon.getLong(alSearchCriteria.get(9).toString()));
        			cStmtObject.registerOutParameter(10,OracleTypes.CURSOR);

        			cStmtObject.execute();
        			rs = (java.sql.ResultSet)cStmtObject.getObject(10);
        			if(rs != null){
        				while(rs.next()){
        					drugDetailsVO = new DrugDetailsVO();					
        					drugDetailsVO.setDrugSeqId(rs.getLong("ACT_MAS_DTL_SEQ_ID"));			
        					drugDetailsVO.setDrugCode(rs.getString("ACTIVITY_CODE"));
        					drugDetailsVO.setDrugDesc(rs.getString("ACTIVITY_DESCRIPTION"));
        					drugDetailsVO.setGranularUnit(rs.getBigDecimal("GRANULAR_UNIT"));
        					if(rs.getBigDecimal("no_of_units") != null){
        						drugDetailsVO.setNoOfUnits(rs.getBigDecimal("no_of_units"));
        					}
        					alResultList.add(drugDetailsVO);
        				}//end of while(rs.next())
        			}//end of if(rs != null)
        			
        			return (ArrayList)alResultList;
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlinepreauth");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlinepreauth");
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
        					log.error("Error while closing the Resultset in PreAuthDAOImpl getDrugCodeList()",sqlExp);
        					throw new TTKException(sqlExp, "onlinepreauth");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (cStmtObject != null) cStmtObject.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in PreAuthDAOImpl getDrugCodeList()",sqlExp);
        						throw new TTKException(sqlExp, "onlinepreauth");
        					}//end of catch (SQLException sqlExp)
        					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        					{
        						try
        						{
        							if(conn != null) conn.close();
        						}//end of try
        						catch (SQLException sqlExp)
        						{
        							log.error("Error while closing the Connection in PreAuthDAOImpl getDrugCodeList()",sqlExp);
        							throw new TTKException(sqlExp, "onlinepreauth");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "onlinepreauth");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        	}//end of getDrugCodeList(ArrayList alSearchCriteria)
            
            
            
            public ArrayList getDiagCodeList(ArrayList alSearchCriteria) throws TTKException {
        		Collection<Object> alResultList = new ArrayList<Object>();
        		Connection conn = null;
        		CallableStatement cStmtObject=null;
        		ResultSet rs = null;
        		DrugDetailsVO drugDetailsVO = null;
        		try{
        			conn	=	ResourceManager.getConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDiagCodeList);
        			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
        			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
        			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        			cStmtObject.execute();
        			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
        			if(rs != null){
        				while(rs.next()){
        					drugDetailsVO = new DrugDetailsVO();					
        					drugDetailsVO.setDrugCode(rs.getString("ICD_CODE"));
        					drugDetailsVO.setDrugDesc(rs.getString("SHORT_DESC"));
        					alResultList.add(drugDetailsVO);
        				}//end of while(rs.next())
        			}//end of if(rs != null)
        			
        			return (ArrayList)alResultList;
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlinepreauth");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlinepreauth");
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
        					log.error("Error while closing the Resultset in PreAuthDAOImpl getDrugCodeList()",sqlExp);
        					throw new TTKException(sqlExp, "onlinepreauth");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (cStmtObject != null) cStmtObject.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in PreAuthDAOImpl getDrugCodeList()",sqlExp);
        						throw new TTKException(sqlExp, "onlinepreauth");
        					}//end of catch (SQLException sqlExp)
        					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        					{
        						try
        						{
        							if(conn != null) conn.close();
        						}//end of try
        						catch (SQLException sqlExp)
        						{
        							log.error("Error while closing the Connection in PreAuthDAOImpl getDrugCodeList()",sqlExp);
        							throw new TTKException(sqlExp, "onlinepreauth");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "onlinepreauth");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        	}//end of getDiagCodeList(ArrayList alSearchCriteria)
            
            
            
            public ActivityDetailsVO getActivityCodeDetails(Long hospSeqId,String enrollId,Long activityDtlSeqId,java.util.Date treatmentDate,String searchType,String actOrDrug) throws TTKException{
        		Connection conn 				=	null;
        		CallableStatement cStmtObject	=	null;
        		ResultSet rs 					=	null;
        		ActivityDetailsVO activityDetailsVO	=	null;
        		try{
        			
        			conn = ResourceManager.getConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strActivityCodeDetails);
        			
        			cStmtObject.setLong(1,hospSeqId);
        			cStmtObject.setString(2,enrollId);
        			if(activityDtlSeqId==null)
        				cStmtObject.setLong(3,0);
        			else
        				cStmtObject.setLong(3,activityDtlSeqId);
        			
        			//cStmtObject.setDate(4,(Date) treatmentDate);
        			cStmtObject.setTimestamp(4,new Timestamp(treatmentDate.getTime()));//TreatmentDate
        			cStmtObject.setString(5,searchType);
        			cStmtObject.setString(6,actOrDrug);
        			
        			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
        			cStmtObject.execute();
        			rs = (java.sql.ResultSet)cStmtObject.getObject(7);
        			if(rs != null){
        				if(rs.next()){
        					activityDetailsVO = new ActivityDetailsVO();					
        					activityDetailsVO.setActivityCode(TTKCommon.checkNull(rs.getString("ACTIVITY_CODE")));	
        					activityDetailsVO.setInternalCode(TTKCommon.checkNull(rs.getString("INTERNAL_CODE")));				
        					activityDetailsVO.setActivityCodeDesc(TTKCommon.checkNull(rs.getString("ACTIVITY_DESCRIPTION")));					
        					activityDetailsVO.setGrossAmount(TTKCommon.getBigDecimal(rs.getString("GROSS_AMOUNT")));
        					activityDetailsVO.setNetAmount(TTKCommon.getBigDecimal(rs.getString("GROSS_AMOUNT")));
        					activityDetailsVO.setDiscount(TTKCommon.getBigDecimal(rs.getString("DISC_AMOUNT")));			
        					Double	grossAmount	=		rs.getDouble("GROSS_AMOUNT");		
        					Double	discAmount	=		rs.getDouble("DISC_AMOUNT");
        					Double	netAmount	=		grossAmount-discAmount;
        					activityDetailsVO.setDiscountedGross(new BigDecimal(grossAmount));
        					activityDetailsVO.setNetAmount(new BigDecimal(netAmount));
        					activityDetailsVO.setActivityTypeId(rs.getString("ACTIVITY_TYPE_ID"));
        					activityDetailsVO.setToothNo(rs.getString("TOOTH_NO"));
        					activityDetailsVO.setInternalDesc(TTKCommon.checkNull(rs.getString("INTERNAL_DESC")));	
        					//activityDetailsVO.setActivitySeqId(rs.getLong("ACT_MAS_DTL_SEQ_ID"));
        				}//end of while(rs.next())
        			}//end of if(rs != null)
        			return activityDetailsVO;
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlinepreauth");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlinepreauth");
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
        					throw new TTKException(sqlExp, "onlinepreauth");
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
        						throw new TTKException(sqlExp, "onlinepreauth");
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
        							throw new TTKException(sqlExp, "onlinepreauth");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "onlinepreauth");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        	}//end of getActivityCodeDetails(Long hospSeqId,String mode,ActivityDetailsVO activityDetailsVO,CashlessDetailVO cashlessDetailVO) throws TTKException{
            
            
            
            public DrugDetailsVO getDrugCodeDetails(Long hospSeqId,String enrollId,Long drugSeqId,java.util.Date treatmentDate,String actOrDrug) throws TTKException{
        		Connection conn 				=	null;
        		CallableStatement cStmtObject	=	null;
        		ResultSet rs 					=	null;
        		DrugDetailsVO  drugDetailsVO	=	new DrugDetailsVO();
        		try{
        			conn = ResourceManager.getConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strActivityCodeDetails);
        			cStmtObject.setLong(1,hospSeqId);
        			cStmtObject.setString(2,enrollId);
        			cStmtObject.setLong(3,drugSeqId);
        			//cStmtObject.setDate(4,(Date) treatmentDate);
        			cStmtObject.setTimestamp(4,new Timestamp(treatmentDate.getTime()));//TreatmentDate
        			cStmtObject.setString(5,"");
        			cStmtObject.setString(6,actOrDrug);
        			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
        			cStmtObject.execute();
        			rs = (java.sql.ResultSet)cStmtObject.getObject(7);
        			if(rs != null){
        				if(rs.next()){
        					drugDetailsVO = new DrugDetailsVO();
        					drugDetailsVO.setDrugCode(TTKCommon.checkNull(rs.getString("ACTIVITY_CODE")));
        					drugDetailsVO.setDrugDesc(TTKCommon.checkNull(rs.getString("ACTIVITY_DESCRIPTION")));
        					if(rs.getBigDecimal("GROSS_AMOUNT") == null){
        						drugDetailsVO.setGrossAmount(new BigDecimal(0.00));
        					}else{
        					drugDetailsVO.setGrossAmount(rs.getBigDecimal("GROSS_AMOUNT"));
        					}
        					if(rs.getBigDecimal("DISC_AMOUNT")==null){
        						drugDetailsVO.setDiscount(new BigDecimal(0.00));
        					}else{
        						drugDetailsVO.setDiscount(rs.getBigDecimal("DISC_AMOUNT"));
        					}if(rs.getBigDecimal("DISC_GROSS_AMOUNT") == null){
        						drugDetailsVO.setNetPriceAftDisc(new BigDecimal(0.00));
        					}
        					else{
        					drugDetailsVO.setNetPriceAftDisc(rs.getBigDecimal("DISC_GROSS_AMOUNT"));
        					}
        					if(rs.getBigDecimal("UNIT_PRICE") == null){
        						drugDetailsVO.setUnitPrice(new BigDecimal(0.00));
        					}else{
        						drugDetailsVO.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));
        					}
        					if(rs.getBigDecimal("PACKAGE_PRICE")==null){
        						drugDetailsVO.setPackagePrice(new BigDecimal(0.00));
        					}else{
        						drugDetailsVO.setPackagePrice(rs.getBigDecimal("PACKAGE_PRICE"));
        					}
        					if(rs.getBigDecimal("GRANULAR_UNIT") == null){
        						drugDetailsVO.setGranularUnit(new BigDecimal(0.00));
        					}else{
        						drugDetailsVO.setGranularUnit(rs.getBigDecimal("GRANULAR_UNIT"));
        					}
        					if(rs.getBigDecimal("DISC_PERC")== null){
        						drugDetailsVO.setDisPrec(new BigDecimal(0.00));
        					}else{
        					drugDetailsVO.setDisPrec(rs.getBigDecimal("DISC_PERC"));
        					}
        				}//end of while(rs.next())
        			}//end of if(rs != null)
        			return drugDetailsVO;
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			throw new TTKException(sqlExp, "onlinepreauth");
        		}//end of catch (SQLException sqlExp)
        		catch (Exception exp)
        		{
        			throw new TTKException(exp, "onlinepreauth");
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
        					throw new TTKException(sqlExp, "onlinepreauth");
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
        						throw new TTKException(sqlExp, "onlinepreauth");
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
        							throw new TTKException(sqlExp, "onlinepreauth");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "onlinepreauth");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				rs = null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        	}//end of getDrugCodeDetails(Long hospSeqId,String mode,ActivityDetailsVO activityDetailsVO,CashlessDetailVO cashlessDetailVO) throws TTKException{

            
            
            public Object[] getOnlinePartialPreAuthDetails(Long preAuthSeqId) throws TTKException {
        		Connection conn = null;
        		CallableStatement cStmtObject=null;
        		ResultSet prs=null;ResultSet drs=null;ResultSet ars = null;ResultSet shrs = null, drugRs	=	null;
        		CashlessDetailVO cashlessDetailVO  = new CashlessDetailVO();
        		Object[] preauthAllResult=new Object[4];
        		ArrayList<DiagnosisDetailsVO> diagnosis=new ArrayList<DiagnosisDetailsVO>();
        		ArrayList<ActivityDetailsVO> activities=new ArrayList<ActivityDetailsVO>();
        		ArrayList<DrugDetailsVO> drugs=new ArrayList<DrugDetailsVO>();
        		ArrayList<String[]> shortfalls=null;
        		try{
        			conn = ResourceManager.getConnection(); 
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strOnlinePreAuthDetails);

        			cStmtObject.setLong(1,preAuthSeqId);			
        			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
        			cStmtObject.execute();
        			prs = (java.sql.ResultSet)cStmtObject.getObject(2);//preauth Details
        			drs = (java.sql.ResultSet)cStmtObject.getObject(3);//diagnosis Details
        			ars = (java.sql.ResultSet)cStmtObject.getObject(4);//activity Details
        			shrs = (java.sql.ResultSet)cStmtObject.getObject(6);//Shortfalls Details
        			drugRs = (java.sql.ResultSet)cStmtObject.getObject(7);//Drug Details
        						
        			if(prs != null){
        				if(prs.next()){
        					
        					cashlessDetailVO.setEnrollId(TTKCommon.checkNull(prs.getString("TPA_ENROLMENT_ID")));
        					cashlessDetailVO.setMemberName(TTKCommon.checkNull(prs.getString("MEM_NAME")));	
        					if(prs.getString("MEM_DOB") != null){
        						cashlessDetailVO.setMemDob(new Date(prs.getTimestamp("MEM_DOB").getTime()));
        					}
        					cashlessDetailVO.setGender(TTKCommon.checkNull(prs.getString("MEM_GENDER")));
        					if(prs.getString("HOSPITALIZATION_DATE") != null){
        						cashlessDetailVO.setTreatmentDate(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime()));
        					}
        					if(prs.getString("DISCHARGE_DATE") != null){
        						cashlessDetailVO.setDischargeDate(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime()));
        					}
        					cashlessDetailVO.setClinicianId(TTKCommon.checkNull(prs.getString("CLINICIAN_ID")));
        					cashlessDetailVO.setClinicianName(TTKCommon.checkNull(prs.getString("CLINICIAN_NAME")));
        					cashlessDetailVO.setSpeciality(TTKCommon.checkNull(prs.getString("CLINICIAN_SPECIALITY")));	
        					cashlessDetailVO.setConsultation(TTKCommon.checkNull(prs.getString("CONSULTATION_TYPE")));
        					cashlessDetailVO.setPreAuthRefNo(TTKCommon.checkNull(prs.getString("PL_PREAUTH_REFNO")));	
        					cashlessDetailVO.setPresentingComplaints(TTKCommon.checkNull(prs.getString("PRESENTING_COMPLAINTS")));	
        					cashlessDetailVO.setPastHistory(TTKCommon.checkNull(prs.getString("PAST_HISTORY")));
        					cashlessDetailVO.setAilmentDurationText(TTKCommon.checkNull(prs.getString("DURATION_ABOVE_ILLNESS")));	
        					cashlessDetailVO.setAilmentDuration(TTKCommon.checkNull(prs.getString("DURATION_ABOVE_ILLNESS_TIME")));	
        					cashlessDetailVO.setIllnessDurationText(TTKCommon.checkNull(prs.getString("SINCE_WHEN")));
        					cashlessDetailVO.setIllnessDuration(TTKCommon.checkNull(prs.getString("SINCE_WHEN_TIME")));		
        					cashlessDetailVO.setBenifitType(TTKCommon.checkNull(prs.getString("BENIFIT_TYPE")));
        					cashlessDetailVO.setBenifitTypeCode(TTKCommon.checkNull(prs.getString("BENIFIT_TYPE_CODE")));
        					cashlessDetailVO.setTreatmentType(TTKCommon.checkNull(prs.getString("TREATMENT_TYPE")));
        					cashlessDetailVO.setEncounterType(TTKCommon.checkNull(prs.getString("ENCOUNTER_TYPE_ID")));        					
        					cashlessDetailVO.setMemberSeqID(prs.getLong("MEMBER_SEQ_ID"));	
        					cashlessDetailVO.setPreAuthSeqID(preAuthSeqId);
        					cashlessDetailVO.setFileName(TTKCommon.checkNull(prs.getString("FILE_NAME")));
        					cashlessDetailVO.setPreAuthNo(TTKCommon.checkNull(prs.getString("PRE_AUTH_NUMBER")));
        					cashlessDetailVO.setEmirateID(TTKCommon.checkNull(prs.getString("EMIRATE_ID")));
        					cashlessDetailVO.setTotalAgreedRate(prs.getBigDecimal("TOT_APPROVED_AMOUNT"));
        			//		cashlessDetailVO.setStatus(TTKCommon.checkNull(prs.getString("STATUS")));
        					
                         	if("INP-APL".equals(prs.getString("IN_PROGESS_STATUS")))
                         	{	
                         		cashlessDetailVO.setStatus("In-Progress-Appeal");
                         	}
                         	else
                         	{
                         		cashlessDetailVO.setStatus(TTKCommon.checkNull(prs.getString("STATUS")));	
                         	}
                         	cashlessDetailVO.setAppealComments(TTKCommon.checkNull(prs.getString("Appeal_Remark")));
                         	
        					cashlessDetailVO.setStatusTypeID(TTKCommon.checkNull(prs.getString("STATUS_TYPE")));
        					cashlessDetailVO.setAuthorizationNo(TTKCommon.checkNull(prs.getString("AUTH_NUMBER")));
        					cashlessDetailVO.setPolicyNo(TTKCommon.checkNull(prs.getString("POLICY_NUMBER")));
        					if(prs.getString("DATE_OF_LMP") != null){
        						cashlessDetailVO.setDateOfLMP(new Date(prs.getTimestamp("DATE_OF_LMP").getTime()));
        					}        					
        					cashlessDetailVO.setNatureOfConception(TTKCommon.checkNull(prs.getString("CONCEPTION_TYPE")));
        					cashlessDetailVO.setSystemOfMedicine(TTKCommon.checkNull(prs.getString("SYSTEM_OF_MEDICINE")));
        					cashlessDetailVO.setModeofdelivery(TTKCommon.checkNull(prs.getString("DELVRY_MOD_TYPE")));
        					cashlessDetailVO.setAppealDocsYN(TTKCommon.checkNull(prs.getString("docs_available_yn")));		// ref
        					
        					cashlessDetailVO.setTreatmentType(prs.getString("TREATMENT_TYPE"));	
        					cashlessDetailVO.setEventReferenceNo(prs.getString("event_no"));	
        					if(prs.getString("SUBMISSION_DATE")!=null)
        						cashlessDetailVO.setSubmissionDt(TTKCommon.getFormattedDate(TTKCommon.getString(prs.getString("SUBMISSION_DATE"))));
        					else
        						cashlessDetailVO.setSubmissionDt("");
        					
        					if(prs.getString("DECISION_DATE")!=null)
        						cashlessDetailVO.setDecisionDt(TTKCommon.getFormattedDate(TTKCommon.getString(prs.getString("DECISION_DATE"))));
        					else
        						cashlessDetailVO.setDecisionDt("");
        					DentalOrthoVO orthoVO	=	new DentalOrthoVO();
        					orthoVO.setDentoSeqid(prs.getLong("ORTHO_SEQ_ID"));
        					orthoVO.setPreAuthSeqid(prs.getLong("SOURCE_SEQ_ID"));
        					orthoVO.setDentoclass1(prs.getString("DENTO_CLASS_I"));
        					orthoVO.setDentoclass2(prs.getString("DENTO_CLASS_II"));
        					orthoVO.setDentoclass2Text(prs.getString("DENTO_CLASS_II_TEXT"));
        					orthoVO.setDentoclass3(prs.getString("DENTO_CLASS_III"));
        					orthoVO.setDentoclass3Text(prs.getString("DENTO_CLASS_III_TEXT"));
        					orthoVO.setSkeletalClass1(prs.getString("SKELE_CLASS_I"));
        					orthoVO.setSkeletalClass2(prs.getString("SKELE_CLASS_II"));
        					orthoVO.setSkeletalClass3(prs.getString("SKELE_CLASS_III"));
        					orthoVO.setOverJet(prs.getString("OVERJET_MM"));
        					orthoVO.setReverseJet(prs.getString("REV_OVERJET_MM"));
        					orthoVO.setReverseJetYN(prs.getString("REV_OVERJET_YN"));
        					orthoVO.setCrossbiteAntrio(prs.getString("CROSSBITE_ANT"));
        					orthoVO.setCrossbitePosterior(prs.getString("CROSSBITE_POST"));
        					orthoVO.setCrossbiteRetrucontract(prs.getString("CROSSBITE_BETW"));
        					orthoVO.setOpenbiteAntrio(prs.getString("OPENBIT_ANT"));
        					orthoVO.setOpenbitePosterior(prs.getString("OPENBIT_POST"));
        					orthoVO.setOpenbiteLateral(prs.getString("OPENBIT_LATE"));
        					orthoVO.setContactPointDisplacement(prs.getString("CONT_POINT_DISP"));
        					orthoVO.setOverBite(prs.getString("OVERBITE"));
        					orthoVO.setOverbitePalatalYN(prs.getString("OVERBIT_PATA"));
        					orthoVO.setOverbiteGingivalYN(prs.getString("OVERBIT_GING"));
        					orthoVO.setHypodontiaQuand1Teeth(prs.getString("HYPO_QUAD1"));
        					orthoVO.setHypodontiaQuand2Teeth(prs.getString("HYPO_QUAD2"));
        					orthoVO.setHypodontiaQuand3Teeth(prs.getString("HYPO_QUAD3"));
        					orthoVO.setHypodontiaQuand4Teeth(prs.getString("HYPO_QUAD4"));
        					orthoVO.setImpededTeethEruptionNo(prs.getString("OTHERS_IMPEDED"));
        					orthoVO.setImpededTeethNo(prs.getString("OTHERS_IMPACT"));
        					orthoVO.setSubmergerdTeethNo(prs.getString("OTHERS_SUBMERG"));
        					orthoVO.setSupernumeryTeethNo(prs.getString("OTHERS_SUPERNUM"));
        					orthoVO.setRetainedTeethNo(prs.getString("OTHERS_RETAINE"));
        					orthoVO.setEctopicTeethNo(prs.getString("OTHERS_ECTOPIC"));
        					orthoVO.setCranioFacialNo(prs.getString("OTHERS_CRANIO"));
        					orthoVO.setCrossbiteAntriomm(prs.getString("CROSSBITE_ANT_MM"));
        					orthoVO.setCrossbitePosteriormm(prs.getString("CROSSBITE_PRST_MM"));
        					orthoVO.setCrossbiteRetrucontractmm(prs.getString("CROSSBITE_BETW_MM"));
        					orthoVO.setContactPointDisplacementmm(prs.getString("CONT_POINT_DISP_MM"));
        					orthoVO.setAestheticComp(prs.getString("AC_MARKS"));
        					orthoVO.setIotn(prs.getString("IOTN"));
        					
        					cashlessDetailVO.setDentalOrthoVO(orthoVO);
        					cashlessDetailVO.setSubmitEN(prs.getString("SUBMIT_EN"));
        					cashlessDetailVO.setPreApprovalAmt(prs.getBigDecimal("TOT_DISC_GROSS_AMOUNT"));
        					cashlessDetailVO.setSourceTypeId(TTKCommon.checkNull(prs.getString("SOURCE_TYPE_ID")));
        					}//prs while
        				}//prs if
        		if(drs!=null){
        		while(drs.next()){
        			String diagCode=drs.getString("DIAGNOSYS_CODE")==null?"":drs.getString("DIAGNOSYS_CODE");
        			String desc=drs.getString("ICD_DESCRIPTION")==null?"":drs.getString("ICD_DESCRIPTION");
        			String primAil=drs.getString("PRIMARY_AILMENT_YN")==null?"":drs.getString("PRIMARY_AILMENT_YN");
        			Long diagSeqId=drs.getLong("DIAG_SEQ_ID");
        			Long icdCodeSeqId=drs.getLong("ICD_CODE_SEQ_ID");			
        			diagnosis.add(new DiagnosisDetailsVO(diagCode,desc,primAil,diagSeqId,icdCodeSeqId));
        		}
        		}
        		if(ars!=null){
        			while(ars.next()){
        				ActivityDetailsVO activityDetailsVO=new ActivityDetailsVO();
        				activityDetailsVO.setActivityCode(TTKCommon.checkNull(ars.getString("ACTIVITY_CODE")));
        				activityDetailsVO.setInternalCode(TTKCommon.checkNull(ars.getString("INTERNAL_CODE")));
        				activityDetailsVO.setActivityCodeDesc(TTKCommon.checkNull(ars.getString("ACTIVITY_DESCRIPTION")));
        				activityDetailsVO.setGrossAmount(TTKCommon.getBigDecimal(ars.getString("GROSS_AMOUNT")));
        				activityDetailsVO.setDiscount(TTKCommon.getBigDecimal(ars.getString("DISCOUNT_AMOUNT")));
        				activityDetailsVO.setNetAmount(TTKCommon.getBigDecimal(ars.getString("NET_AMOUNT")));
        				activityDetailsVO.setQuantity(ars.getFloat("QUANTITY"));
        				activityDetailsVO.setApprovedAmount(TTKCommon.getBigDecimal(ars.getString("APPROVED_AMT")));
        				activityDetailsVO.setActivitySeqId(ars.getLong("ACTIVITY_DTL_SEQ_ID"));
        				activityDetailsVO.setActivityStatus(TTKCommon.checkNull(ars.getString("STATUS")));
        				activityDetailsVO.setProviderRequestedAmt(TTKCommon.getBigDecimal(ars.getString("DISC_GROSS_AMOUNT")));
        				activityDetailsVO.setPatientShare(TTKCommon.getBigDecimal(ars.getString("PATIENT_SHARE")));
        				activityDetailsVO.setMedicationDays(ars.getInt("DURATION"));
        				activityDetailsVO.setDenialCode(TTKCommon.checkNull(ars.getString("DENIAL")));
        				activityDetailsVO.setActivityRemarks(TTKCommon.checkNull(ars.getString("REMARKS")));
        				activityDetailsVO.setToothNo(TTKCommon.checkNull(ars.getString("TOOTH_NO")));
        				activityDetailsVO.setInternalDesc(TTKCommon.checkNull(ars.getString("INTERNAL_DESC")));     
        				activityDetailsVO.setUnitPrice(TTKCommon.getBigDecimal(ars.getString("UNIT_PRICE")));				        				
        				activityDetailsVO.setOverrideYN(TTKCommon.checkNull(ars.getString("OVERRIDE_YN")));	
        				activityDetailsVO.setOverrideRemarks(TTKCommon.checkNull(ars.getString("OVERRIDE_REMARKS")));
        				activityDetailsVO.setDenialCode(TTKCommon.checkNull(ars.getString("DENIAL_CODE")));
        				activityDetailsVO.setDenialDescription(TTKCommon.checkNull(ars.getString("DENIAL_DESC")));
        				activityDetailsVO.setOverrideRemarksCode(TTKCommon.checkNull(ars.getString("OVERRIDE_REMARKS_CODE")));
        				activityDetailsVO.setOtherRemarks(TTKCommon.checkNull(ars.getString("OTHER_REMARKS")));
        				//activityDetailsVO.setAllowedAmount(TTKCommon.getBigDecimal(ars.getString("ALLOWED_AMOUNT")));	
        				//activityDetailsVO.setApprovedAmount(TTKCommon.getBigDecimal(ars.getString("APPROVED_AMOUNT")));
        				//activityDetailsVO.setActivityTypeId(TTKCommon.checkNull(ars.getString("ACTIVITY_TYPE_ID")));	 				
        				activities.add(activityDetailsVO);
        			}	
        		}if(drugRs!=null){
        			while(drugRs.next()){
        				DrugDetailsVO drugDetailsVO	=	new DrugDetailsVO();
        				drugDetailsVO.setDrugCode(drugRs.getString("ACTIVITY_CODE"));
        				drugDetailsVO.setDrugDesc(drugRs.getString("ACTIVITY_DESCRIPTION"));
        				drugDetailsVO.setGrossAmount(drugRs.getBigDecimal("GROSS_AMOUNT"));
        				drugDetailsVO.setDiscount(drugRs.getBigDecimal("DISCOUNT_AMOUNT"));
        				drugDetailsVO.setNetPriceAftDisc(drugRs.getBigDecimal("NET_AMOUNT"));
        				drugDetailsVO.setDrugqty(drugRs.getFloat("QUANTITY"));
        				drugDetailsVO.setTtlAmnt(drugRs.getBigDecimal("NET_AMOUNT"));
        				drugDetailsVO.setDrugSeqId(drugRs.getLong("ACTIVITY_DTL_SEQ_ID"));
        				drugDetailsVO.setPosology(drugRs.getString("POSOLOGY"));
        				drugDetailsVO.setDays(drugRs.getString("POSOLOGY_DURATION"));
        				drugDetailsVO.setDrugunit(drugRs.getString("UNIT_TYPE"));

        				drugDetailsVO.setRequestedAmount(drugRs.getBigDecimal("DISC_GROSS_AMOUNT"));
        				drugDetailsVO.setApprovedAmount(drugRs.getBigDecimal("APPROVED_AMOUNT"));
        				drugDetailsVO.setPateintShare(drugRs.getBigDecimal("PATIENT_SHARE"));
        				drugDetailsVO.setDenial(drugRs.getString("DENIAL"));
        				drugDetailsVO.setRemarks(drugRs.getString("REMARKS"));
        				drugDetailsVO.setStatus(drugRs.getString("STATUS"));
        				drugDetailsVO.setUnitPrice(drugRs.getBigDecimal("UNIT_PRICE"));
        				drugs.add(drugDetailsVO);
        			}	
        		}
        			
        			preauthAllResult[0]=cashlessDetailVO;
        			preauthAllResult[1]=diagnosis;
        			preauthAllResult[2]=activities;
        			preauthAllResult[3]=drugs;
        		
        			return preauthAllResult;
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
        			try // First try closing the result set
        			{
        				try
        				{
        					if (prs != null) prs.close();
        					if (drs != null) drs.close();
        					if (ars != null) ars.close();
        					if (shrs != null) shrs.close();
        					if (drugRs != null) drugRs.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
        					throw new TTKException(sqlExp, "preauth");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (cStmtObject != null) cStmtObject.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
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
        							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
        							throw new TTKException(sqlExp, "preauth");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "preauth");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				prs = null;
        				drs = null;
        				ars = null;
        				shrs=null;
        				drugRs=null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        	}//end of getOnlinePartialPreAuthDetails(Long preAuthSeqId)

            public Object[] getOnlinePartnerPartialPreAuthDetails(Long preAuthSeqId) throws TTKException {
        		Connection conn = null;
        		CallableStatement cStmtObject=null;
        		ResultSet prs=null;ResultSet drs=null;ResultSet ars = null;ResultSet shrs = null, drugRs	=	null,partnerDetails = null;
        		CashlessDetailVO cashlessDetailVO  = new CashlessDetailVO();
        		Object[] preauthAllResult=new Object[4];
        		ArrayList<DiagnosisDetailsVO> diagnosis=new ArrayList<DiagnosisDetailsVO>();
        		ArrayList<ActivityDetailsVO> activities=new ArrayList<ActivityDetailsVO>();
        		ArrayList<DrugDetailsVO> drugs=new ArrayList<DrugDetailsVO>();
        		ArrayList<String[]> shortfalls=null;
        		try{
        			conn = ResourceManager.getConnection(); 
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strOnlinePartnerPreAuthDetails);

        			cStmtObject.setLong(1,preAuthSeqId);			
        			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
        			cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
        			
        			cStmtObject.execute();
        			prs = (java.sql.ResultSet)cStmtObject.getObject(2);//preauth Details
        			drs = (java.sql.ResultSet)cStmtObject.getObject(3);//diagnosis Details
        			ars = (java.sql.ResultSet)cStmtObject.getObject(4);//activity Details
        			shrs = (java.sql.ResultSet)cStmtObject.getObject(6);//Shortfalls Details
        			drugRs = (java.sql.ResultSet)cStmtObject.getObject(7);//Drug Details
        			partnerDetails = (java.sql.ResultSet)cStmtObject.getObject(8);//Partner Details
        						
        			if(prs != null){
        				if(prs.next()){

        					cashlessDetailVO.setEnrollId(TTKCommon.checkNull(prs.getString("TPA_ENROLMENT_ID")));
        					cashlessDetailVO.setMemberName(TTKCommon.checkNull(prs.getString("MEM_NAME")));	
        					if(prs.getString("MEM_DOB") != null){
        						cashlessDetailVO.setMemDob(new Date(prs.getTimestamp("MEM_DOB").getTime()));
        					}
        					cashlessDetailVO.setGender(TTKCommon.checkNull(prs.getString("MEM_GENDER")));
        					
        					if(prs.getString("HOSPITALIZATION_DATE") != null){
        						cashlessDetailVO.setTreatmentDate(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime()));
        						cashlessDetailVO.setReceiveTime(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ")[1]:"");
        						cashlessDetailVO.setReceiveDay(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ")[2]:"");
        					}
        					if(prs.getString("DISCHARGE_DATE") != null){
        						cashlessDetailVO.setDischargeDate(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime()));
        						cashlessDetailVO.setDischargeTime(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ")[1]:"");
        						cashlessDetailVO.setDischargeDay(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ")[2]:"");
        					}
        					cashlessDetailVO.setClinicianId(TTKCommon.checkNull(prs.getString("CLINICIAN_ID")));
        					cashlessDetailVO.setClinicianName(TTKCommon.checkNull(prs.getString("CLINICIAN_NAME")));
        					cashlessDetailVO.setSpeciality(TTKCommon.checkNull(prs.getString("CLINICIAN_SPECIALITY")));	
        					cashlessDetailVO.setConsultation(TTKCommon.checkNull(prs.getString("CONSULTATION_TYPE")));
        					cashlessDetailVO.setPreAuthRefNo(TTKCommon.checkNull(prs.getString("PL_PREAUTH_REFNO")));	
        					cashlessDetailVO.setPresentingComplaints(TTKCommon.checkNull(prs.getString("PRESENTING_COMPLAINTS")));	
        					cashlessDetailVO.setPastHistory(TTKCommon.checkNull(prs.getString("PAST_HISTORY")));
        					cashlessDetailVO.setAilmentDurationText(TTKCommon.checkNull(prs.getString("DURATION_ABOVE_ILLNESS")));	
        					cashlessDetailVO.setAilmentDuration(TTKCommon.checkNull(prs.getString("DURATION_ABOVE_ILLNESS_TIME")));	
        					cashlessDetailVO.setIllnessDurationText(TTKCommon.checkNull(prs.getString("SINCE_WHEN")));
        					cashlessDetailVO.setIllnessDuration(TTKCommon.checkNull(prs.getString("SINCE_WHEN_TIME")));		
        					cashlessDetailVO.setBenifitType(TTKCommon.checkNull(prs.getString("BENIFIT_TYPE")));
        					cashlessDetailVO.setBenifitTypeCode(TTKCommon.checkNull(prs.getString("BENIFIT_TYPE_CODE")));
        					cashlessDetailVO.setTreatmentType(TTKCommon.checkNull(prs.getString("TREATMENT_TYPE")));
        					cashlessDetailVO.setEncounterType(TTKCommon.checkNull(prs.getString("ENCOUNTER_TYPE_ID")));        					
        					cashlessDetailVO.setMemberSeqID(prs.getLong("MEMBER_SEQ_ID"));	
        					cashlessDetailVO.setPreAuthSeqID(preAuthSeqId);
        					cashlessDetailVO.setFileName(TTKCommon.checkNull(prs.getString("FILE_NAME")));
        					cashlessDetailVO.setPreAuthNo(TTKCommon.checkNull(prs.getString("PRE_AUTH_NUMBER")));
        					cashlessDetailVO.setEmirateID(TTKCommon.checkNull(prs.getString("EMIRATE_ID")));
        					cashlessDetailVO.setTotalAgreedRate(prs.getBigDecimal("TOT_APPROVED_AMOUNT"));
        					cashlessDetailVO.setStatus(TTKCommon.checkNull(prs.getString("STATUS")));;
        					cashlessDetailVO.setStatusTypeID(TTKCommon.checkNull(prs.getString("STATUS_TYPE")));
        					cashlessDetailVO.setAuthorizationNo(TTKCommon.checkNull(prs.getString("AUTH_NUMBER")));
        					cashlessDetailVO.setPolicyNo(TTKCommon.checkNull(prs.getString("POLICY_NUMBER")));
        				
        					if(prs.getString("DATE_OF_LMP") != null){
        						cashlessDetailVO.setDateOfLMP(new Date(prs.getTimestamp("DATE_OF_LMP").getTime()));
        					}        					
        					cashlessDetailVO.setNatureOfConception(TTKCommon.checkNull(prs.getString("CONCEPTION_TYPE")));
        					cashlessDetailVO.setSystemOfMedicine(TTKCommon.checkNull(prs.getString("SYSTEM_OF_MEDICINE")));        					
        					cashlessDetailVO.setModeofdelivery(TTKCommon.checkNull(prs.getString("DELVRY_MOD_TYPE")));
        					cashlessDetailVO.setSubmissionDt(TTKCommon.checkNull(prs.getString("SUBMISSION_DATE")));
        					cashlessDetailVO.setDecisionDt(TTKCommon.checkNull(prs.getString("DECISION_DATE")));
        					
        					DentalOrthoVO orthoVO	=	new DentalOrthoVO();
        					orthoVO.setDentoSeqid(prs.getLong("ORTHO_SEQ_ID"));
        					orthoVO.setPreAuthSeqid(prs.getLong("SOURCE_SEQ_ID"));
        					orthoVO.setDentoclass1(prs.getString("DENTO_CLASS_I"));
        					orthoVO.setDentoclass2(prs.getString("DENTO_CLASS_II"));
        					orthoVO.setDentoclass2Text(prs.getString("DENTO_CLASS_II_TEXT"));
        					orthoVO.setDentoclass3(prs.getString("DENTO_CLASS_III"));
        					orthoVO.setDentoclass3Text(prs.getString("DENTO_CLASS_III_TEXT"));
        					orthoVO.setSkeletalClass1(prs.getString("SKELE_CLASS_I"));
        					orthoVO.setSkeletalClass2(prs.getString("SKELE_CLASS_II"));
        					orthoVO.setSkeletalClass3(prs.getString("SKELE_CLASS_III"));
        					orthoVO.setOverJet(prs.getString("OVERJET_MM"));
        					orthoVO.setReverseJet(prs.getString("REV_OVERJET_MM"));
        					orthoVO.setReverseJetYN(prs.getString("REV_OVERJET_YN"));
        					orthoVO.setCrossbiteAntrio(prs.getString("CROSSBITE_ANT"));
        					orthoVO.setCrossbitePosterior(prs.getString("CROSSBITE_POST"));
        					orthoVO.setCrossbiteRetrucontract(prs.getString("CROSSBITE_BETW"));
        					orthoVO.setOpenbiteAntrio(prs.getString("OPENBIT_ANT"));
        					orthoVO.setOpenbitePosterior(prs.getString("OPENBIT_POST"));
        					orthoVO.setOpenbiteLateral(prs.getString("OPENBIT_LATE"));
        					orthoVO.setContactPointDisplacement(prs.getString("CONT_POINT_DISP"));
        					orthoVO.setOverBite(prs.getString("OVERBITE"));
        					orthoVO.setOverbitePalatalYN(prs.getString("OVERBIT_PATA"));
        					orthoVO.setOverbiteGingivalYN(prs.getString("OVERBIT_GING"));
        					orthoVO.setHypodontiaQuand1Teeth(prs.getString("HYPO_QUAD1"));
        					orthoVO.setHypodontiaQuand2Teeth(prs.getString("HYPO_QUAD2"));
        					orthoVO.setHypodontiaQuand3Teeth(prs.getString("HYPO_QUAD3"));
        					orthoVO.setHypodontiaQuand4Teeth(prs.getString("HYPO_QUAD4"));
        					orthoVO.setImpededTeethEruptionNo(prs.getString("OTHERS_IMPEDED"));
        					orthoVO.setImpededTeethNo(prs.getString("OTHERS_IMPACT"));
        					orthoVO.setSubmergerdTeethNo(prs.getString("OTHERS_SUBMERG"));
        					orthoVO.setSupernumeryTeethNo(prs.getString("OTHERS_SUPERNUM"));
        					orthoVO.setRetainedTeethNo(prs.getString("OTHERS_RETAINE"));
        					orthoVO.setEctopicTeethNo(prs.getString("OTHERS_ECTOPIC"));
        					orthoVO.setCranioFacialNo(prs.getString("OTHERS_CRANIO"));
        					orthoVO.setCrossbiteAntriomm(prs.getString("CROSSBITE_ANT_MM"));
        					orthoVO.setCrossbitePosteriormm(prs.getString("CROSSBITE_PRST_MM"));
        					orthoVO.setCrossbiteRetrucontractmm(prs.getString("CROSSBITE_BETW_MM"));
        					orthoVO.setContactPointDisplacementmm(prs.getString("CONT_POINT_DISP_MM"));
        					orthoVO.setAestheticComp(prs.getString("AC_MARKS"));
        					orthoVO.setIotn(prs.getString("IOTN"));
        					
        					cashlessDetailVO.setDentalOrthoVO(orthoVO);
        					
        					}//prs while
        				}//prs if
        		if(drs!=null){
        		while(drs.next()){
        			String diagCode=drs.getString("DIAGNOSYS_CODE")==null?"":drs.getString("DIAGNOSYS_CODE");
        			String desc=drs.getString("ICD_DESCRIPTION")==null?"":drs.getString("ICD_DESCRIPTION");
        			String primAil=drs.getString("PRIMARY_AILMENT_YN")==null?"":drs.getString("PRIMARY_AILMENT_YN");
        			Long diagSeqId=drs.getLong("DIAG_SEQ_ID");
        			Long icdCodeSeqId=drs.getLong("ICD_CODE_SEQ_ID");			
        			diagnosis.add(new DiagnosisDetailsVO(diagCode,desc,primAil,diagSeqId,icdCodeSeqId));
        		}
        		}
        		if(ars!=null){
        			while(ars.next()){
        				ActivityDetailsVO activityDetailsVO=new ActivityDetailsVO();
        				activityDetailsVO.setActivityCode(TTKCommon.checkNull(ars.getString("ACTIVITY_CODE")));
        				activityDetailsVO.setInternalCode(TTKCommon.checkNull(ars.getString("INTERNAL_CODE")));
        				activityDetailsVO.setActivityCodeDesc(TTKCommon.checkNull(ars.getString("ACTIVITY_DESCRIPTION")));
        				activityDetailsVO.setGrossAmount(TTKCommon.getBigDecimal(ars.getString("GROSS_AMOUNT")));
        				activityDetailsVO.setDiscount(TTKCommon.getBigDecimal(ars.getString("DISCOUNT_AMOUNT")));
        				activityDetailsVO.setNetAmount(TTKCommon.getBigDecimal(ars.getString("NET_AMOUNT")));
        				activityDetailsVO.setQuantity(ars.getFloat("QUANTITY"));
        				activityDetailsVO.setApprovedAmount(TTKCommon.getBigDecimal(ars.getString("APPROVED_AMT")));
        				activityDetailsVO.setActivitySeqId(ars.getLong("ACTIVITY_DTL_SEQ_ID"));
        				activityDetailsVO.setActivityStatus(TTKCommon.checkNull(ars.getString("STATUS")));
        				activityDetailsVO.setProviderRequestedAmt(TTKCommon.getBigDecimal(ars.getString("DISC_GROSS_AMOUNT")));
        				activityDetailsVO.setPatientShare(TTKCommon.getBigDecimal(ars.getString("PATIENT_SHARE")));
        				activityDetailsVO.setMedicationDays(ars.getInt("DURATION"));
        				activityDetailsVO.setDenialCode(TTKCommon.checkNull(ars.getString("DENIAL")));
        				activityDetailsVO.setActivityRemarks(TTKCommon.checkNull(ars.getString("REMARKS")));
        				activityDetailsVO.setToothNo(TTKCommon.checkNull(ars.getString("TOOTH_NO")));
        				activityDetailsVO.setInternalDesc(TTKCommon.checkNull(ars.getString("INTERNAL_DESC")));
        				activityDetailsVO.setUnitPrice(TTKCommon.getBigDecimal(ars.getString("UNIT_PRICE")));
        				activities.add(activityDetailsVO);
        			}	
        		}if(drugRs!=null){
        			while(drugRs.next()){
        				DrugDetailsVO drugDetailsVO	=	new DrugDetailsVO();
        				drugDetailsVO.setDrugCode(drugRs.getString("ACTIVITY_CODE"));
        				drugDetailsVO.setDrugDesc(drugRs.getString("ACTIVITY_DESCRIPTION"));
        				drugDetailsVO.setGrossAmount(drugRs.getBigDecimal("GROSS_AMOUNT"));
        				drugDetailsVO.setDiscount(drugRs.getBigDecimal("DISCOUNT_AMOUNT"));
        				drugDetailsVO.setNetPriceAftDisc(drugRs.getBigDecimal("NET_AMOUNT"));
        				drugDetailsVO.setDrugqty(drugRs.getFloat("QUANTITY"));
        				drugDetailsVO.setTtlAmnt(drugRs.getBigDecimal("NET_AMOUNT"));
        				drugDetailsVO.setDrugSeqId(drugRs.getLong("ACTIVITY_DTL_SEQ_ID"));
        				drugDetailsVO.setPosology(drugRs.getString("POSOLOGY"));
        				drugDetailsVO.setDays(drugRs.getString("POSOLOGY_DURATION"));
        				drugDetailsVO.setDrugunit(drugRs.getString("UNIT_TYPE"));

        				drugDetailsVO.setRequestedAmount(drugRs.getBigDecimal("DISC_GROSS_AMOUNT"));
        				drugDetailsVO.setApprovedAmount(drugRs.getBigDecimal("APPROVED_AMOUNT"));
        				drugDetailsVO.setPateintShare(drugRs.getBigDecimal("PATIENT_SHARE"));
        				drugDetailsVO.setDenial(drugRs.getString("DENIAL"));
        				drugDetailsVO.setRemarks(drugRs.getString("REMARKS"));
        				drugDetailsVO.setStatus(drugRs.getString("STATUS"));
        				
        				drugs.add(drugDetailsVO);
        			}	
        		}
        		
        		if(partnerDetails!=null){
        			while(partnerDetails.next()){
        				//partner details
    					cashlessDetailVO.setPartnerGenSeqId(partnerDetails.getLong("ONL_PAT_AUTH_SEQ_ID"));
    					cashlessDetailVO.setEnrollId(partnerDetails.getString("TPA_ENROLLMENT_ID"));
    					
//    					cashlessDetailVO.setTreatmentDate(TTKCommon.getUtilDate(partnerDetails.getString("ADMISSION_DATE")));
    					cashlessDetailVO.setTreatmentDate(new Date(partnerDetails.getTimestamp("ADMISSION_DATE").getTime()));
						cashlessDetailVO.setReceiveTime(TTKCommon.getFormattedDateHour(new Date(partnerDetails.getTimestamp("ADMISSION_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(partnerDetails.getTimestamp("ADMISSION_DATE").getTime())).split(" ")[1]:"");
						cashlessDetailVO.setReceiveDay(TTKCommon.getFormattedDateHour(new Date(partnerDetails.getTimestamp("ADMISSION_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(partnerDetails.getTimestamp("ADMISSION_DATE").getTime())).split(" ")[2]:"");
//    					
						cashlessDetailVO.setDischargeDate(new Date(partnerDetails.getTimestamp("DISCHARGE_DATE").getTime()));
						cashlessDetailVO.setDischargeTime(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ")[1]:"");
						cashlessDetailVO.setDischargeDay(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ")[2]:"");
    					cashlessDetailVO.setEstimatedCost(partnerDetails.getLong("ESTIMATED_AMOUNT"));
    					cashlessDetailVO.setProviderName(partnerDetails.getString("HOSP_NAME"));
    					cashlessDetailVO.setCountry(partnerDetails.getString("COUNTRY_NAME"));
    					cashlessDetailVO.setCurrency(partnerDetails.getString("CURRENCY_ID"));
    					cashlessDetailVO.setLineOfTreatment(partnerDetails.getString("LINE_OF_TREATMENT"));
    					cashlessDetailVO.setPtrEncounterType(partnerDetails.getString("ENCOUNTER_START_TYPE"));
    					cashlessDetailVO.setTreatmentType(TTKCommon.checkNull(prs.getString("ENCOUNTER_START_TYPE")));
        			}
        		}
        			
        			preauthAllResult[0]=cashlessDetailVO;
        			preauthAllResult[1]=diagnosis;
        			preauthAllResult[2]=activities;
        			preauthAllResult[3]=drugs;
        		
        			return preauthAllResult;
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
        			try // First try closing the result set
        			{
        				try
        				{
        					if (prs != null) prs.close();
        					if (drs != null) drs.close();
        					if (ars != null) ars.close();
        					if (shrs != null) shrs.close();
        					if(drugRs!=null)drugRs.close();
        					if(partnerDetails!=null) partnerDetails.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
        					throw new TTKException(sqlExp, "preauth");
        				}//end of catch (SQLException sqlExp)
        				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        				{

        					try
        					{
        						if (cStmtObject != null) cStmtObject.close();
        					}//end of try
        					catch (SQLException sqlExp)
        					{
        						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
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
        							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
        							throw new TTKException(sqlExp, "preauth");
        						}//end of catch (SQLException sqlExp)
        					}//end of finally Connection Close
        				}//end of finally Statement Close
        			}//end of try
        			catch (TTKException exp)
        			{
        				throw new TTKException(exp, "preauth");
        			}//end of catch (TTKException exp)
        			finally // Control will reach here in anycase set null to the objects 
        			{
        				prs = null;
        				drs = null;
        				ars = null;
        				drugRs=null;
        				partnerDetails=null;
        				cStmtObject = null;
        				conn = null;
        			}//end of finally
        		}//end of finally
        	}//end of getOnlinePartialPreAuthDetails(Long preAuthSeqId)
// for provider exception cr
            public long deleteExistngDataForPreAuth(String seqIds,Long lPreAuthIntSeqId,String flag,String seqQty,Long LhospSeqId,Long userSeqId,String loginFlag,HttpServletRequest request) throws TTKException {
            	
            	Connection conn = null;
            	CallableStatement cStmtObject=null;
            	Long lSuccess	=	(long) 0;
            	OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
            	HttpSession session	=	request.getSession();
            	try{
            		
            		conn = ResourceManager.getConnection();
            		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteExistingDataForPreAuth);
            		   cStmtObject.setString(1,seqIds);
            		   cStmtObject.setLong(2,lPreAuthIntSeqId); 
            		   cStmtObject.setString(3,flag);
            		   cStmtObject.setString(4,seqQty);
            		   cStmtObject.setLong(5,LhospSeqId);
            		   cStmtObject.registerOutParameter(6,Types.INTEGER);
            		   cStmtObject.execute();			
            		   lSuccess = cStmtObject.getLong(6);		
            	}//end of try
            	catch (SQLException sqlExp)
            	{
            		if("providerLogin".equals(loginFlag))
            		{
            				System.out.println("------------------ Calling : M1 or m2 :  deleteExistngDataForPreAuth ------------------"); System.out.println("loginFlag : "+loginFlag);
            			lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
            			userSeqId = (userSeqId==null) ? 0: userSeqId;
            			
            			System.out.println("SQL Exception raised...");
            			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
            			System.out.println("userSeqId 			= "+userSeqId);
            			
            			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
            			session.setAttribute("lpreAuthIntSeqId", null);
            		}
            		throw new TTKException(sqlExp, "preauth");
            	}//end of catch (SQLException sqlExp)
            	catch (Exception exp)
            	{
            		if("providerLogin".equals(loginFlag))
            		{
            			System.out.println("------------------ Calling : M1 or M2 :  deleteExistngDataForPreAuth ------------------"); System.out.println("loginFlag : "+loginFlag);
            			lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
            			userSeqId = (userSeqId==null) ? 0: userSeqId;
            			
            			System.out.println("JAVA Exception raised...");
            			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
            			System.out.println("userSeqId 			= "+userSeqId);
            			
            			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
            			session.setAttribute("lpreAuthIntSeqId", null);
            		}
            		throw new TTKException(exp, "preauth");
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
            				log.error("Error while closing the Statement in PreAuthDAOImpl deleteDiagnosisDetails()",sqlExp);
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
            					log.error("Error while closing the Connection in PreAuthDAOImpl deleteDiagnosisDetails()",sqlExp);
            					throw new TTKException(sqlExp, "preauth");
            				}//end of catch (SQLException sqlExp)
            			}//end of finally Connection Close
            		}//end of try
            		catch (TTKException exp)
            		{
            			throw new TTKException(exp, "preauth");
            		}//end of catch (TTKException exp)
            		finally // Control will reach here in anycase set null to the objects 
            		{
            			cStmtObject = null;
            			conn = null;
            		}//end of finally
            	}//end of finally
            	return lSuccess;
            }//end of deleteExistngDataForPreAuth(lPreauthSeqId)     

            

//GET FILE FFROM DB FOR UPLOADED DOCS
            
       	 public InputStream getProviderUploadedDocsDBFile(String fileSeqId)throws TTKException{
    			
    			Connection conn = null;
    			PreparedStatement cStmtObject=null;
    			ResultSet rs=null;
    			Blob blob	=	null;
    			InputStream iStream	=	null;
    			try{
    				conn = ResourceManager.getConnection();
    		                 
    		         cStmtObject = conn.prepareCall(strProviderUploadedDocsDBFile);
    		         cStmtObject.setString(1, fileSeqId);
    					rs= cStmtObject.executeQuery();
    					
    					if(rs != null){
    						if(rs.next())
    							blob	=	rs.getBlob(1);
    						iStream	=	blob.getBinaryStream();
    					}//end of if(rs != null)
    			    	//return (ArrayList<Object>)alMouList;
    					
    		    }//end of tryd
    		    catch (SQLException sqlExp) 
    		    {
    		        throw new TTKException(sqlExp, "onlineperauth");
    		    }//end of catch (SQLException sqlExp)
    		    catch (Exception exp) 
    		    {
    		        throw new TTKException(exp, "onlineperauth");
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
    		    			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getProviderUploadedDocsDBFile()",sqlExp);
    		    			throw new TTKException(sqlExp, "onlineperauth");
    		    		}//end of catch (SQLException sqlExp)
    		    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    		    		{
    		    			try
    		    			{
    		    				if(conn != null) conn.close();
    		    			}//end of try
    		    			catch (SQLException sqlExp)
    		    			{
    		    				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl getProviderUploadedDocsDBFile()",sqlExp);
    		    				throw new TTKException(sqlExp, "onlineperauth");
    		    			}//end of catch (SQLException sqlExp)
    		    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		    		{
        		    			try
        		    			{
        		    				if(rs != null) rs.close();
        		    			}//end of try
        		    			catch (SQLException sqlExp)
        		    			{
        		    				log.error("Error while closing the result set in OnlinePreAuthDAOImpl getProviderUploadedDocsDBFile()",sqlExp);
        		    				throw new TTKException(sqlExp, "onlineperauth");
        		    			}//end of catch (SQLException sqlExp)
        		    		}//end of finally Connection Close
    		    		}//end of finally Connection Close
    		    	}//end of try
    		    	catch (TTKException exp)
    		    	{
    		    		throw new TTKException(exp, "onlineperauth");
    		    	}//end of catch (TTKException exp)
    		    	finally // Control will reach here in anycase set null to the objects 
    		    	{
    		    		cStmtObject = null;
    		    		conn = null;
    		    		rs = null;
    		    		blob	=	null;
    		    	}//end of finally
    			}//end of finally
    		    return iStream;
    	 }


//GET ENCOUNTER TYPES BASED ON THE BENEFIT TYPE SELECTED
       	 
       	public ArrayList<CacheObject> getEnounterTypes(String benefitType) throws TTKException {
       		ArrayList<CacheObject> alResultList = new ArrayList<CacheObject>();
    		Connection conn = null;
    		CallableStatement cStmtObject=null;
    		ResultSet rs = null;
    		CacheObject  cacheObject = null;
    		try{
    			conn	=	ResourceManager.getConnection();
    			cStmtObject=conn.prepareCall(strEncounterTypeList);    
    			cStmtObject.setString(1,benefitType);
    			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
    			cStmtObject.execute();
    			 rs=(ResultSet)cStmtObject.getObject(2);	
    			if(rs != null){
    				while(rs.next()){
    					cacheObject = new CacheObject();	
						cacheObject.setCacheId(TTKCommon.checkNull(rs.getString(1)));
						cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString(2)));
    					alResultList.add(cacheObject);
    				}//end of while(rs.next())
    			}//end of if(rs != null)
    			
    			return (ArrayList<CacheObject>)alResultList;
    		}//end of try
    		catch (SQLException sqlExp)
    		{
    			throw new TTKException(sqlExp, "onlinepreauth");
    		}//end of catch (SQLException sqlExp)
    		catch (Exception exp)
    		{
    			throw new TTKException(exp, "onlinepreauth");
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
    					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl getDrugCodeList()",sqlExp);
    					throw new TTKException(sqlExp, "onlinepreauth");
    				}//end of catch (SQLException sqlExp)
    				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
    				{

    					try
    					{
    						if (cStmtObject != null) cStmtObject.close();
    					}//end of try
    					catch (SQLException sqlExp)
    					{
    						log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getDrugCodeList()",sqlExp);
    						throw new TTKException(sqlExp, "onlinepreauth");
    					}//end of catch (SQLException sqlExp)
    					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    					{
    						try
    						{
    							if(conn != null) conn.close();
    						}//end of try
    						catch (SQLException sqlExp)
    						{
    							log.error("Error while closing the Connection in OnlinePreAuthDAOImpl getDrugCodeList()",sqlExp);
    							throw new TTKException(sqlExp, "onlinepreauth");
    						}//end of catch (SQLException sqlExp)
    					}//end of finally Connection Close
    				}//end of finally Statement Close
    			}//end of try
    			catch (TTKException exp)
    			{
    				throw new TTKException(exp, "onlinepreauth");
    			}//end of catch (TTKException exp)
    			finally // Control will reach here in anycase set null to the objects 
    			{
    				rs = null;
    				cStmtObject = null;
    				conn = null;
    			}//end of finally
    		}//end of finally
    	}//end of getEnounterTypes(String benefitType)
        
        public String saveDentalDetails(DentalOrthoVO dentalOrthoVO,String PatOrClm,Long lPreAuthIntSeqId,Long userSeqId,String loginFlag,HttpServletRequest request) throws TTKException{
        	Connection conn = null;
			CallableStatement cStmtObject=null;
			OracleResultSet rs = null;	
			OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
			HttpSession session	=	request.getSession();
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePreAuthDentalDetails);
			
				if(dentalOrthoVO.getDentoSeqid()!=null)
					cStmtObject.setLong(1,dentalOrthoVO.getDentoSeqid());
				else
					cStmtObject.setLong(1,0);
				cStmtObject.setLong(2,dentalOrthoVO.getPreAuthSeqid());
				cStmtObject.setString(3,PatOrClm);
				cStmtObject.setString(4,dentalOrthoVO.getDentoclass1());
				cStmtObject.setString(5,dentalOrthoVO.getDentoclass2());
				cStmtObject.setString(6,dentalOrthoVO.getDentoclass2Text());
				cStmtObject.setString(7,dentalOrthoVO.getDentoclass3());
				cStmtObject.setString(8,dentalOrthoVO.getSkeletalClass1());
				cStmtObject.setString(9,dentalOrthoVO.getSkeletalClass2());
				cStmtObject.setString(10,dentalOrthoVO.getSkeletalClass3());
				cStmtObject.setString(11,dentalOrthoVO.getOverJet());
				cStmtObject.setString(12,dentalOrthoVO.getReverseJet());
				cStmtObject.setString(13,dentalOrthoVO.getReverseJetYN());
				cStmtObject.setString(14,dentalOrthoVO.getCrossbiteAntrio());
				cStmtObject.setString(15,dentalOrthoVO.getCrossbitePosterior());
				cStmtObject.setString(16,dentalOrthoVO.getCrossbiteRetrucontract());
				cStmtObject.setString(17,dentalOrthoVO.getOpenbiteAntrio());
				cStmtObject.setString(18,dentalOrthoVO.getOpenbitePosterior());
				cStmtObject.setString(19,dentalOrthoVO.getOpenbiteLateral());
				cStmtObject.setString(20,dentalOrthoVO.getContactPointDisplacement());
				cStmtObject.setString(21,dentalOrthoVO.getOverBite());
				cStmtObject.setString(22,dentalOrthoVO.getOverbitePalatalYN());
				cStmtObject.setString(23,dentalOrthoVO.getOverbiteGingivalYN());
				cStmtObject.setString(24,dentalOrthoVO.getHypodontiaQuand1Teeth());
				cStmtObject.setString(25,dentalOrthoVO.getHypodontiaQuand2Teeth());
				cStmtObject.setString(26,dentalOrthoVO.getHypodontiaQuand3Teeth());
				cStmtObject.setString(27,dentalOrthoVO.getHypodontiaQuand4Teeth());
				cStmtObject.setString(28,dentalOrthoVO.getImpededTeethEruptionNo());
				cStmtObject.setString(29,dentalOrthoVO.getImpededTeethNo());
				cStmtObject.setString(30,dentalOrthoVO.getSubmergerdTeethNo());
				cStmtObject.setString(31,dentalOrthoVO.getSupernumeryTeethNo());
				cStmtObject.setString(32,dentalOrthoVO.getRetainedTeethNo());
				cStmtObject.setString(33,dentalOrthoVO.getEctopicTeethNo());
				cStmtObject.setString(34,dentalOrthoVO.getCranioFacialNo());
				cStmtObject.setString(35,dentalOrthoVO.getAestheticComp());
				cStmtObject.setString(36,dentalOrthoVO.getCrossbiteAntriomm());
				cStmtObject.setString(37,dentalOrthoVO.getCrossbitePosteriormm());
				cStmtObject.setString(38,dentalOrthoVO.getCrossbiteRetrucontractmm());
				cStmtObject.setString(39,dentalOrthoVO.getContactPointDisplacementmm());
				cStmtObject.setString(40,dentalOrthoVO.getDentoclass3Text());
				
				cStmtObject.registerOutParameter(41,Types.VARCHAR);
				cStmtObject.executeUpdate();
				String iotn = cStmtObject.getString(41);
				return iotn;
			 }//end of try
				catch (SQLException sqlExp) 
				{
					if("providerLogin".equals(loginFlag))
            		{
						System.out.println("------------------ Calling : M5 :  saveDentalDetails ------------------"); System.out.println("loginFlag : "+loginFlag);
						lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
            			userSeqId = (userSeqId==null) ? 0: userSeqId;
            			
            			System.out.println("SQL Exception raised...");
            			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
            			System.out.println("userSeqId 			= "+userSeqId);
            			
            			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
            			session.setAttribute("lpreAuthIntSeqId", null);
            		}
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp) 
				{	
					
					if("providerLogin".equals(loginFlag))
            		{
						System.out.println("------------------ Calling : M5 :  saveDentalDetails ------------------"); System.out.println("loginFlag : "+loginFlag);
						lPreAuthIntSeqId = (lPreAuthIntSeqId==null) ? 0: lPreAuthIntSeqId;
            			userSeqId = (userSeqId==null) ? 0: userSeqId;
            			
            			System.out.println("java  Exception raised...");
            			System.out.println("lPreAuthIntSeqId	= "+lPreAuthIntSeqId);
            			System.out.println("userSeqId 			= "+userSeqId);
            			
            			onlinePreAuthManager.deleteProviderDetail(lPreAuthIntSeqId, userSeqId);
            			session.setAttribute("lpreAuthIntSeqId", null);
            		}
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
							log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl saveDentalDetails()",sqlExp);
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
								log.error("Error while closing the Statement in OnlinePreAuthDAOImpl saveDentalDetails()",sqlExp);
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
									log.error("Error while closing the Connection in OnlinePreAuthDAOImpl saveDentalDetails()",sqlExp);
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
	 	
	 	}//long saveDentalDetails(DentalOrthoVO dentalOrthoVO


        public String saveAppealComments(Long preAuthSeqId,String appealComments) throws TTKException
        {
        	Connection conn = null;
        	CallableStatement cStmtObject=null;
        	ResultSet rs = null;
        	String aplComment="";
            try {
            	
            	conn = ResourceManager.getConnection();
    			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetAppealComments);
        		cStmtObject.setLong(1, preAuthSeqId);
        		cStmtObject.setString(2, appealComments);
        		
        		cStmtObject.registerOutParameter(2, Types.VARCHAR);
    			cStmtObject.execute();
    	
    			aplComment = cStmtObject.getString(2);
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
    					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl saveAppealComments()",sqlExp);
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
    						log.error("Error while closing the Statement in OnlinePreAuthDAOImpl saveAppealComments()",sqlExp);
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
    							log.error("Error while closing the Connection in OnlinePreAuthDAOImpl saveAppealComments()",sqlExp);
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
    		return aplComment;
         }
        // ProviderLogin Exception Cr
        public void deleteProviderDetail(Long preAuthSeqId,Long userSeqId) throws TTKException
        {
        	System.out.println(" # # # # # # calling DB Procudere (ranjan) to  delete ProviderLogin Preauth Detail after raising exception...  # # # # # # ");
        	Connection conn = null;
        	CallableStatement cStmtObject=null;
            try 
            {	
            	conn = ResourceManager.getConnection();
    			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteProviderDetail);
    			
    			 if(preAuthSeqId == null)
                 	cStmtObject.setLong(1, 0);
                 else
                 	cStmtObject.setLong(1, preAuthSeqId);
    			 
    			 if(userSeqId == null)
                  	cStmtObject.setLong(2, 0);
                  else
                  	cStmtObject.setLong(2, userSeqId);
    			 
    			 	cStmtObject.registerOutParameter(3,Types.INTEGER);
    			 	cStmtObject.execute();
            }//end of try
            catch (SQLException sqlExp) 
			{
            	System.out.println(" # # # # # #   after called deleteProviderDetail() method, SQL Exception Raised # # # # # #");	
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				System.out.println(" # # # # # #   after called deleteProviderDetail() method, Java Exception Raised  # # # # # #");	
				throw new TTKException(exp, "policy");
			}//end of catch (Exception exp) 
    		finally
    		{
    			/* Nested Try Catch to ensure resource closure */ 
    			try // First try closing the result set
    			{
    				try
    				{
    					if (cStmtObject != null) 
    						cStmtObject.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl deleteProviderDetail()",sqlExp);
    					throw new TTKException(sqlExp, "policy");
    				}
    				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
    				{


    					try
    					{
    						if (conn != null) 
    							conn.close();
    					}
    					catch (SQLException sqlExp)
    					{
    						log.error("Error while closing the Statement in OnlinePreAuthDAOImpl deleteProviderDetail()",sqlExp);
    						throw new TTKException(sqlExp, "policy");
    					}
    					
    				}
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
       } // end of deleteProviderDetail()
        
        private OnlinePreAuthManager getOnlineAccessManagerObject() throws TTKException
        {
        	OnlinePreAuthManager onlinePreAuthManager= null;
            try
            {
                if(onlinePreAuthManager == null)
                {
                    InitialContext ctx = new InitialContext();
                    //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
                    onlinePreAuthManager = (OnlinePreAuthManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlinePreAuthManagerBean!com.ttk.business.onlineforms.OnlinePreAuthManager");
                }//end if
            }//end of try
            catch(Exception exp)
            {
                throw new TTKException(exp, strOnlinePreAuth);
            }//end of catch
            return onlinePreAuthManager;
        }//end of getOnlineAccessManagerObject()
        

public String getVipDetail(String enrollID) throws TTKException{
    Connection conn 		= null;
    ResultSet rs 			= null;
    PreparedStatement pStmt = null;
    UserListVO userListVO	=	null;
    String vip=null;
    try{
	
		 conn	=	null;
		 rs		=	null;
		 conn 	= ResourceManager.getConnection();
		 pStmt = conn.prepareStatement(vip_query);
         pStmt.setString(1,enrollID);
         rs = pStmt.executeQuery();
         if(rs != null){
             while (rs.next()) {
            	vip=TTKCommon.checkNull(rs.getString("vip_yn"));;
             }
         }
}
    catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "onlineformsemp");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "onlineformsemp");
	}//end of catch (Exception exp)
	finally
	{
    	/* Nested Try Catch to ensure resource closure */
    	try // First try closing the Statement
    	{
    		try
			{
				if (rs != null) rs.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in OnlineProviderDAOImpl getVipYN()",sqlExp);
				throw new TTKException(sqlExp, "onlineforms");
			}//end of catch (SQLException sqlExp)
    		finally{
    			try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getVipYN()",sqlExp);
        			throw new TTKException(sqlExp, "onlineformsemp");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl savePreAuthDetails()",sqlExp);
        				throw new TTKException(sqlExp, "onlineformsemp");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
    		}
    		
    	}//end of try
    	catch (TTKException exp)
    	{
    		throw new TTKException(exp, "onlineformsemp");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects
    	{
    		rs=null;
    		pStmt = null;
    		conn = null;
    	}//end of finally
	}//end of finally

			return vip;
}

}//end of OnlinePreAuthDAOImpl
