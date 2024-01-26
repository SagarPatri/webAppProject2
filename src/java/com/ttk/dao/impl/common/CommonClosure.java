package com.ttk.dao.impl.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ttk.common.exception.TTKException;

public class CommonClosure {
	public static Logger log = Logger.getLogger(CommonClosure.class);

	public static void closeOpenResources(ResultSet rs, Connection conn, CallableStatement cStmtObject,Object obj,String methodName ) throws TTKException{
    	/* Nested Try Catch to ensure resource closure */ 
    	try // First try closing the Statement
    	{
    		try
    		{
    			if (rs != null) rs.close();
    		}//end of try
    		catch (SQLException sqlExp)
    		{
    			log.error("Error while closing the Resultset in "+obj.toString()+" "+methodName+"",sqlExp);
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
    				log.error("Error while closing the Statement in in "+obj.toString()+" "+methodName+"",sqlExp);
    				throw new TTKException(sqlExp, "floataccount");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    			{
    				try
    				{
    					if(conn != null) conn.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Connection in in "+obj.toString()+" "+methodName+"",sqlExp);
    					throw new TTKException(sqlExp, "floataccount");
    				}//end of catch (SQLException sqlExp)
    			}//end of finally Connection Close
    		}//end of try
    	}//end of try
    	catch (TTKException exp)
    	{
    		throw new TTKException(exp, "tTkReports");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		rs = null;
    		cStmtObject = null;
    		conn = null;
    	}//end of finally
    
		
	}
	
	public static void closeOpenResources(Connection conn, CallableStatement cStmtObject,Object obj,String methodName ) throws TTKException{
    	/* Nested Try Catch to ensure resource closure */ 
    	try // First try closing the Statement
    	{
    			try
    			{
    				if (cStmtObject != null) cStmtObject.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Statement in in "+obj.toString()+" "+methodName+"",sqlExp);
    				throw new TTKException(sqlExp, "floataccount");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    			{
    				try
    				{
    					if(conn != null) conn.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Connection in in "+obj.toString()+" "+methodName+"",sqlExp);
    					throw new TTKException(sqlExp, "floataccount");
    				}//end of catch (SQLException sqlExp)
    			}//end of finally Connection Close
    	}//end of try
    	catch (TTKException exp)
    	{
    		throw new TTKException(exp, "tTkReports");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		cStmtObject = null;
    		conn = null;
    	}//end of finally
    
		
	}
	
	public static void closeOpenResources(ResultSet rs,PreparedStatement preparedStatement,Statement statement, Connection conn, CallableStatement cStmtObject,Object obj,String methodName) throws TTKException{
    	/* Nested Try Catch to ensure resource closure */ 
    	try // First try closing the Statement
    	{
    			try
    			{
    				if (cStmtObject != null) cStmtObject.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Statement in in "+obj.toString()+" "+methodName+"",sqlExp);
    				throw new TTKException(sqlExp, "floataccount");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    			{
    				try
    				{
    					if(rs!=null) rs.close();
    					if (preparedStatement != null) preparedStatement.close();
    					if(statement!=null) statement.close();
    					if(conn != null) conn.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Connection in in "+obj.toString()+" "+methodName+"",sqlExp);
    					throw new TTKException(sqlExp, "floataccount");
    				}//end of catch (SQLException sqlExp)
    			}//end of finally Connection Close
    	}//end of try
    	catch (TTKException exp)
    	{
    		throw new TTKException(exp, "tTkReports");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		cStmtObject = null;
    		preparedStatement = null;
    		statement=null;
    		conn = null;
    	}//end of finally
    
		
	}
	
}
