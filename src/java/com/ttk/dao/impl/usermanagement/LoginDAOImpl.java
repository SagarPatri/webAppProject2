/**
 * @ (#) LoginDAOImpl.java Jul 29, 2005
 * Project      :
 * File         : LoginDAOImpl.java
 * Author       : Nagaraj D V
 * Company      :
 * Date Created : Jul 29, 2005
 *
 * @author       :  Nagaraj D V
 * Modified by   : Arun K N
 * Modified date : Mar 25, 2006
 * Reason        : for updating userValidation method.
 */

package com.ttk.dao.impl.usermanagement;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import sourceafis.simple.AfisEngine;
import sourceafis.simple.Finger;
import sourceafis.simple.Fingerprint;
import sourceafis.simple.Person;
import sourceafis.templates.TemplateBuilder;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.usermanagement.UserVO;
//Changes Added for Password Policy CR KOC 1235
//End changes for Password Policy CR KOC 1235

public class LoginDAOImpl implements BaseDAO,Serializable{
	 private static Logger log = Logger.getLogger(LoginDAOImpl.class );
	 private static final String strUserValidation="{CALL AUTHENTICATION_PKG.PR_USER_VALIDATION(?,?,?,?)}";		//Changes Added one parameter for Password Policy CR KOC 1235
     private static final String strExternalUserValidation="{CALL AUTHENTICATION_PKG.PR_EXTERNAL_USER_VALIDATION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 1 param for single sign on Modified as per KOC 1257 11PP PASSWORD POLICY FOR ONLINE
     private static final String strIpruUserValidation = "{CALL AUTHENTICATION_PKG.PR_IPRU_USER_VALIDATION(?,?,?)}";
     private static final String strGetFingerPrintData="{CALL FINGER_PRINT_PKG.SELECT_FINGER_PRINT(?,?)}";
     private static final String strGerCredentials = "{CALL FINGER_PRINT_PKG.GET_CREDENTIALS (?,?)}";
     //private static final String strFingerPrintReg = "{CALL FINGER_PRINT_PKG.SAVE_FING_PRINT(?,?,?,?)}";
	 /**
     * This method taks UserVO object as input which consits of userid and password to
     * validate the user when user trys to log in.
     * If he is valid User then it returns UserSecurityProfile Dom object which consists of
     * User information and his privileges.
     * If he is not valid User then it catches the Exception thrown from the Stored procedure
     * and displays the appropriate message.
     * @param userVO UserVO
     * @return doc Document Object
     * @throws TTKException if not valid User or any run time Exception occures
     */
    public Document userValidation(UserVO userVO)throws TTKException
    {
        Connection conn = null;
        OracleCallableStatement cStmtObject = null;
        Document doc = null;
        XMLType xml = null;
        try
        {
            conn = ResourceManager.getConnection();
            cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strUserValidation);
            cStmtObject.setString(1,userVO.getUSER_ID());
            cStmtObject.setString(2,userVO.getPassword());
			cStmtObject.setString(3,userVO.getHostIPAddress());	//Changes Added get an ip address for Password Policy CR KOC 1235
            cStmtObject.registerOutParameter(4,OracleTypes.OPAQUE,"SYS.XMLTYPE");
            cStmtObject.execute();
            xml = XMLType.createXML(cStmtObject.getOPAQUE(4));
            DOMReader domReader = new DOMReader();
            doc = xml != null ? domReader.read(xml.getDOM()):null;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "login");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "login");
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
        			log.error("Error while closing the Statement in LoginDAOImpl userValidation()",sqlExp);
        			throw new TTKException(sqlExp, "login");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LoginDAOImpl userValidation()",sqlExp);
        				throw new TTKException(sqlExp, "login");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "login");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return doc;
    }//end of userValidation(UserVO userVO)

   /**
    * This method is used to authenticate the external Users,
    * who log in to the System.
    * If he is valid User then it returns UserSecurityProfile Dom object which consists of
    * User information and his privileges.
    * @param userVO UserVO
    * @return doc Document
    * @throws TTKException if not valid User or any run time Exception occures
    */
    public ArrayList<Object> externalUserValidation(UserVO userVO)throws TTKException
    {
        Connection conn = null;
        OracleCallableStatement cStmtObject = null;
        Document doc = null;
        XMLType xml = null;
        String strExpiryYN="";
        String alertMsg="";//added as per KOC 11PP 1257
		 String strLoginExpiryYN="";;//added as per KOC 11PP 1257
		 String strRandomNo="";;//added as per KOC 1349
        ArrayList<Object> alResult = new ArrayList<Object>();
        try
        {
            conn = ResourceManager.getConnection();
            cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strExternalUserValidation);
            
            if("EMPL".equals(userVO.getLoginType()))
            	cStmtObject.setString(1,null);
            else
            cStmtObject.setString(1,userVO.getUSER_ID());
            cStmtObject.setString(2,userVO.getPassword());
            cStmtObject.setString(3,userVO.getPolicyNo());
            if("EMPL".equals(userVO.getLoginType()))
            cStmtObject.setString(4,userVO.getUSER_ID());
            else
            cStmtObject.setString(4,userVO.getEnrollmentID());
            cStmtObject.setString(5,userVO.getGroupID());
            cStmtObject.setString(6,userVO.getLoginType());
            cStmtObject.registerOutParameter(7,OracleTypes.OPAQUE,"SYS.XMLTYPE");
            cStmtObject.setString(8,userVO.getCertificateNbr());
            cStmtObject.registerOutParameter(9, OracleTypes.CHAR);
            cStmtObject.registerOutParameter(10, OracleTypes.VARCHAR);//added as per KOC 11PP 1257
		    cStmtObject.registerOutParameter(11, OracleTypes.VARCHAR);//added as per KOC 11PP 1257
		    cStmtObject.registerOutParameter(12, OracleTypes.VARCHAR);//added as per KOC 1349
            cStmtObject.setString(13,userVO.getHostIPAddress());//kocbroker
            cStmtObject.setString(14,userVO.getGrpOrInd());//kocprovider
            cStmtObject.setString(15,userVO.getDateOfBirth());//kocprovider            
            cStmtObject.execute();
            xml = XMLType.createXML(cStmtObject.getOPAQUE(7));
            
            strExpiryYN = TTKCommon.checkNull(cStmtObject.getString(9)) ;
            alertMsg = TTKCommon.checkNull(cStmtObject.getString(10)) ; //added as per KOC 11PP 1257
			 strLoginExpiryYN=TTKCommon.checkNull(cStmtObject.getString(11)) ; //added as per KOC 11PP 1257
			 strRandomNo=TTKCommon.checkNull(cStmtObject.getString(12)) ; //added as per KOC 1349
            DOMReader domReader = new DOMReader();
            doc = xml != null ? domReader.read(xml.getDOM()):null;
            alResult.add(doc);
            alResult.add(strExpiryYN);
            alResult.add(alertMsg);//added as per KOC1257 11PP
			alResult.add(strLoginExpiryYN);//added as per KOC1257 11PP
			alResult.add(strRandomNo);//added as per KOC1349
			//this.FingerPrintRegistration(userVO);
        	return (ArrayList<Object>)alResult;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "login");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "login");
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
        			log.error("Error while closing the Statement in LoginDAOImpl externalUserValidation()",sqlExp);
        			throw new TTKException(sqlExp, "login");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LoginDAOImpl externalUserValidation()",sqlExp);
        				throw new TTKException(sqlExp, "login");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "login");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
         
    }//end of externalUserValidation(UserVO userVO)
    
    /**
     * This method returns the UserSecurityProfile object which contains all the user details
     * if he is a valid User.
     * @param strCustomerCode Customer Code
     * @return UserSecurityProfile object which contains all the user details along with their role and profile information
     * @exception throws TTKException
     */
    public Document ipruLoginValidation(UserVO userVO) throws TTKException {
    	Connection conn = null;
        OracleCallableStatement cStmtObject = null;
        Document doc = null;
        XMLType xml = null;

        try
        {
            conn = ResourceManager.getConnection();
            cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strIpruUserValidation);
           
            cStmtObject.setString(1,userVO.getLoginType());
            cStmtObject.setString(2,userVO.getCustomerCode());
            cStmtObject.registerOutParameter(3,OracleTypes.OPAQUE,"SYS.XMLTYPE");
            cStmtObject.execute();
            xml = XMLType.createXML(cStmtObject.getOPAQUE(3));
            DOMReader domReader = new DOMReader();
            doc = xml != null ? domReader.read(xml.getDOM()):null;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "login");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "login");
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
        			log.error("Error while closing the Statement in LoginDAOImpl externalUserValidation()",sqlExp);
        			throw new TTKException(sqlExp, "login");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LoginDAOImpl externalUserValidation()",sqlExp);
        				throw new TTKException(sqlExp, "login");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "login");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return doc;
    }//end of ipruLoginValidation(UserVO userVO)

    
    
   public ArrayList<Object> externalUserValidationUsingFingerPrint(UserVO userVO)throws TTKException
    {
        Connection conn1 = null;
        Connection conn2 = null;
        OracleCallableStatement cStmtObject1 = null;
        OracleCallableStatement cStmtObject2 = null;
       
        ResultSet rs = null;
      
        ArrayList<Object> seqList = new ArrayList<Object>();
        ArrayList<Object> fingerPrintList = new ArrayList<Object>();
        ArrayList<Object> alResult = new ArrayList<Object>();
        
        try
        {
        	conn1 = ResourceManager.getConnection();
            
        	cStmtObject1 = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn1).getUnderlyingConnection()).prepareCall(strGetFingerPrintData);
            cStmtObject1.setString(1,userVO.getGroupID());
            cStmtObject1.registerOutParameter(2, OracleTypes.CURSOR);
            cStmtObject1.execute();
            
            rs = (java.sql.ResultSet)cStmtObject1.getObject(2);
            
            if(rs != null){
				while(rs.next()){
				seqList.add(new Long(rs.getLong("SEQ_ID")));
				fingerPrintList.add(rs.getBytes("FINGER_PRINT"));	
				}
            
            long result = this.MatchFingerPrint(userVO, seqList, fingerPrintList);
            
            if(result > 0){
            	conn2 = ResourceManager.getConnection();
            	cStmtObject2 = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn2).getUnderlyingConnection()).prepareCall(strGerCredentials);
            	cStmtObject2.setLong(1, result);
            	cStmtObject2.registerOutParameter(2, OracleTypes.CURSOR);
            	cStmtObject2.execute();
            	rs = (java.sql.ResultSet)cStmtObject2.getObject(2);
            	while(rs.next()){
                userVO.setUSER_ID(rs.getString("USER_ID"));
                userVO.setPassword(rs.getString("PASS_WORD"));
            	}
            	System.out.println("  = =    match");
            	alResult = this.externalUserValidation(userVO);
            }else
            {
            	System.out.println("  = =  not  match");
            }
			//this.FingerPrintRegistration(userVO);
            }
            return (ArrayList<Object>)alResult;
        }
            //end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "login");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "login");
        }//end of catch (Exception exp)
        finally
		{
        	 //Nested Try Catch to ensure resource closure  
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject1 != null) cStmtObject1.close();
        			if (cStmtObject2 != null) cStmtObject2.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in LoginDAOImpl externalUserValidation()",sqlExp);
        			throw new TTKException(sqlExp, "login");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn1 != null) conn1.close();
        				if(conn2 != null) conn2.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LoginDAOImpl externalUserValidation()",sqlExp);
        				throw new TTKException(sqlExp, "login");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "login");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject1 = null;
        		cStmtObject2 = null;
        		conn1 = null;
        		conn2 = null;
        	}//end of finally
		}//end of finally
         
    }//end of externalUserValidation(UserVO userVO)
   
   private long MatchFingerPrint(UserVO userVO, ArrayList<Object> seqList, ArrayList<Object> fingerPrintList)
   {	
	   AfisEngine afisEngine = new AfisEngine();
	   long result = 0;
	   
	   Person probe = new Person();
		Fingerprint fingerprintOfProbe = new Fingerprint();
		fingerprintOfProbe.setIsoTemplate(this.trimToGetIsoFormat(userVO.getHosFingerPrintFile()));
		fingerprintOfProbe.setFinger(Finger.LEFT_THUMB);
		ArrayList<Fingerprint> listOfFingerPrintOfProbe = new ArrayList<Fingerprint>();
		listOfFingerPrintOfProbe.add(fingerprintOfProbe);
		probe.setFingerprints(listOfFingerPrintOfProbe);
	
	   
	ArrayList<Person> candidates = new ArrayList<Person>();
		for(int i=0;i<seqList.size();i++){
			Person candidate = new Person();
			Fingerprint fingerPrintOfCandidate = new Fingerprint();
			if(fingerPrintList.get(i)!=null)
			{
			fingerPrintOfCandidate.setIsoTemplate(this.trimToGetIsoFormat((byte[]) fingerPrintList.get(i)));
			fingerPrintOfCandidate.setFinger(Finger.LEFT_THUMB);
			ArrayList<Fingerprint> listOfFingerPrintOfCandidate = new ArrayList<Fingerprint>();
			candidate.setId(i);
			listOfFingerPrintOfCandidate.add(fingerPrintOfCandidate);
			candidate.setFingerprints(listOfFingerPrintOfCandidate);
			candidates.add(candidate);
			}
		}
	   
	
	Iterable<Person> potentialCandidates = afisEngine.identify(probe, candidates);
	Iterator<Person> personIterator =  potentialCandidates.iterator();
	
	if(personIterator.hasNext()){
		Person potentialCandiate = personIterator.next();
		float score = afisEngine.verify(probe, potentialCandiate);
		if(score>0)
			result = (long) seqList.get(potentialCandiate.getId());
	}
	
	return result;
 } 
    
   /* public Integer FingerPrintRegistration(UserVO userVO)throws TTKException
    {
        Connection conn = null;
        OracleCallableStatement cStmtObject = null;
        try
        {
            conn = ResourceManager.getConnection();
            cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strFingerPrintReg);
            cStmtObject.setString(1,userVO.getGroupID());
            cStmtObject.setString(2,userVO.getUSER_ID());
            cStmtObject.setBytes(3, userVO.getHosFingerPrintFile());
            cStmtObject.registerOutParameter(4, OracleTypes.INTEGER);
            cStmtObject.execute();
            
            return cStmtObject.getInt(4);
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "Reg-FingerPrint");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "Reg-FingerPrint");
        }//end of catch (Exception exp)
        finally
		{
        	 //Nested Try Catch to ensure resource closure  
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in LoginDAOImpl FingerPrintRegistration()",sqlExp);
        			throw new TTKException(sqlExp, "Reg-FingerPrint");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LoginDAOImpl FingerPrintRegistration()",sqlExp);
        				throw new TTKException(sqlExp, "Reg-FingerPrint");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "Reg-FingerPrint");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    }*/
    
    private byte[] trimToGetIsoFormat(byte[] paramArrayOfByte){
    	
    	try {
		TemplateBuilder localTemplateBuilder = new TemplateBuilder();
		localTemplateBuilder.originalDpi = 500;
		ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
		DataInputStream localDataInputStream = new DataInputStream(localByteArrayInputStream);
		byte[] arrayOfByte1 = new byte[4];
		localDataInputStream.read(arrayOfByte1);
		byte[] arrayOfByte2 = new byte[4];
		localDataInputStream.read(arrayOfByte2);
		int subArraysize = localDataInputStream.readInt();
		byte[] subArray = new byte[subArraysize];
		System.arraycopy(paramArrayOfByte, 0, subArray, 0, subArraysize);
		return subArray;
		
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
}//end of class LoginDAOImpl