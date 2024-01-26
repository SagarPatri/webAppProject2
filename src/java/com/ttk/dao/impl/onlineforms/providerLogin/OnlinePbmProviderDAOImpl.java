package com.ttk.dao.impl.onlineforms.providerLogin; 
/**
 * @ (#) OnlinePbmProviderDAOImpl.java April 02, 2017
 * Project 	     : Dubai ProjectX
 * File          : OnlinePbmProviderDAOImpl.java
 * Author        : Nagababu Kamadi
 * Company       : RCS Technologies
 * Date Created  : April 02, 2017
 *
 * @author       :  Nagababu Kamadi
 * Modified by   :
 * Modified date :
 * Reason        :
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
//import com.ttk.dto.preauth.PreAuthVO;
//import com.ttk.dto.security.GroupVO;
public class OnlinePbmProviderDAOImpl implements BaseDAO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID 				= 1L;
	private static Logger log = Logger.getLogger(OnlinePbmProviderDAOImpl.class);
	
	private static String strPreAuthGeneralDetails="{CALL HOSP_PBM_PKG.SAVE_PBM_PAT(?,?,?,?,?,?,?,?,?,?)}";
	private static String strSelectAllPreAuthDetails="{CALL HOSP_PBM_PKG.SELECT_PAT_AUTH_DETAILS(?,?,?,?)}";
	private static String strAddIcdDetails="{CALL HOSP_PBM_PKG.SAVE_DIAGNOSYS_DETAILS(?,?,?,?,?)}";
	private static String strAddDrugDetails="{CALL HOSP_PBM_PKG.SAVE_ACTIVITY_DETAILS(?,?,?,?,?,?,?,?,?)}";
	private static String strRequestAuthorization="{CALL HOSP_PBM_PKG.GENERATE_AUTHORIZATION(?,?)}";
	private static String strWebserviceCode="{CALL  HOSP_PBM_PKG.PMB_WEBSRVC_PROC(?,?,?,?,?)}";

	
	
	private static String strDeleteDiagnosisDetails="{CALL HOSP_PBM_PKG.DELETE_DIAGNOSYS_DETAILS(?,?,?)}";
	private static String strDeleteDrugDetails="{CALL HOSP_PBM_PKG.DELETE_ACTIVITY_DETAILS(?,?,?)}";
	private static String strValidateMember="{CALL HOSP_PBM_PKG.VALIDATE_ENROLLMENT_ID(?,?,?,?,?)}";
	private static String strPreAuthList="{CALL HOSP_PBM_PKG.SELECT_PREAUTH_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static String strGetTariffDetails="{CALL HOSP_PBM_PKG.SELECT_ACTIVITY_CODE(?,?,?,?,?,?)}";
	private static String strSubmitClaim="{CALL hosp_pbm_pkg.save_pbm_clm(?,?,?,?)}";
	private static String strCheckElegibility="{CALL hosp_pbm_pkg.pbm_clm_status(?,?)}";
	private static String strPreAuthStatus="select PAT_STATUS_TYPE_ID,COMPLETED_YN from APP.PAT_AUTHORIZATION_DETAILS where PAT_AUTH_SEQ_ID= ?";
	private static String strPBMUploadFile="{CALL hosp_pbm_pkg.save_pbm_clm_file(?,?,?,?,?)}";
	private static String strclaimsList="{CALL hosp_pbm_pkg.select_claim_list(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static String strclaimDetails="{CALL hosp_pbm_pkg.SELECT_CLAIM_DETAILS(?,?,?,?,?,?)}";
	
	private static String strComp_Preauth="{CALL HOSP_PBM_PKG.COMP_PREAUTH(?,?)}";

	private static final String strSaveClaimsXML = "{CALL CLAIM_BULK_UPLOAD.SAVE_CLAIM_XML(?,?,?,?)}";
	
	private static final String strClaimupload = "{CALL CLAIM_BULK_UPLOAD.CLAIM_UPLOAD(?,?,?,?,?,?,?,?,?,?,?)}";
	// private static final String strGetAppealComments = "{CALL AUTHORIZATION_PKG.SAVE_APPEAL_REMARK(?,?)}";
	 private static final String strGetAppealComments = "{CALL HOSP_DIAGNOSYS_PKG.SAVE_APPEAL_REMARK(?,?)}";
 
	 private static final String strGetMemberOnEnroll 	= "{CALL HOSP_PBM_PKG.GET_MEM_BENIFIT_DETAILS(?,?,?,?,?)}";
	 
	 private static final String strGetTOBForBenefir	 	= "{CALL HOSP_DIAGNOSYS_PKG.GET_BENIFIT_COPAY_DEDU(?,?,?)}";
		private static final String updateLogProc="{CALL ELIGIBILITY_LOG_PROC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	 
	 
	 
	
	public Long addPreAuthGeneralDetails(PbmPreAuthVO preAuthVO) throws TTKException{
        Connection conn = null;
        CallableStatement cllStm=null;
        
        try{
            conn = ResourceManager.getConnection();
            cllStm	=	conn.prepareCall(strPreAuthGeneralDetails);
            
            if(preAuthVO.getPreAuthSeqID()==null){
            	//cllStm.setString(1,null);     
            	cllStm.registerOutParameter(1,Types.BIGINT);
            }else{
            	cllStm.setLong(1,preAuthVO.getPreAuthSeqID());
            }
            cllStm.setString(2,preAuthVO.getDateOfTreatment());
            cllStm.setString(3,preAuthVO.getEnrolmentID());
           /*   */
            cllStm.setString(4,preAuthVO.getQatarID());            
            cllStm.setString(5,preAuthVO.getPreAuthNO());
            cllStm.setLong(6,preAuthVO.getHospitalSeqID());           
            cllStm.setString(7,preAuthVO.getClinicianID());
            cllStm.setString(8,preAuthVO.getClinicianName());
            cllStm.setString(9,preAuthVO.getEventRefNo());
            cllStm.setLong(10,preAuthVO.getAddedBy());
            
            cllStm.execute();
            
             Long preAuthSeqID;
             if(preAuthVO.getPreAuthSeqID()==null)preAuthSeqID=cllStm.getLong(1);
             else preAuthSeqID=preAuthVO.getPreAuthSeqID();
             
            return preAuthSeqID;
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
        	closeResource("addPreAuthGeneralDetails()",null,cllStm,conn);
		}
			
    }//end of addPreAuthGeneralDetails(String enrollmentId)
	
	public PbmPreAuthVO  validateMemberID(PbmPreAuthVO preAuthVO) throws TTKException{
        Connection conn = null;
        CallableStatement cllStm=null;
        ResultSet         rs=null;
        
        
        try{
        	
            conn = ResourceManager.getConnection();
            cllStm	=	conn.prepareCall(strValidateMember);
            cllStm.setString(1,TTKCommon.checkNull(preAuthVO.getEnrolmentID()).trim());
            cllStm.setString(2,preAuthVO.getDateOfTreatment());
            cllStm.setString(3,preAuthVO.getEventRefNo());
            cllStm.setLong(4, preAuthVO.getHospitalSeqID());
            cllStm.registerOutParameter(5,OracleTypes.CURSOR);
            
            
           
             cllStm.execute();
            
             
             rs=(ResultSet)cllStm.getObject(5);
             
             if(rs!=null){
            	 
            	 if(rs.next()){
            		 preAuthVO.setMemSeqID(rs.getLong("MEMBER_SEQ_ID"));
            		 preAuthVO.setDobDate(rs.getString("MEM_DOB"));
            		 preAuthVO.setEnrolmentID(rs.getString("TPA_ENROLLMENT_ID"));
            		 preAuthVO.setQatarID(rs.getString("EMIRATE_ID"));
            		 preAuthVO.setInsCompanyID(rs.getString("ins_cmp_code"));
            		 preAuthVO.setMemGender(rs.getString("gender_general_type_id"));
            	 }
             }
             
             
            return preAuthVO;
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
        	closeResource("validateMemberID()",rs,cllStm,conn);
		}
			
    }//end of validateMemberID(String enrollmentId)
	
	
	
	
	public String  checkClaimElegibility(Long preAuthSeqID) throws TTKException{
        Connection conn = null;
        CallableStatement cllStm=null;
       
        
        
        try{
        	
            conn = ResourceManager.getConnection();
            
            cllStm	=	conn.prepareCall(strCheckElegibility);
            cllStm.setLong(1,preAuthSeqID);
            
            cllStm.registerOutParameter(2,OracleTypes.CHAR);
                                  
             cllStm.execute();
             String rowprocc=cllStm.getString(2);
                                      
            return rowprocc;
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
        	closeResource("chechClaimElegibility()",null,cllStm,conn);
		}
			
    }//end of validateMemberID(String enrollmentId)
	
	
	
	
	
	

	
	public PbmPreAuthVO getAllPreAuthDetails(Long preAuthSeqID) throws TTKException{
        Connection conn = null;
        CallableStatement cllStm=null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        PbmPreAuthVO pbmPreAuthVO=new PbmPreAuthVO();
        DiagnosisDetailsVO diagnosisDetailsVO=null;
        ActivityDetailsVO activityDetailsVO=null;
        ArrayList<DiagnosisDetailsVO> allIcds=new ArrayList<>();
        ArrayList<ActivityDetailsVO> allAct=new ArrayList<>();
        ResultSet rsGeneral=null;
        BigDecimal totalRequestedAmt=new BigDecimal(0);
        BigDecimal totalApprovedAmt=new BigDecimal(0);
        BigDecimal totalRequestedAmtforApp=new BigDecimal(0);
        /*String dateFormat="DD/MM/YYYY";*/
        try{
            conn = ResourceManager.getConnection();
            cllStm	=	conn.prepareCall(strSelectAllPreAuthDetails);
            
            	cllStm.setLong(1,preAuthSeqID);
            	
            	cllStm.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            	cllStm.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
            	cllStm.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
             cllStm.execute();
             rsGeneral=(ResultSet)cllStm.getObject(2);
             ResultSet rsIcd=(ResultSet)cllStm.getObject(3);
             ResultSet rsDrug=(ResultSet)cllStm.getObject(4);
             
             if(rsGeneral!=null){
            	 if(rsGeneral.next()){
            		 pbmPreAuthVO.setPreAuthSeqID(preAuthSeqID);
            		 pbmPreAuthVO.setPreAuthNO(rsGeneral.getString("PRE_AUTH_NUMBER"));
            		 pbmPreAuthVO.setEnrolmentID(rsGeneral.getString("TPA_ENROLLMENT_ID"));
            		 pbmPreAuthVO.setQatarID(rsGeneral.getString("QUATAR_ID"));
            		 pbmPreAuthVO.setDobDate(rsGeneral.getString("MEM_DOB"));
            		 pbmPreAuthVO.setInsCompanyID(rsGeneral.getString("PAYER_ID"));
            		 pbmPreAuthVO.setClinicianID(rsGeneral.getString("CLINICIAN_ID"));
            		 pbmPreAuthVO.setClinicianName(rsGeneral.getString("CLINICIAN_NAME"));
            		 pbmPreAuthVO.setDateOfTreatment(rsGeneral.getString("HOSPITALIZATION_DATE"));
            		 pbmPreAuthVO.setTransactionDate(rsGeneral.getString("TRANSACTION_DATE"));
            		 pbmPreAuthVO.setInsuranceCompanyName(rsGeneral.getString("INS_COMP_NAME"));
            		 pbmPreAuthVO.setAuthorizationNO(rsGeneral.getString("AUTH_NUMBER"));
            		 pbmPreAuthVO.setErxRef(rsGeneral.getString("ERX_REF"));
            		 pbmPreAuthVO.setCommonts(rsGeneral.getString("COMMENTS"));
            		 pbmPreAuthVO.setPreAuthStatus(rsGeneral.getString("PAT_STATUS_TYPE_ID"));
            		 pbmPreAuthVO.setEventRefNo(rsGeneral.getString("event_no"));
            		 pbmPreAuthVO.setPolicyNo(rsGeneral.getString("policy_number"));
            		 pbmPreAuthVO.setPolicyNo(rsGeneral.getString("policy_number"));
            		 pbmPreAuthVO.setTotalGrossAmt(TTKCommon.checkNull(rsGeneral.getString("TOT_ALLOWED_AMOUNT")));            		 
            		 pbmPreAuthVO.setMemberName(TTKCommon.checkNull(rsGeneral.getString("mem_name")));
            	//	 pbmPreAuthVO.setPreAuthstatusDesc(TTKCommon.checkNull(rsGeneral.getString("pat_status_type_desc")));
            		 pbmPreAuthVO.setDecisionDt(TTKCommon.checkNull(rsGeneral.getString("DECISION_DATE"))); 
            		 pbmPreAuthVO.setAppealComments(rsGeneral.getString("Appeal_Remark"));
            		 pbmPreAuthVO.setAppealDocsYN(TTKCommon.checkNull(rsGeneral.getString("docs_available_yn")));
            		 
            		if("INP-APL".equals(rsGeneral.getString("IN_PROGESS_STATUS")))
                  	{	
            			 pbmPreAuthVO.setPreAuthstatusDesc("In-Progress");
                  	}
                  	else
                  	{
                  		pbmPreAuthVO.setPreAuthstatusDesc(TTKCommon.checkNull(rsGeneral.getString("pat_status_type_desc")));
                  	}
            		
            		pbmPreAuthVO.setPreApprovalAmt(TTKCommon.checkNull(rsGeneral.getString("TOT_DISC_GROSS_AMOUNT")));
            		 
            	 }
             }
            	 if(rsIcd!=null){
                	 while(rsIcd.next()){
                		 diagnosisDetailsVO=new DiagnosisDetailsVO();
                		 diagnosisDetailsVO.setDiagSeqId(rsIcd.getLong("DIAG_SEQ_ID"));
                		 diagnosisDetailsVO.setAilmentDescription(rsIcd.getString("ICD_DESCRIPTION"));
                		 diagnosisDetailsVO.setIcdCode(rsIcd.getString("DIAGNOSYS_CODE"));
                		 diagnosisDetailsVO.setPrimaryAilment(rsIcd.getString("PRIMARY_AILMENT_YN"));
                		 diagnosisDetailsVO.setIcdCodeSeqId(rsIcd.getLong("ICD_CODE_SEQ_ID"));
                		 allIcds.add(diagnosisDetailsVO);
                	 }
                	 
             }
            	 if(rsDrug!=null){
            		 int sNO=1;
                	 while(rsDrug.next()){
                		 activityDetailsVO=new ActivityDetailsVO();
                		 activityDetailsVO.setSerialNo(sNO);
                		
                		 activityDetailsVO.setActivityCodeDesc(rsDrug.getString("ACTIVITY_DESCRIPTION"));
                		 
                		 activityDetailsVO.setUnitType(rsDrug.getString("UNIT_TYPE")); 
                		 activityDetailsVO.setGrossAmount(rsDrug.getBigDecimal("GROSS_AMOUNT"));
                		 activityDetailsVO.setDiscount(rsDrug.getBigDecimal("DISCOUNT_AMOUNT"));
                		 activityDetailsVO.setNetAmount(rsDrug.getBigDecimal("NET_AMOUNT"));
                		 activityDetailsVO.setQuantity(rsDrug.getFloat("QUANTITY"));
                		 activityDetailsVO.setQuantityInt(rsDrug.getInt("QUANTITY"));
                		 activityDetailsVO.setApprovedQuantity(rsDrug.getFloat("APPROVED_QUANTITY"));
                		 activityDetailsVO.setApprovedAmount(rsDrug.getBigDecimal("APPROVED_AMT"));
                		 if(rsDrug.getBigDecimal("APPROVED_AMT")!=null){
                			 totalApprovedAmt=totalApprovedAmt.add(rsDrug.getBigDecimal("APPROVED_AMT"));
                    		 }
                		 activityDetailsVO.setActivityDtlSeqId(rsDrug.getLong("ACTIVITY_DTL_SEQ_ID"));
                		 activityDetailsVO.setActivityStatus(rsDrug.getString("ACTIVITY_STATUS"));
                		                 		                		 
                		 activityDetailsVO.setProviderRequestedAmt(rsDrug.getBigDecimal("REQ_AMOUNT"));
                		
                		 if(rsDrug.getBigDecimal("REQ_AMOUNT")!=null){
                		 totalRequestedAmt=totalRequestedAmt.add(rsDrug.getBigDecimal("REQ_AMOUNT"));
                		 }
                		 if(rsDrug.getBigDecimal("REQ_AMOUNT")!=null&& "Approved".equals(rsDrug.getString("ACTIVITY_STATUS")))
                		 {  
                			 totalRequestedAmtforApp=totalRequestedAmtforApp.add(rsDrug.getBigDecimal("REQ_AMOUNT"));
                			 
                		 }
                		 
                		
                		 
                			 
                			 
                		 activityDetailsVO.setPatientShare(rsDrug.getBigDecimal("PATIENT_SHARE_AMOUNT"));
                		 activityDetailsVO.setDuration(rsDrug.getInt("DURATION_DAYS"));
                		 activityDetailsVO.setDenialDescription(rsDrug.getString("DENIAL_CODE"));
                		 activityDetailsVO.setDenialRemarks(rsDrug.getString("REMARKS"));
                		 activityDetailsVO.setDateOfApproval(rsDrug.getString("DATE_APPROVAL"));
                		/* activityDetailsVO.setMiRef(rsDrug.getString("MI_REF"));*/
                		 activityDetailsVO.setErxRef(rsDrug.getString("ERX_REF"));
                		 activityDetailsVO.setErxInstruction(rsDrug.getString("ERX_INSTRUCTIONS"));
                		 
                		 allAct.add(activityDetailsVO);
                		 sNO++;
                	 }
             }
            	 pbmPreAuthVO.setIcdDetails(allIcds);
            	 pbmPreAuthVO.setDrugDetails(allAct);
            	 pbmPreAuthVO.setTotalReqAmt(totalRequestedAmt);
            	 pbmPreAuthVO.setTotalApprAmt(totalApprovedAmt);
            	 pbmPreAuthVO.setTotalReqAmtforApp(totalRequestedAmtforApp);
            	
            	 
            	 if(rsDrug!=null)rsDrug.close();
            	 if(rsIcd!=null)rsIcd.close();
            	
            	 pStmt = conn.prepareStatement(strPreAuthStatus);
                 pStmt.setLong(1,preAuthSeqID);
                 
                 rs = pStmt.executeQuery();
                 if(rs != null){
                     while(rs.next()){
                     	
                     	pbmPreAuthVO.setFinalStatus(rs.getString("PAT_STATUS_TYPE_ID"));
                      	pbmPreAuthVO.setCompletedYN(rs.getString("COMPLETED_YN"));
                     }
                 }
                 
                 
            return pbmPreAuthVO;
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
        	closeResource("getAllPreAuthDetails()",rsGeneral,cllStm,null);
        	closeResource("getAllPreAuthDetails()",rs,pStmt,conn);
		}
			
    }//end of getAllPreAuthDetails(String enrollmentId)
	
	public Integer addIcdDetails(PbmPreAuthVO preAuthVO) throws TTKException{
        Connection conn = null;
        CallableStatement cllStm=null;
        
        try{
            conn = ResourceManager.getConnection();
            cllStm	=	conn.prepareCall(strAddIcdDetails);
            
            if(preAuthVO.getIcdDtlSeqID()==null){
            	cllStm.setString(1,null);
            }else{
            	cllStm.setLong(1,preAuthVO.getIcdDtlSeqID());
            }
            cllStm.setLong(2,preAuthVO.getPreAuthSeqID());
            cllStm.setLong(3,preAuthVO.getIcdCodeSeqID());
            cllStm.setString(4,preAuthVO.getIcdCode());
            cllStm.setLong(5,preAuthVO.getAddedBy());
            cllStm.registerOutParameter(6,OracleTypes.INTEGER);         
            cllStm.execute();
             Integer rowprocc=cllStm.getInt(6);
            return rowprocc;
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
        	closeResource("addIcdDetails()",null,cllStm,conn);
		}
			
    }//ublic Long addIcdDetails(PbmPreAuthVO preAuthVO) throws TTKException{
	
	
	public Integer addDrugDetails(PbmPreAuthVO preAuthVO) throws TTKException{
	 CallableStatement cllStm=null;     
	 Connection conn = null;
        
        
        try{
            conn = ResourceManager.getConnection();
            cllStm	=	conn.prepareCall(strAddDrugDetails);
            
            if(preAuthVO.getDrugDtlSeqID()==null){
            	cllStm.setString(1,null);
            }else{
            	cllStm.setLong(1,preAuthVO.getDrugDtlSeqID());
            }
            cllStm.setLong(2,preAuthVO.getPreAuthSeqID());
            cllStm.setLong(3,preAuthVO.getDrugCodeSeqID());
            cllStm.setString(4,preAuthVO.getUnitType());
            cllStm.setString(5,preAuthVO.getQuantity());
            cllStm.setString(6,preAuthVO.getDays());
            cllStm.setString(7,"Y");
            cllStm.setLong(8,preAuthVO.getAddedBy());
            cllStm.setString(9,"Y");
            cllStm.registerOutParameter(10,OracleTypes.INTEGER);         
            cllStm.execute();
             Integer rowprocc=cllStm.getInt(10);
            return rowprocc;
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
        	closeResource("addDrugDetails()",null,cllStm,conn);
		}
			
    }//ublic Integer addDrugDetails(PbmPreAuthVO preAuthVO) throws TTKException{
	

	public Long requstAuthorization(PbmPreAuthVO preAuthVO) throws TTKException{
        Connection conn = null;
    //    Connection conn2 = null;
        CallableStatement generalCllStm=null;        
        CallableStatement icdCllStm=null;
        CallableStatement drugCllStm=null;
        CallableStatement reqAuthCllStm=null;
        CallableStatement webserviceData=null;
        
        try{
            conn = ResourceManager.getConnection();
            conn.setAutoCommit(false);
          //  conn2 = ResourceManager.getConnection();
          //  conn2.setAutoCommit(false);
            
            //PreAuth general details saving            
            generalCllStm	=	conn.prepareCall(strPreAuthGeneralDetails);
            generalCllStm.registerOutParameter(1,Types.BIGINT);          
            generalCllStm.setString(2,preAuthVO.getDateOfTreatment());
            generalCllStm.setString(3,preAuthVO.getEnrolmentID());
            generalCllStm.setString(4,preAuthVO.getQatarID());            
            generalCllStm.setString(5,preAuthVO.getPreAuthNO());
            
            
            generalCllStm.setLong(6,preAuthVO.getHospitalSeqID());           
            generalCllStm.setString(7,preAuthVO.getClinicianID());
            generalCllStm.setString(8,preAuthVO.getClinicianName());
            generalCllStm.setString(9,preAuthVO.getEventRefNo());
            generalCllStm.setLong(10,preAuthVO.getAddedBy());            
            generalCllStm.execute();
            
             Long preAuthSeqID=generalCllStm.getLong(1);
            
             preAuthVO.setPreAuthSeqID(preAuthSeqID);
             
             //Adding icd details with respect to preauthseqid
             
             ArrayList<DiagnosisDetailsVO> icdDetails=preAuthVO.getIcdDetails();             
             
             icdCllStm	=	conn.prepareCall(strAddIcdDetails);
             for(DiagnosisDetailsVO diagnosisDetailsVO:icdDetails){
            
             icdCllStm.setString(1,null);            
             icdCllStm.setLong(2,preAuthSeqID);
             icdCllStm.setLong(3,diagnosisDetailsVO.getIcdCodeSeqId());
             icdCllStm.setString(4,diagnosisDetailsVO.getIcdCode());
             icdCllStm.setLong(5,preAuthVO.getAddedBy());                      
             icdCllStm.addBatch();
             }
             icdCllStm.executeBatch();
             
             //Adding drug details with respect to preauthseqid
             
             ArrayList<ActivityDetailsVO> drugdetails=preAuthVO.getDrugDetails();
             
             drugCllStm	=	conn.prepareCall(strAddDrugDetails);
             
            for(ActivityDetailsVO activityDetailsVO:drugdetails){
         
             drugCllStm.setString(1,null);             
             drugCllStm.setLong(2,preAuthSeqID);
             drugCllStm.setLong(3,activityDetailsVO.getActivityCodeSeqId());
             drugCllStm.setString(4,activityDetailsVO.getUnitType());
             drugCllStm.setFloat(5,activityDetailsVO.getQuantity());
             drugCllStm.setInt(6,activityDetailsVO.getMedicationDays());
             drugCllStm.setString(7,"Y");
             drugCllStm.setLong(8,preAuthVO.getAddedBy());
             drugCllStm.setString(9,"Y");            
             drugCllStm.addBatch();             
            }
            drugCllStm.executeBatch();
            
            reqAuthCllStm	=	conn.prepareCall(strRequestAuthorization);
           
            reqAuthCllStm.setLong(1,preAuthVO.getPreAuthSeqID());
            reqAuthCllStm.setLong(2,preAuthVO.getAddedBy());
            reqAuthCllStm.execute();
            
            conn.commit();
            
         /* webserviceData = conn2.prepareCall(strWebserviceCode);
            //  XMLType  xmlInputType = XMLType.createXML(conn,preAuthVO.getInputPBMXML());
              XMLType  xmlInputType =   XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), preAuthVO.getInputPBMXML());
              XMLType  xmlResponseType =   XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), preAuthVO.getResponsePBMXML());

           //   XMLType  xmlResponseType = XMLType.createXML(conn,preAuthVO.getInputPBMXML());

            
           webserviceData.setObject(1, xmlInputType);
            webserviceData.setObject(2, xmlResponseType);
            webserviceData.setString(3, "PAT");
            webserviceData.setLong(4,preAuthSeqID);
            webserviceData.setLong(5,preAuthVO.getAddedBy());            
            webserviceData.execute();
            conn2.commit();*/
            
            
            return preAuthSeqID;
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
        	closeResource("requstAuthorization()",null,reqAuthCllStm,null);
        	closeResource("requstAuthorization()",null,drugCllStm,null);
        	closeResource("requstAuthorization()",null,icdCllStm,null);
        	closeResource("requstAuthorization()",null,generalCllStm,conn);  
        	closeResource("requstAuthorization()",null,webserviceData,null);        	
        	
        	
		}
			
    }//ublic Long requstAuthorization(PbmPreAuthVO preAuthVO) throws TTKException{
	
	public Integer deleteDiagnosisDetails(String strIcdDtlSeqID,String preAuthSeqID) throws TTKException{
        Connection conn = null;
        CallableStatement cllStm=null;
        try{
            conn = ResourceManager.getConnection();
            cllStm	=	conn.prepareCall(strDeleteDiagnosisDetails);
            
            	cllStm.setString(1,preAuthSeqID);
               cllStm.setString(2,strIcdDtlSeqID);
            cllStm.registerOutParameter(3,OracleTypes.INTEGER);         
            cllStm.execute();
             Integer rowprocc=cllStm.getInt(3);
            return rowprocc;
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
        	closeResource("deleteDiagnosisDetails()",null,cllStm,conn);
		}
			
    }//ublic Integer deleteDiagnosisDetails(PbmPreAuthVO preAuthVO) throws TTKException{
	
	public Integer deleteDrugDetails(String strDrugDtlSeqID,String preAuthSeqID) throws TTKException{
        Connection conn = null;
        CallableStatement cllStm=null;
        try{
            conn = ResourceManager.getConnection();
            cllStm	=	conn.prepareCall(strDeleteDrugDetails);
            
            	cllStm.setString(1,preAuthSeqID);
            cllStm.setString(2,strDrugDtlSeqID);
            cllStm.registerOutParameter(3,OracleTypes.INTEGER);         
            cllStm.execute();
             Integer rowprocc=cllStm.getInt(3);
            return rowprocc;
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
        	closeResource("deleteDrugDetails()",null,cllStm,conn);
		}
			
    }//ublic Integer deleteDrugDetails(PbmPreAuthVO preAuthVO) throws TTKException{
	
	
	
	public ArrayList<PbmPreAuthVO> getPbmPreAuthList(ArrayList<Object> alData,Long userSeqID, Long hospSeqID) throws TTKException{
/*  
  */
        Connection conn = null;
        CallableStatement cllStm=null;
        PbmPreAuthVO pbmPreAuthVO=null;
        ArrayList<PbmPreAuthVO> allPreAuths=new ArrayList<>();
        String dateFormat="dd/MM/yyyy";
        String strSuppressLink[]={"1"};
        ResultSet rs=null;
       
        try{
            conn = ResourceManager.getConnection();
            cllStm	=	conn.prepareCall(strPreAuthList);// alData=[hjbhjhb, 02/05/2017, 03/05/2017, hjhh, hjhj, hbjbhj, bhjbhj, INP, AUTH_NUMBER, ASC, 1, 101]
          /*  */
            cllStm.setString(1,(String)alData.get(0));
            cllStm.setString(2,(String)alData.get(1));
            cllStm.setString(3,(String)alData.get(2));
            cllStm.setString(4,(String)alData.get(3));
            cllStm.setString(5,(String)alData.get(4));
            cllStm.setString(6,(String)alData.get(5));
            cllStm.setString(7,(String)alData.get(6));
            cllStm.setString(8,(String)alData.get(7));
            cllStm.setString(9,(String)alData.get(8));
            cllStm.setString(10,(String)alData.get(9));
            String status = (String)alData.get(7);
            if("INP".equals(status))							// status
            	cllStm.setString(17,(String)alData.get(10));	// in-progress-Status
            else
            	cllStm.setString(17,null);						// in-progress-Status
            
            cllStm.setString(11,(String)alData.get(11));
            cllStm.setString(12,(String)alData.get(12));
            cllStm.setString(13,(String)alData.get(13));
            cllStm.setString(14,(String)alData.get(14)); 
            cllStm.setLong(15,userSeqID);
            cllStm.setLong(16,hospSeqID);

            cllStm.registerOutParameter(18, oracle.jdbc.OracleTypes.CURSOR);
            	
             cllStm.execute();
             rs=(ResultSet)cllStm.getObject(18);
             
             if(rs!=null){
            	 while(rs.next()){
            		 /*  */
            		 /*  */
            		 pbmPreAuthVO=new PbmPreAuthVO();
            		 pbmPreAuthVO.setPreAuthSeqID(rs.getLong("PRE_AUTH_SEQ_ID"));
            		 pbmPreAuthVO.setPreAuthNO(rs.getString("PRE_AUTH_NUMBER"));
            		 pbmPreAuthVO.setEnrolmentID(rs.getString("TPA_ENROLLMENT_ID"));
            		 if(rs.getString("QUATAR_ID")==null){
            			 pbmPreAuthVO.setQatarID(""); 
            		 }else{
            		 pbmPreAuthVO.setQatarID(rs.getString("QUATAR_ID"));     
            		 }
            		 pbmPreAuthVO.setClinicianName(rs.getString("CLINICIAN_NAME"));
            		 if(rs.getString("HOSPITALIZATION_DATE")!=null){
            		 pbmPreAuthVO.setDateOfTreatment(rs.getString("HOSPITALIZATION_DATE"));
            		 }
            		 if(rs.getString("DECISION_DATE")!=null){
                		 pbmPreAuthVO.setDecisionDt(rs.getString("DECISION_DATE"));
                		 }else{
                			 pbmPreAuthVO.setDecisionDt("");
                		 }
            		 //pbmPreAuthVO.setDateOfTreatment(TTKCommon.convertDateAsString(dateFormat, rs.getDate("HOSPITALIZATION_DATE")));
            		 pbmPreAuthVO.setAuthorizationNO(rs.getString("AUTH_NUMBER"));
            		 pbmPreAuthVO.setMemberName(rs.getString("MEM_NAME"));
            		 
            		 pbmPreAuthVO.setEventRefNo(rs.getString("EVENT_NO"));
            		
            		 pbmPreAuthVO.setStatus(rs.getString("STATUS"));
            		 
            		 if("In Progress".equals(rs.getString("STATUS"))){
	            			
	            			pbmPreAuthVO.setSuppressLink(strSuppressLink);
	            		 }
            		 if("Shortfall".equals(rs.getString("STATUS"))){
            			 
         				pbmPreAuthVO.setSuppressLink(strSuppressLink);
         			 
         		       }
            		 if("Cancelled".equals(rs.getString("STATUS"))){
            			 
          				pbmPreAuthVO.setSuppressLink(strSuppressLink);
          			 
          		       }
            		 
                 	if("INP-ENH".equals(rs.getString("IN_PROGESS_STATUS")))
                 	{	
                 		pbmPreAuthVO.setStatus("In-Progress - Enhancement");
                 		pbmPreAuthVO.setInProImageName("InprogressEnhancement");
                 		pbmPreAuthVO.setInProImageTitle("InProgress Enhancement");
                 	}
                 	else if("INP-APL".equals(rs.getString("IN_PROGESS_STATUS")))
                 	{
                 		pbmPreAuthVO.setStatus("In-Progress-Appeal");
                 		pbmPreAuthVO.setInProImageName("InprogressAppeal");
                 		pbmPreAuthVO.setInProImageTitle("InProgress Appeal");
                 	}
                 	else if("INP-RES".equals(rs.getString("IN_PROGESS_STATUS")))
                 	{
                 		pbmPreAuthVO.setStatus("In-Progress-Shortfall Responded");
                 		pbmPreAuthVO.setInProImageName("AddIcon");
                 		pbmPreAuthVO.setInProImageTitle("InProgress Shortfall Responded");
                 	}
                 	else
                 	{
                 		pbmPreAuthVO.setStatus(rs.getString("STATUS"));
                 		pbmPreAuthVO.setInProImageName("Blank");
                 		pbmPreAuthVO.setInProImageTitle("");
                 	}
                 	
                 	String PAT_STATUS_TYPE_ID = TTKCommon.checkNull(rs.getString("PAT_STATUS_TYPE_ID"));
                	if("APR".equals(PAT_STATUS_TYPE_ID) || "REJ".equals(PAT_STATUS_TYPE_ID))
                 	{		
                 		if(rs.getInt("claim_seq_id") == 0)
                 		{	
                 			if(rs.getInt("appeal_count")<2)
                 			{
                 				pbmPreAuthVO.setAppealImageName("AppealButton");
                 				pbmPreAuthVO.setApealImageTitle("PreApproval Appeal");
                			}
                 			else
                 			{
                 				pbmPreAuthVO.setAppealImageName("Blank");
                 				pbmPreAuthVO.setApealImageTitle("");
                 			} 
                 		}
                 		else
             			{
             				pbmPreAuthVO.setAppealImageName("Blank");
             				pbmPreAuthVO.setApealImageTitle("");
             			}
                 	}
                 	else
           			{
            				pbmPreAuthVO.setAppealImageName("Blank");
             				pbmPreAuthVO.setApealImageTitle("");
           			}	
                 	 allPreAuths.add(pbmPreAuthVO);
            	 }
            	 
             }
            return allPreAuths;
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
        	closeResource("getPbmPreAuthList()",rs,cllStm,conn);
		}
			
    
	}//public ArrayList<PbmPreAuthVO> getPbmPreAuthList(ArrayList<Object> alData) throws TTKException{
	
	//getPbmClaimList
	
	
	public ArrayList getPbmClaimList(ArrayList alSearchCriteria,Long hospSeqID) throws TTKException{
		
		        Connection conn = null;
		        CallableStatement cllStm=null;
		        PbmPreAuthVO pbmPreAuthVO=null;
		        String strSuppressLink[]={"14"};
		        ResultSet rs=null;
		        
		        try{
		            conn = ResourceManager.getConnection();
		            cllStm	=	conn.prepareCall(strclaimsList);// alSearchCriteria=[hjbhjhb, 02/05/2017, 03/05/2017, hjhh, hjhj, hbjbhj, bhjbhj, INP, AUTH_NUMBER, ASC, 1, 101]
		         
		          cllStm.setString(1,(String) alSearchCriteria.get(0));//1 trtmtFromDate
		          cllStm.setString(2,(String) alSearchCriteria.get(1));//2 trtmtToDate
		          cllStm.setString(3,(String) alSearchCriteria.get(2));//3 clmFromDate
		          cllStm.setString(4,(String) alSearchCriteria.get(3));//4 clmToDate
		          cllStm.setString(5,(String) alSearchCriteria.get(4));//5  patientName
		          cllStm.setString(6,(String) alSearchCriteria.get(5));//6 authNo
		          cllStm.setString(7,(String) alSearchCriteria.get(6));//7 invoiceNumber
		          cllStm.setString(8,(String) alSearchCriteria.get(7));//8 alKootId
		          cllStm.setString(9,(String) alSearchCriteria.get(8));//9 claimNumber
		          cllStm.setString(10,(String) alSearchCriteria.get(9));//10 clmPayStatus
		          cllStm.setString(11,(String) alSearchCriteria.get(10));//11 status
		          cllStm.setString(12,(String) alSearchCriteria.get(11));//12  eventRefNo
		          cllStm.setString(13,(String) alSearchCriteria.get(12));//13
		          
		          cllStm.setString(14,(String) alSearchCriteria.get(15)); // srt Var
		          cllStm.setString(15,(String) alSearchCriteria.get(16)); // srt order
		          cllStm.setString(16,(String) alSearchCriteria.get(17)); //start
		          cllStm.setString(17,(String) alSearchCriteria.get(18)); //  end
		          
		          cllStm.setLong(18,(Long) hospSeqID);//18 hospSeqID
		          
		          cllStm.setString(19,(String) alSearchCriteria.get(13));// QatarId
		          String status = (String) alSearchCriteria.get(10);//10
		          if("INP".equals(status))
		        	  	cllStm.setString(20,(String) alSearchCriteria.get(14));// inProgressStatus
		           else
		        	    cllStm.setString(20,null);// inProgressStatus
		          
		          cllStm.registerOutParameter(21,OracleTypes.CURSOR);//20
		          cllStm.execute();
		           
		            //Clasims sEARCH lIST
		            rs = (java.sql.ResultSet)cllStm.getObject(21);
		            ArrayList<PbmPreAuthVO> alPreAuthSearchVOs	=	new ArrayList<PbmPreAuthVO>();
		            if(rs != null){
		                while (rs.next()) {
		                	pbmPreAuthVO	=	new PbmPreAuthVO();
		                			                			                	
		                	pbmPreAuthVO.setClaimNo(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
		                	pbmPreAuthVO.setClmSeqId((Long)TTKCommon.checkNull(rs.getLong("claim_seq_id")));
		                	pbmPreAuthVO.setEnrolmentID(TTKCommon.checkNull(rs.getString("tpa_enrollment_id")));
		                	pbmPreAuthVO.setMemberName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
		                			                	
		                	pbmPreAuthVO.setInvoiceNo(TTKCommon.checkNull(rs.getString("INVOICE_NUMBER")));
		                	pbmPreAuthVO.setStatus(TTKCommon.checkNull(rs.getString("status")));
		                	pbmPreAuthVO.setDateOfTreatment(TTKCommon.checkNull(rs.getString("PRESCRIPTION_DATE")));
		                	
		                	
		                	pbmPreAuthVO.setAuthorizationNO(TTKCommon.checkNull(rs.getString("Auth_number")));
		                	
		                	pbmPreAuthVO.setQatarID(TTKCommon.checkNull(rs.getString("qatar_id")));
		                	
		                	
		                	
		                	//pbmPreAuthVO.setDispenseStatus(TTKCommon.checkNull(rs.getString("Dispensed_status")));
		                	
		                	
		                	pbmPreAuthVO.setTotalApprAmt(rs.getBigDecimal("APPROVED_AMOUNT"));
		                			                	
		                	pbmPreAuthVO.setClaimSubmittedDate(TTKCommon.checkNull(rs.getString("DISPENSED_DATE")));
		                	pbmPreAuthVO.setClmPayStatus(TTKCommon.checkNull(rs.getString("PAYMENT_STATUS")));
		                	
		                 	pbmPreAuthVO.setSettlementNO(rs.getString("SETTLEMENT_NUMBER"));
		                 	
		                 	if(rs.getString("PRE_AUTH_SEQ_ID")!= null){
		                 	pbmPreAuthVO.setPreAuthSeqID(Long.parseLong(rs.getString("PRE_AUTH_SEQ_ID")));
		                 	}
		                 	pbmPreAuthVO.setBatchNo(TTKCommon.checkNull(rs.getString("batch_number")));
		                 	if(rs.getString("DECISION_DATE")!=null)
		                 	pbmPreAuthVO.setDecisionDtOfClaim(rs.getString("DECISION_DATE")); 
		                 	else
		                 		pbmPreAuthVO.setDecisionDtOfClaim("");
		            		 if("Approved".equals(rs.getString("status"))){
			            			
			            			pbmPreAuthVO.setClaimStatus("APR");
			            		 }
		            		 else if("Rejected".equals(rs.getString("status"))){
		            			 pbmPreAuthVO.setClaimStatus("REJ");
		            		 }
		            		 
		            		 if("In Progress".equals(rs.getString("status"))){
		            			
		            			pbmPreAuthVO.setSuppressLink(strSuppressLink);
		            	      }
		            		 
		                 	if("INP-ENH".equals(rs.getString("IN_PROGESS_STATUS")))
		                 	{	
		                 		pbmPreAuthVO.setStatus("In-Progress-Resubmission"); 
		                 		pbmPreAuthVO.setInProImageName("InprogressAppeal");
		                 		pbmPreAuthVO.setInProImageTitle("InProgress Resubmission");
		                 	}
		                     else if("INP-RES".equals(rs.getString("IN_PROGESS_STATUS")))
		                 	{
		                    	 pbmPreAuthVO.setStatus("In-Progress-Shortfall Responded");
		                    	 pbmPreAuthVO.setInProImageName("AddIcon");
		                    	 pbmPreAuthVO.setInProImageTitle("InProgress Shortfall Responded");
		                 	}
		                     else
		                     {
		                    	pbmPreAuthVO.setStatus(TTKCommon.checkNull(rs.getString("status")));
		                     	pbmPreAuthVO.setInProImageName("Blank");
		                     	pbmPreAuthVO.setInProImageTitle("");
		                     }
		            		
		                	alPreAuthSearchVOs.add(pbmPreAuthVO);
		                	 
		                }//end of while(rs.next())
		            }//end of if(rs != null)
		            
		            return alPreAuthSearchVOs;
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
		        	closeResource("getPbmClaimList()",rs,cllStm,conn);
				}
					
		    
			}//public ArrayList<PbmPreAuthVO> getPbmPreAuthList(ArrayList<Object> alData) throws TTKException{
			
	
	
	
	
	

	//getPBMSubmitClaim
	
	public PbmPreAuthVO getPBMSubmitClaim(ArrayList<Object> claimData) throws TTKException{
		
		        Connection conn = null;
		        CallableStatement cllStm=null;
		       
		      
		       
		        ResultSet rs=null;
		        PbmPreAuthVO pbmPreAuthVO=new PbmPreAuthVO();
		        
		        try{
		            conn = ResourceManager.getConnection();
		            cllStm	=	conn.prepareCall(strSubmitClaim);
		         
		            cllStm.setLong(1,(Long)claimData.get(0));
		            cllStm.setString(2,(String)claimData.get(1));
		            cllStm.setString(3,(String)claimData.get(2));
		            cllStm.registerOutParameter(4,oracle.jdbc.OracleTypes.CURSOR );
		            	
		             cllStm.execute();
		             rs=(ResultSet)cllStm.getObject(4);
		             ResultSetMetaData resultSetMetaData=rs.getMetaData();
		             int length=resultSetMetaData.getColumnCount();
		             
		             if(rs!=null){
		            	 while(rs.next()){
		            		 pbmPreAuthVO.setInvoiceNo(rs.getString("INVOICE_NUMBER"));
		            		 pbmPreAuthVO.setBatchNo(rs.getString("BATCH_NO"));
		            		 pbmPreAuthVO.setClmSeqId(rs.getLong("claim_seq_id"));
		            		 pbmPreAuthVO.setClmBatchSeqId(rs.getLong("CLM_BATCH_SEQ_ID"));
		            		 pbmPreAuthVO.setClaimType(rs.getString("CLAIM_TYPE"));
		            		 pbmPreAuthVO.setClaimNo(rs.getString("CLAIM_NUMBER"));
		            		 pbmPreAuthVO.setClaimSubmittedDate(rs.getString("CLM_RECEIVED_DATE"));
		            		 pbmPreAuthVO.setClaimedAmount(rs.getString("TOT_ALLOWED_AMOUNT"));
		            		 pbmPreAuthVO.setClaimStatus(rs.getString("CLM_STATUS_TYPE_ID"));
		        		     pbmPreAuthVO.setClinicianID(rs.getString("CLINICIAN_ID"));
		            		 pbmPreAuthVO.setClinicianName(rs.getString("CLINICIAN_NAME"));
		            		 pbmPreAuthVO.setPolicyNo(rs.getString("policy_number"));
		            		 pbmPreAuthVO.setMemberName(rs.getString("mem_name"));
		            	 }
		             }
		            	
		            	 
		            return pbmPreAuthVO;
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
		        	closeResource("getPBMSubmitClaim()",rs,cllStm,conn);
				}
					
		    
			}//public ArrayList<PbmPreAuthVO> getPbmPreAuthList(ArrayList<Object> alData) throws TTKException{
	
	
	//claim details
	
	 public PbmPreAuthVO getClaimDetails(Long claimSeqNO, Long preAuthSeqId) throws TTKException
	 {
			
	        Connection conn = null;
	        CallableStatement cllStm=null;
	        DiagnosisDetailsVO diagnosisDetailsVO=null;
	        ActivityDetailsVO activityDetailsVO=null;
	        ArrayList<DiagnosisDetailsVO> allIcds=new ArrayList<>();
	        ArrayList<ActivityDetailsVO> allAct=new ArrayList<>();
	        BigDecimal totalRequestedAmt=new BigDecimal(0);
	        BigDecimal totalApprovedAmt=new BigDecimal(0);
	        BigDecimal totalRequestedAmtforApp=new BigDecimal(0);
	        ResultSet rs=null;
	        PbmPreAuthVO pbmPreAuthVO=new PbmPreAuthVO();
	        ResultSet rsIcd=null;
	        ResultSet rsDrug=null;
	        ResultSet rsPreAuth =null;
	        try{
	            conn = ResourceManager.getConnection();
	            cllStm	=	conn.prepareCall(strclaimDetails);
	         
	            cllStm.setLong(1,(Long)claimSeqNO);
	            if(preAuthSeqId != null){
	            cllStm.setLong(2, (Long)preAuthSeqId);
	            }else{
	            	cllStm.setString(2, null);	
	            }
	            
	            cllStm.registerOutParameter(3,oracle.jdbc.OracleTypes.CURSOR );
	            cllStm.registerOutParameter(4,oracle.jdbc.OracleTypes.CURSOR );
	            cllStm.registerOutParameter(5,oracle.jdbc.OracleTypes.CURSOR );
	            cllStm.registerOutParameter(6,oracle.jdbc.OracleTypes.CURSOR );	
	             cllStm.execute();
	             rs=(ResultSet)cllStm.getObject(3);
	             rsIcd=(ResultSet)cllStm.getObject(4);
	             rsDrug=(ResultSet)cllStm.getObject(5);
	             rsPreAuth = (ResultSet)cllStm.getObject(6);
	             
	             
	             if(rs!=null){
	            	 while(rs.next()){
	            		 pbmPreAuthVO.setInvoiceNo(rs.getString("INVOICE_NUMBER"));
	            		 pbmPreAuthVO.setBatchNo(rs.getString("BATCH_NO"));
	            		 pbmPreAuthVO.setClmSeqId(rs.getLong("claim_seq_id"));
	            		 pbmPreAuthVO.setClmBatchSeqId(rs.getLong("CLM_BATCH_SEQ_ID"));
	            		 pbmPreAuthVO.setClaimType(rs.getString("CLAIM_TYPE"));   
	            		 pbmPreAuthVO.setClaimNo(rs.getString("CLAIM_NUMBER"));
	            		 pbmPreAuthVO.setClaimSubmittedDate(rs.getString("CLM_RECEIVED_DATE"));
	            		 pbmPreAuthVO.setClaimedAmount(rs.getString("requested_amount"));
	            		 pbmPreAuthVO.setClaimStatus(rs.getString("CLAIM_STATUS"));
	        		     pbmPreAuthVO.setClinicianIDForClaim(rs.getString("CLINICIAN_ID"));
	            		 pbmPreAuthVO.setClinicianName(rs.getString("CLINICIAN_NAME"));
	            		 pbmPreAuthVO.setPolicyNo(rs.getString("policy_number"));
	            		 pbmPreAuthVO.setMemberName(rs.getString("mem_name"));
	            		 
	            		 pbmPreAuthVO.setSettlementNO(rs.getString("SETTLEMENT_NUMBER"));
	            	 }
	             }
	            
	             if(rsIcd!=null){
                	 while(rsIcd.next()){
                		 diagnosisDetailsVO=new DiagnosisDetailsVO();
                		 diagnosisDetailsVO.setDiagSeqId(rsIcd.getLong("diag_seq_id"));
                		 diagnosisDetailsVO.setAilmentDescription(rsIcd.getString("icd_description"));
                		 diagnosisDetailsVO.setIcdCode(rsIcd.getString("diagnosys_code"));
                		 diagnosisDetailsVO.setPrimaryAilment(rsIcd.getString("primary_ailment_yn"));
                		 diagnosisDetailsVO.setIcdCodeSeqId(rsIcd.getLong("icd_code_seq_id"));
                		 allIcds.add(diagnosisDetailsVO);
                	 }
                	 
             }
            	 if(rsDrug!=null){
            		 int sNO=1;
                	 while(rsDrug.next()){
                		 activityDetailsVO=new ActivityDetailsVO();
                		 activityDetailsVO.setSerialNo(sNO);
                		
                		 activityDetailsVO.setActivityCodeDesc(rsDrug.getString("activity_description"));
                		 activityDetailsVO.setActivityStatus(rsDrug.getString("activity_status"));
                		 activityDetailsVO.setQuantity(rsDrug.getFloat("quantity"));
                		 activityDetailsVO.setQuantityInt(rsDrug.getInt("quantity"));
                		 activityDetailsVO.setUnitType(rsDrug.getString("unit_type")); 
                		 activityDetailsVO.setProviderRequestedAmt(rsDrug.getBigDecimal("req_amount"));
                		 
                		 if(rsDrug.getBigDecimal("REQ_AMOUNT")!=null){
                    		 totalRequestedAmt=totalRequestedAmt.add(rsDrug.getBigDecimal("REQ_AMOUNT"));
                    		 }
                    		 if(rsDrug.getBigDecimal("req_amount")!=null&& "Approved".equals(rsDrug.getString("activity_status")))
                    		 {  
                    			 totalRequestedAmtforApp=totalRequestedAmtforApp.add(rsDrug.getBigDecimal("req_amount"));
                    			 
                    		 }
                    		 
                    		 activityDetailsVO.setApprovedAmount(rsDrug.getBigDecimal("approved_amt"));
                    		 if(rsDrug.getBigDecimal("approved_amt")!=null){
                    			 totalApprovedAmt=totalApprovedAmt.add(rsDrug.getBigDecimal("approved_amt"));
                        		 }	 
                    		 activityDetailsVO.setPatientShare(rsDrug.getBigDecimal("patient_share_amount"));
                    		 activityDetailsVO.setDuration(rsDrug.getInt("duration_days"));
                    		 activityDetailsVO.setDenialDescription(rsDrug.getString("denial_code"));
                    		 activityDetailsVO.setDenialRemarks(rsDrug.getString("remarks"));
                    		 activityDetailsVO.setDateOfApproval(rsDrug.getString("date_approval"));
                	
                		 
                	
                		 
                		 allAct.add(activityDetailsVO);
                		 sNO++;
                	 }
             }
            	 
            if(rsPreAuth != null){
            	while(rsPreAuth.next()){
            		pbmPreAuthVO.setPreAuthNO(rsPreAuth.getString("pre_auth_number"));
            		pbmPreAuthVO.setAuthorizationNO(TTKCommon.checkNull(rsPreAuth.getString("auth_number")));
            		pbmPreAuthVO.setEnrolmentID(TTKCommon.checkNull(rsPreAuth.getString("alkoot_id")));
            		pbmPreAuthVO.setDateOfTreatment(TTKCommon.checkNull(rsPreAuth.getString("prescreption_date")));
            		pbmPreAuthVO.setTransactionDate(TTKCommon.checkNull(rsPreAuth.getString("transaction_date")));
            		pbmPreAuthVO.setClinicianID(TTKCommon.checkNull(rsPreAuth.getString("clinician_id")));
            		pbmPreAuthVO.setInsuranceCompanyName(TTKCommon.checkNull(rsPreAuth.getString("ins_comp_name")));
            		pbmPreAuthVO.setDecisionDt(TTKCommon.checkNull(rsPreAuth.getString("decision_date")));
            		pbmPreAuthVO.setEventRefNo(TTKCommon.checkNull(rsPreAuth.getString("event_reference_no")));
            		
            	}
            }
            	 pbmPreAuthVO.setIcdDetails(allIcds);
            	 pbmPreAuthVO.setDrugDetails(allAct);
            	 pbmPreAuthVO.setTotalReqAmt(totalRequestedAmt);
            	 pbmPreAuthVO.setTotalApprAmt(totalApprovedAmt);
	             pbmPreAuthVO.setTotalReqAmtforApp(totalRequestedAmtforApp);	 
	            return pbmPreAuthVO;
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
	        	closeResource("getClaimDetails()",rsIcd,null,null);
	        	closeResource("getClaimDetails()",rsDrug,null,null);
	        	closeResource("getClaimDetails()",rsPreAuth,null,null);
	        	closeResource("getClaimDetails()",rs,cllStm,conn);
			}
				
	    
		}//public ArrayList<PbmPreAuthVO> getPbmPreAuthList(ArrayList<Object> alData) throws TTKException{


	
	
	
	
	
	
	
	
	
	
	//SavePBMUploadFile
	
	 public long SavePBMUploadFile(String preAuthSeqID,String generateType,FormFile formFile) throws TTKException
	 {
			
	        Connection conn = null;
	        CallableStatement cllStm=null;     
	       
        try{ 
	        	byte[] iStream	=	formFile.getFileData();
	            conn = ResourceManager.getConnection();
	            cllStm	=	conn.prepareCall(strPBMUploadFile);
	         
	            cllStm.setString(1,(String)preAuthSeqID);
	            cllStm.setString(2,(String)formFile.getFileName());
	            cllStm.setString(3,(String)generateType);
	            cllStm.setBytes(4,iStream);
	            cllStm.registerOutParameter(5,oracle.jdbc.OracleTypes.INTEGER);
	            	
	             cllStm.execute();
	         long count= cllStm.getInt(5);
	             
	             
	            	 
	            return count;
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
	        	closeResource("SavePBMUploadFile()",null,cllStm,conn);
			}
				
	    
		}//public ArrayList<PbmPreAuthVO> getPbmPreAuthList(ArrayList<Object> alData) throws TTKException{



	
	

private void closeResource(String methodName,ResultSet  rs,Statement stm,Connection con){
try{
	try{
		if(rs!=null)rs.close();	
	    if(stm!=null)stm.close();
	   }catch(Exception e){		  
		   log.error("Error while closing the Connection Resource in OnlinePbmProviderDAOImpl "+methodName,e);
		   }
	if(con!=null)con.close();
}catch(Exception e){
	rs=null;
	stm=null;
	con=null;
	 log.error("Error while closing the Connection Resource in OnlinePbmProviderDAOImpl "+methodName,e);
}
}//private void closeResource(ResultSet  rs,Statement stm,Connection con){

public ActivityDetailsVO getTariffDetails(PbmPreAuthVO pbmPreAuthVO)throws TTKException {
	
	
	Connection conn = null;
    CallableStatement cllStm=null;
    ResultSet rsDrug=null;    
    try{
    	ActivityDetailsVO activityDetailsVO=null;
        conn = ResourceManager.getConnection();
        cllStm	=	conn.prepareCall(strGetTariffDetails);
        cllStm.setLong(1,pbmPreAuthVO.getHospitalSeqID());
        cllStm.setString(2,pbmPreAuthVO.getEnrolmentID());
    //	cllStm.setLong(3,pbmPreAuthVO.getDrugCodeSeqID());
        cllStm.setString(3,pbmPreAuthVO.getDrugCode());	// drug code
        cllStm.setString(4,pbmPreAuthVO.getDateOfTreatment());
        cllStm.setString(5,pbmPreAuthVO.getUnitType());
        
        cllStm.registerOutParameter(6,OracleTypes.CURSOR);                
        cllStm.execute();
        rsDrug=(ResultSet)cllStm.getObject(6);
         
         if(rsDrug!=null){
        	 if(rsDrug.next()){
        		 activityDetailsVO=new ActivityDetailsVO();
        		 //activityDetailsVO.setActivityCodeDesc(rsDrug.getString("ACTIVITY_DESCRIPTION"));        		 
        		 activityDetailsVO.setGrossAmount(rsDrug.getBigDecimal("GROSS_AMOUNT"));
        		 activityDetailsVO.setDiscount(rsDrug.getBigDecimal("UNIT_DISCOUNT"));     
        		 activityDetailsVO.setActivityCode(rsDrug.getString("ACTIVITY_CODE"));     
        		 activityDetailsVO.setStartDate(rsDrug.getString("START_DATE"));     
        		 activityDetailsVO.setActivityServiceType(rsDrug.getString("ACTIVITY_TYPE_SEQ_ID"));     
        		 activityDetailsVO.setMophCodes(rsDrug.getString("MOPH_CODES"));  
        		 activityDetailsVO.setActivityCodeSeqId(rsDrug.getLong("activity_seq_id"));
        
        	 }
     }
        return activityDetailsVO;
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
    	closeResource("getTariffDetails()",rsDrug,cllStm,conn);
	}
}

public long requstAuthWebservice(Long preAuthSeqID, PbmPreAuthVO pbmPreAuthVO) throws TTKException{
	Connection conn = null;
    CallableStatement webserviceData=null;
    ResultSet rsDrug=null;    
    try{
        conn = ResourceManager.getConnection();
        webserviceData = conn.prepareCall(strWebserviceCode);
          
          
        
          XMLType  xmlInputType =   XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), pbmPreAuthVO.getInputPBMXML());
          XMLType  xmlResponseType =   XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), pbmPreAuthVO.getResponsePBMXML());
            
            

          
        webserviceData.setObject(1, xmlInputType);
        webserviceData.setObject(2, xmlResponseType);
        webserviceData.setString(3, "PAT");
        webserviceData.setLong(4,preAuthSeqID);
        webserviceData.setLong(5,pbmPreAuthVO.getAddedBy());            
        webserviceData.execute();
        long flag =1l;
        return flag;
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
    	closeResource("requstAuthWebservice()",rsDrug,webserviceData,conn);
	}
}

 
public long requstComp_preauth(Long preAuthSeqID, PbmPreAuthVO pbmPreAuthVO) throws TTKException{
	Connection conn = null;
    CallableStatement webserviceData=null;
    ResultSet rsDrug=null;    
    try{
        conn = ResourceManager.getConnection();
        webserviceData = conn.prepareCall(strComp_Preauth);
        webserviceData.setLong(1,preAuthSeqID);
        webserviceData.setLong(2,pbmPreAuthVO.getAddedBy());            
        webserviceData.execute();
        long flag =1l;
        return flag;
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
    	closeResource("requstPreauthAuthWebservice()",rsDrug,webserviceData,conn);
	}
}//End Of requstPreauthAuthWebservice()

public String saveClaimXML(InputStream inputStream, String fileName,
		Long userSeqId) throws TTKException {
	Connection conn 	= null;
	CallableStatement cStmtObject=null;
	Reader reader		=	null;
	FileWriter fileWriter	=	null;
	String BatchRefNo = "";
    try{
    	
        //------------
    	conn = ResourceManager.getConnection();
		
		XMLType poXML = null;
		if(inputStream != null)
		{
			poXML = XMLType.createXML (((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), inputStream);
		}
		cStmtObject=conn.prepareCall(strSaveClaimsXML); 
		cStmtObject.setLong(1,0);
		cStmtObject.registerOutParameter(1, OracleTypes.BIGINT);
		cStmtObject.setObject(2, poXML);
		cStmtObject.setLong(3, userSeqId);
		cStmtObject.setString(4, fileName);
		cStmtObject.execute();
		
		BatchRefNo=(BatchRefNo + cStmtObject.getInt(1)).trim();
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
    	try // First try closing the Statement
    	{
    		try
    		{
    			if (cStmtObject != null) cStmtObject.close();
    		}//end of try
    		catch (SQLException sqlExp)
    		{
    			log.error("Error while closing the Statement in ClaimsDAOImpl saveProviderClaims()",sqlExp);
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
    				log.error("Error while closing the Connection in ClaimsDAOImpl saveProviderClaims()",sqlExp);
    				throw new TTKException(sqlExp, "onlineforms");
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
    		throw new TTKException(exp, "onlineforms");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		reader=null;
    		fileWriter=null;
    		cStmtObject = null;
    		conn = null;
    	}//end of finally
	}//end of finally
    return BatchRefNo;
}

public String[] uploadingClaims(String batchRefNo, Long userSeqId) throws TTKException {
	

	Connection conn 	= null;
	CallableStatement cStmtObject=null;
	Reader reader		=	null;
	FileWriter fileWriter	=	null;
	String[] batchNo		=	new String[10];
    try{
    	
        //------------
    	conn = ResourceManager.getConnection();
		cStmtObject=conn.prepareCall(strClaimupload); 
		
		cStmtObject.setLong(1,Long.parseLong(batchRefNo));
		cStmtObject.registerOutParameter(1, OracleTypes.BIGINT);
		cStmtObject.registerOutParameter(2, OracleTypes.VARCHAR);
		cStmtObject.registerOutParameter(3, OracleTypes.VARCHAR);
		cStmtObject.registerOutParameter(4, OracleTypes.VARCHAR);
		cStmtObject.registerOutParameter(5, OracleTypes.VARCHAR);
		cStmtObject.registerOutParameter(6, OracleTypes.VARCHAR);
		cStmtObject.registerOutParameter(7, OracleTypes.VARCHAR);
		cStmtObject.registerOutParameter(8, OracleTypes.VARCHAR);
		cStmtObject.registerOutParameter(9, OracleTypes.VARCHAR);
		cStmtObject.registerOutParameter(10, OracleTypes.VARCHAR);
		cStmtObject.setLong(11,userSeqId);
		cStmtObject.execute();
		
		batchNo[0]=(""+cStmtObject.getInt(1)).trim();
		batchNo[1]=cStmtObject.getString(2);
		batchNo[2]=cStmtObject.getString(3);
		batchNo[3]=cStmtObject.getString(4);
		batchNo[4]=cStmtObject.getString(5);
		batchNo[5]=cStmtObject.getString(6);
		batchNo[6]=cStmtObject.getString(7);
		batchNo[7]=cStmtObject.getString(8);
		batchNo[8]=cStmtObject.getString(9);
		batchNo[9]=cStmtObject.getString(10);
		
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
    	try // First try closing the Statement
    	{
    		try
    		{
    			if (cStmtObject != null) cStmtObject.close();
    		}//end of try
    		catch (SQLException sqlExp)
    		{
    			log.error("Error while closing the Statement in ClaimsDAOImpl saveProviderClaims()",sqlExp);
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
    				log.error("Error while closing the Connection in ClaimsDAOImpl saveProviderClaims()",sqlExp);
    				throw new TTKException(sqlExp, "onlineforms");
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
    		throw new TTKException(exp, "onlineforms");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		reader=null;
    		fileWriter=null;
    		cStmtObject = null;
    		conn = null;
    	}//end of finally
	}//end of finally
    return batchNo;

}

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
				log.error("Error while closing the Resultset in OnlinePbmProviderDAOImpl saveAppealComments()",sqlExp);
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
					log.error("Error while closing the Statement in OnlinePbmProviderDAOImpl saveAppealComments()",sqlExp);
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
						log.error("Error while closing the Connection in OnlinePbmProviderDAOImpl saveAppealComments()",sqlExp);
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
	return aplComment;
 }

public PbmPreAuthVO geMemberDetailsOnEnrollId(String enrollId, String benifitTypeVal, Long hospSeqId) throws TTKException{
	PbmPreAuthVO cashlessDetailVO	=null;
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs	=	null;
	ResultSet rs1	=	null;
	String[] netArry	=	new String[2];
	try{
		
			conn = ResourceManager.getConnection();
			
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strGetMemberOnEnroll);
			cStmtObject.setString(1,enrollId);//EnrollId
			cStmtObject.setString(2,benifitTypeVal);//benifitType
			cStmtObject.setLong(3,hospSeqId);//benifitType
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			//cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(5,OracleTypes.VARCHAR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(4);
			//rs1 = (java.sql.ResultSet)cStmtObject.getObject(5);
		if(rs != null)
		{
			if(rs.next())
			{
				cashlessDetailVO=new PbmPreAuthVO();
				cashlessDetailVO.setMemberName(rs.getString("MEM_NAME"));
				cashlessDetailVO.setAge(String.valueOf(rs.getLong("MEM_AGE")));
				cashlessDetailVO.setGender(rs.getString("GENDER"));
				cashlessDetailVO.setPayer(rs.getString("PAYER_NAME"));
				cashlessDetailVO.setEligibility(rs.getString("ELIGIBILITY"));
				cashlessDetailVO.setVipDetails(rs.getString("VIP"));
				//System.out.println("vip:::::::"+rs.getString("VIP"));
				cashlessDetailVO.setDeductable(rs.getString("DEDUCTIBLE"));
				cashlessDetailVO.setCoPay(rs.getString("CO_PARTICIPATION"));
				//cashlessDetailVO.setApplProcudure(rs.getString("APPLICABLE_PROCEDURE"));
				cashlessDetailVO.setExclusions(rs.getString("EXCLUSIONS"));
				cashlessDetailVO.setInsurredName(rs.getString("INSURED_NAME"));
				cashlessDetailVO.setPreApprLimit(rs.getString("PRE_APPRVL_LIMIT"));
				cashlessDetailVO.setEnrollId(rs.getString("TPA_ENROLLMENT_ID"));
				cashlessDetailVO.setMemDob(rs.getDate("MEM_DOB"));
				cashlessDetailVO.setMemberSeqID(rs.getLong("MEMBER_SEQ_ID"));
				/*String temp	=	rs.getString("ELIG_DENIAL_REASON");
				if(temp!=null || temp!="")
					temp*/
				cashlessDetailVO.setReasonForRejection(rs.getString("ELIG_DENIAL_REASON"));
				netArry[0]		=	rs.getString("PRIMARY_NETWORK");//PRIMARY NETWORK;//"GN";//rs.getString("DEDUCTIBLE");//PRIMARY NETWORK
				netArry[1]		=	rs.getString("PRODUCTTYPE");//"SN";//rs.getString("DEDUCTIBLE");//POLICY NETWORK
				cashlessDetailVO.setNetworksArray(netArry);				
				cashlessDetailVO.setEmirateID(rs.getString("EMIRATE_ID"));

				cashlessDetailVO.setPolicyNo(rs.getString("POLICY_NO"));
				cashlessDetailVO.setPolicyStDt(rs.getString("POLICY_ST_DT"));
				cashlessDetailVO.setPolicyEnDt(rs.getString("POLICY_EN_DT"));
				cashlessDetailVO.setInitPolicyNo(rs.getString("INITIAL_POLICY_NO"));
				cashlessDetailVO.setMemberStartDate(TTKCommon.checkNull(rs.getString("member_st_dt")));
				cashlessDetailVO.setMemberEndDate(TTKCommon.checkNull(rs.getString("member_en_dt")));
				cashlessDetailVO.setRenewedMemberStartDate(TTKCommon.checkNull(rs.getString("RENEW_POLICY_START_DATE")));
				cashlessDetailVO.setRenewedMemberEndDate(TTKCommon.checkNull(rs.getString("RENEW_POLICY_END_DATE")));
				cashlessDetailVO.setBufferFlag(TTKCommon.checkNull(rs.getString("buffer_warning")));
			}//end of while(rs.next())
		}//end of if(rs != null)
		
		/*if(rs1 != null)
		{
			Map<String, String> provNetworksMap	=	new LinkedHashMap<String, String>();
			while(rs1.next())
			{
				provNetworksMap.put(rs1.getString("NETWORK_TYPE"), rs1.getString("NETWORK_YN"));
				netArry[2]		=	rs1.getString("CN_YN");//"N";//rs.getString("DEDUCTIBLE");//CN
				netArry[3]		=	rs1.getString("GN_YN");//"Y";//rs.getString("DEDUCTIBLE");//GN
				netArry[4]		=	rs1.getString("SN_YN");//"Y";//rs.getString("DEDUCTIBLE");//SN
				netArry[5]		=	rs1.getString("BN_YN");//"N";//rs.getString("DEDUCTIBLE");//BN
				netArry[6]		=	rs1.getString("WN_YN");//"N";//rs.getString("DEDUCTIBLE");//WN
				
			}//end of while(rs.next())
			cashlessDetailVO.setAssNetworksArray(provNetworksMap);
		}//end of if(rs != null)
		*/
			//lResult++;
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
				//if (rs1 != null) rs1.close();
				if (rs != null) rs.close();
			
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in OnlinePbmProviderDAOImpl geMemberDetailsOnEnrollId()",sqlExp);
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
					log.error("Error while closing the Statement in OnlinePbmProviderDAOImpl geMemberDetailsOnEnrollId()",sqlExp);
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
						log.error("Error while closing the Connection in OnlinePbmProviderDAOImpl geMemberDetailsOnEnrollId()",sqlExp);
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
			//rs1 = null;
			rs=null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return cashlessDetailVO;
}

public String[] getTobForBenefits(String benifitTypeVal, String enrollId)  throws TTKException{
	CashlessDetailVO cashlessDetailVO	=new CashlessDetailVO();
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs1	=	null;
	String[] tobBenefits	=	new String[14];
	try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strGetTOBForBenefir);
			cStmtObject.setString(1,benifitTypeVal);//EnrollId
			cStmtObject.setString(2,enrollId);//benifitType
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
		if(rs1 != null)
		{
			if(rs1.next())
			{
				tobBenefits[0]	=	rs1.getString("COPAY");
				tobBenefits[1]	=	rs1.getString("ELIGIBILITY");
				tobBenefits[2]	=	rs1.getString("DEDUCTIBLE");
				tobBenefits[3]	=	rs1.getString("CO_INS");
				tobBenefits[4]	=	rs1.getString("CLASS");
				tobBenefits[5]	=	rs1.getString("MATERNITY_YN");
				tobBenefits[6]	=	rs1.getString("MATERNITY_COPAY");
				tobBenefits[7]	=	rs1.getString("OPTICAL_YN");
				tobBenefits[8]	=	rs1.getString("OPTICAL_COPAY");
				tobBenefits[9]	=	rs1.getString("DENTAL_YN");
				tobBenefits[10]	=	rs1.getString("DENTAL_COPAY");
				tobBenefits[11]	=	rs1.getString("IP_OP_SERVICES");
				tobBenefits[12]	=	rs1.getString("PHARMACEUTICALS");
				tobBenefits[13] =   rs1.getString("Psychiatric_allowed");
			}//end of if(rs1.next())
		}//end of if(rs1 != null)
		
		
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
		try // First try closing the result set
		{
			try
			{
				if (rs1 != null) rs1.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in OnlineAccessDAOImpl getTobForBenefits()",sqlExp);
				throw new TTKException(sqlExp, "user");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null)	cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in OnlineAccessDAOImpl getTobForBenefits()",sqlExp);
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
						log.error("Error while closing the Connection in OnlineAccessDAOImpl getTobForBenefits()",sqlExp);
						throw new TTKException(sqlExp, "user");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "user");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			rs1 = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return tobBenefits;
}

public int updateLogTable(ArrayList<String> dataList) throws TTKException{
	int update=0;
	try(Connection connection=ResourceManager.getConnection();
		CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(updateLogProc)){
		cStmtObject.setString(1, dataList.get(0));
		cStmtObject.setString(2, dataList.get(1));
		cStmtObject.setString(3, dataList.get(2));
		cStmtObject.setString(4, dataList.get(3));
		cStmtObject.setString(5, dataList.get(4));
		cStmtObject.setString(6, dataList.get(5));
		cStmtObject.setString(7, dataList.get(6));
		cStmtObject.setString(8, dataList.get(7));
		cStmtObject.setString(9, dataList.get(8));
		cStmtObject.setString(10, dataList.get(9));
		cStmtObject.setString(11, dataList.get(10));
		cStmtObject.setString(12, dataList.get(11));
		cStmtObject.setString(14, dataList.get(12));
		cStmtObject.setString(15, dataList.get(13));
		cStmtObject.setString(16, dataList.get(14));
		cStmtObject.setString(17, dataList.get(15));
		cStmtObject.registerOutParameter(13,OracleTypes.INTEGER);//12 No
		cStmtObject.execute();
		update = cStmtObject.getInt(13);
	}catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "onlineforms");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "onlineforms");
	}
	return update;
} 

}//end of OnlinePbmProviderDAOImpl
