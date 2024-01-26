/**
 * @ (#) MOUDAOImpl.java Dec 7, 2005
 * Project      :
 * File         : MOUDAOImpl.java
 * Author       : Nagaraj D V
 * Company      :
 * Date Created : Dec 7, 2005
 *
 * @author       :  Nagaraj D V
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dao.impl.empanelment;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.OracleConnection;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;

public class MOUDAOImpl implements BaseDAO,Serializable{
	
	private static Logger log = Logger.getLogger(MOUDAOImpl.class);

	private static final String strMOUDetail = "SELECT EMPANEL_SEQ_ID, HOSP_MOU FROM tpa_hosp_mou WHERE EMPANEL_SEQ_ID = (SELECT empanel_seq_id FROM tpa_hosp_empanel_status WHERE hosp_seq_id = ? AND active_yn = 'Y')";
	private static final String strAddUpdateMou = "{CALL HOSPITAL_MOU.PR_MOU_SAVE(?,?,?)}";

	/**
	 * This method returns the modified MOU document
	 * @param lHospSeqId long object which contains the Hospital seq id
	 * @return Document the XML based MOU document
	 * @exception throws TTKException
	 */
	public Document getMOUDocument(long lHospitalSeqId) throws TTKException
	{
		Connection conn = null;
		OracleCallableStatement stmt = null;
		OracleResultSet rs = null;
		Document doc = null;
		XMLType xml = null;

		try{
			conn = ResourceManager.getConnection();
			stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strMOUDetail);
			stmt.setLong(1,lHospitalSeqId);
			rs = (OracleResultSet)stmt.executeQuery();
			if(rs != null){
				while (rs.next()) {
					if(rs.getOPAQUE(2) != null)
					{
						xml = XMLType.createXML(rs.getOPAQUE(2));
					}//end of if(rs.getOPAQUE(2) != null)
				}// end of while
			}//end of if(rs != null)
			DOMReader domReader = new DOMReader();
			if(xml != null)
			{
				doc = domReader.read(xml.getDOM());//read method take w3c document and returns dom4j document
			}//end of if(xml != null)
			return doc;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "mou");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "mou");
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
					log.error("Error while closing the Resultset in MOUDAOImpl getMOUDocument()",sqlExp);
					throw new TTKException(sqlExp, "mou");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (stmt != null) stmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MOUDAOImpl getMOUDocument()",sqlExp);
						throw new TTKException(sqlExp, "mou");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MOUDAOImpl getMOUDocument()",sqlExp);
							throw new TTKException(sqlExp, "mou");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "mou");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				stmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getMOUDocument(long lHospitalSeqId)

	/**
	 * This method adds or updates the MOU details
	 * @param mouDocument Document object which contains MOU details
	 * @param lHospSeqId Long object which contains the hospital sequence id
	 * @param lUpdatedBy Long object which contains the user sequence id
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int addUpdateDocument(Document mouDocument, Long lHospSeqId, Long lUpdatedBy) throws TTKException
	{
		int iResult =1;
		Connection conn = null;
		OracleCallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strAddUpdateMou);
			XMLType poXML = null;
			if(mouDocument != null)
			{
				poXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), mouDocument.asXML());
			}//end of if(mouDocument != null)
			cStmtObject.setLong(1, lHospSeqId);
			cStmtObject.setObject(2, poXML);
			cStmtObject.setLong(3, lUpdatedBy);
			cStmtObject.execute();
			iResult = 1;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "mou");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "mou");
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
        			log.error("Error while closing the Statement in MOUDAOImpl addUpdateDocument()",sqlExp);
        			throw new TTKException(sqlExp, "mou");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MOUDAOImpl addUpdateDocument()",sqlExp);
        				throw new TTKException(sqlExp, "mou");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "mou");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of method addUpdateDocument(Document mouDocument, Long lHospSeqId, Long lUpdatedBy)
}//end of class MOUDAOImpl
