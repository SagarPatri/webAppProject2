/**
 * @ (#) EnrollmentAction.java Feb 10, 2006
 * Project 		: TTK HealthCare Services
 * File 		: EnrollmentAction.java
 * Author 		: Pradeep R
 * Company 		: Span Systems Corporation
 * Date Created : Feb 10, 2006
 *
 * @author 		: Pradeep R
 * Modified by 	: Raghavendra T M
 * Modified date: July 30, 2007
 * Reason 		: doViewAccInfoEnrollment method added for accountinfo 
 */

package com.ttk.action.enrollment;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.ResourceManager;
import com.ttk.action.table.TableData;
import com.ttk.dto.preauth.PreAuthVO;
/**
 * This class is reusable for adding enrollment information in corporate and non corporate policies in enrollment flow.
 * This class also provides option for deleting the selected enrollment.
 */

public class AsynchronusAction extends DispatchAction {
	private static Logger log = Logger.getLogger( AsynchronusAction.class );
	public ActionForward getEmirateIdDescription(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet rs=null;
		try{
		con=ResourceManager.getConnection();
		statement=con.prepareStatement("select b.detail_number,b.detail_desc from tpa_mouse_over_details b  where detail_number= ?");		
	    String eId=(String)request.getParameter("eId");
	         eId=(eId==null)?null:eId.trim();	    
	    
	      statement.setString(1,eId);
	      rs=statement.executeQuery();
	      if(rs.next())writer.println(rs.getString(2));
	      else writer.println("NON");
	      writer.flush();	    
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);			
			}
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
	  				log.error("Error while closing the Resultset in AsynchronusAction getEmirateIdDescription()",sqlExp);
	  				throw new TTKException(sqlExp, "async");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
	  			{
	  				try
	  				{
	  					if (statement != null) statement.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Statement in AsynchronusAction getEmirateIdDescription()",sqlExp);
	  					throw new TTKException(sqlExp, "async");
	  				}//end of catch (SQLException sqlExp)
	  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  				{
	  					try
	  					{
	  						if(con != null) con.close();
	  					}//end of try
	  					catch (SQLException sqlExp)
	  					{
	  						log.error("Error while closing the Connection in AsynchronusAction getEmirateIdDescription()",sqlExp);
	  						throw new TTKException(sqlExp, "async");
	  					}//end of catch (SQLException sqlExp)
	  				}//end of finally Connection Close
	  			}//end of finally Statement Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "async");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			rs = null;
	  			statement = null;
	  			con = null;
			if(writer!=null)writer.close();
	  		}//end of finally
	  	}//end of finally	
        return null;
    }
	public ActionForward getProviderInvoiceNO(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet rs=null;
		try{
			String claimSeqID=(String)request.getParameter("claimSeqID");
			claimSeqID=(claimSeqID==null)?null:claimSeqID.trim();
		con=ResourceManager.getConnection();
		statement=con.prepareStatement("SELECT INVOICE_NUMBER FROM CLM_AUTHORIZATION_DETAILS WHERE CLAIM_SEQ_ID="+claimSeqID);		
	      rs=statement.executeQuery();
	      if(rs.next()){
	    	  writer.print(rs.getString(1));
	      }
	      else writer.print("");
	      
	      writer.flush();	    
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);			
			}
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
	  				log.error("Error while closing the Resultset in AsynchronusAction getProviderInvoiceNO()",sqlExp);
	  				throw new TTKException(sqlExp, "async");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
	  			{
	  				try
	  				{
	  					if (statement != null) statement.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Statement in AsynchronusAction getProviderInvoiceNO()",sqlExp);
	  					throw new TTKException(sqlExp, "async");
	  				}//end of catch (SQLException sqlExp)
	  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  				{
	  					try
	  					{
	  						if(con != null) con.close();
	  					}//end of try
	  					catch (SQLException sqlExp)
	  					{
	  						log.error("Error while closing the Connection in AsynchronusAction getProviderInvoiceNO()",sqlExp);
	  						throw new TTKException(sqlExp, "async");
	  					}//end of catch (SQLException sqlExp)
	  				}//end of finally Connection Close
	  			}//end of finally Statement Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "async");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			rs = null;
	  			statement = null;
	  			con = null;
			if(writer!=null)writer.close();
	  		}//end of finally
	  	}//end of finally	
        return null;
    }
	
	public ActionForward getProviderNames(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet rs=null;
		try{
		con=ResourceManager.getConnection();
		statement=con.prepareStatement("SELECT HOSP_NAME||' ('||HOSP_LICENC_NUMB||')' FROM TPA_HOSP_INFO I JOIN TPA_HOSP_EMPANEL_STATUS S ON (I.HOSP_SEQ_ID=S.HOSP_SEQ_ID) WHERE S.EMPANEL_STATUS_TYPE_ID='EMP'");		
	      rs=statement.executeQuery();
	      while(rs.next())
	      writer.println(rs.getString(1));	      
	     //String data[]={"AAAA","BBBB","CCCC","DDDD"};
	     //for(String dt:data)writer.println(dt);
	      writer.flush();	    
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);			
		}
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
	  				log.error("Error while closing the Resultset in AsynchronusAction getProviderNames()",sqlExp);
	  				throw new TTKException(sqlExp, "async");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
	  			{
	  				try
	  				{
	  					if (statement != null) statement.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Statement in AsynchronusAction getProviderNames()",sqlExp);
	  					throw new TTKException(sqlExp, "async");
	  				}//end of catch (SQLException sqlExp)
	  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  				{
	  					try
	  					{
	  						if(con != null) con.close();
	  					}//end of try
	  					catch (SQLException sqlExp)
	  					{
	  						log.error("Error while closing the Connection in AsynchronusAction getProviderNames()",sqlExp);
	  						throw new TTKException(sqlExp, "");
	  					}//end of catch (SQLException sqlExp)
	  				}//end of finally Connection Close
	  			}//end of finally Statement Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "async");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			rs = null;
	  			statement = null;
	  			con = null;
			if(writer!=null)writer.close();
	  		}//end of finally
	  	}//end of finally	
        return null;
    }
	public ActionForward validateMember(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	PrintWriter	writer= response.getWriter();
		Connection con=null;
		CallableStatement statement=null;
		ResultSet resultSet=null;
		String status;
		try{
	       String memberId=request.getParameter("memberId");
	       memberId=(memberId==null)?"":memberId.trim();
	     con=ResourceManager.getConnection();     
	     statement=con.prepareCall("{CALL AUTHORIZATION_PKG.SELECT_MEMBER(?,?)}");    
	     statement.setString(1,memberId);
	     statement.registerOutParameter(2,OracleTypes.CURSOR);
		statement.execute();
		
		 resultSet=(ResultSet)statement.getObject(2);	
		if(resultSet==null||!resultSet.next()||resultSet.getLong("MEMBER_SEQ_ID")==0)status="MINE";		 
		else status="SUCCE";
	     
	      writer.println(status);  
	      writer.flush();	    
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);
			 status="EO";			
			}finally
		  	{
		  		/* Nested Try Catch to ensure resource closure */
		  		try // First try closing the result set
		  		{
		  			try
		  			{
		  				if (resultSet != null) resultSet.close();
		  			}//end of try
		  			catch (SQLException sqlExp)
		  			{
		  				log.error("Error while closing the Resultset in AsynchronusAction validateMember()",sqlExp);
		  				throw new TTKException(sqlExp, "async");
		  			}//end of catch (SQLException sqlExp)
		  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		  			{
		  				try
		  				{
		  					if (statement != null) statement.close();
		  				}//end of try
		  				catch (SQLException sqlExp)
		  				{
		  					log.error("Error while closing the Statement in AsynchronusAction validateMember()",sqlExp);
		  					throw new TTKException(sqlExp, "async");
		  				}//end of catch (SQLException sqlExp)
		  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		  				{
		  					try
		  					{
		  						if(con != null) con.close();
		  					}//end of try
		  					catch (SQLException sqlExp)
		  					{
		  						log.error("Error while closing the Connection in AsynchronusAction validateMember()",sqlExp);
		  						throw new TTKException(sqlExp, "async");
		  					}//end of catch (SQLException sqlExp)
		  				}//end of finally Connection Close
		  			}//end of finally Statement Close
		  		}//end of try
		  		catch (TTKException exp)
		  		{
		  			throw new TTKException(exp, "async");
		  		}//end of catch (TTKException exp)
		  		finally // Control will reach here in anycase set null to the objects
		  		{
		  			resultSet = null;
		  			statement = null;
		  			con = null;
			if(writer!=null)writer.close();
		  		}//end of finally
		  	}//end of finally	
        return null;
    }
	
	public ActionForward getClinicianNames(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet rs=null;
		try{
		con=ResourceManager.getConnection();
		statement=con.prepareStatement("SELECT CONTACT_NAME ||' ('||PROFESSIONAL_ID||')' AS CONTACT_NAME FROM TPA_HOSP_PROFESSIONALS");		
	 
	      rs=statement.executeQuery();
	      while(rs.next())  writer.println(rs.getString(1));	    	  
	     
	      writer.flush();	    
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);
			}
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
	  				log.error("Error while closing the Resultset in AsynchronusAction getClinicianNames()",sqlExp);
	  				throw new TTKException(sqlExp, "async");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
	  			{
	  				try
	  				{
	  					if (statement != null) statement.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Statement in AsynchronusAction getClinicianNames()",sqlExp);
	  					throw new TTKException(sqlExp, "async");
	  				}//end of catch (SQLException sqlExp)
	  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  				{
	  					try
	  					{
	  						if(con != null) con.close();
	  					}//end of try
	  					catch (SQLException sqlExp)
	  					{
	  						log.error("Error while closing the Connection in AsynchronusAction getClinicianNames()",sqlExp);
	  						throw new TTKException(sqlExp, "async");
	  					}//end of catch (SQLException sqlExp)
	  				}//end of finally Connection Close
	  			}//end of finally Statement Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "async");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			rs = null;
	  			statement = null;
	  			con = null;
			if(writer!=null)writer.close();
	  		}//end of finally
	  	}//end of finally	
        return null;
    }
	
	
	
	public ActionForward getStates(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet resultSet=null;
						
	    String countryId=(String)request.getParameter("countryId");
	    countryId=(countryId==null)?"0":countryId.trim();
	    String options="";
	    String query="select state_type_id,state_name from TPA_STATE_CODE where country_id="+countryId+" ORDER BY state_name";
	   try{
	    con=ResourceManager.getConnection();
		statement=con.prepareStatement(query);
		  resultSet=statement.executeQuery();
		    HashMap<String,String> providerStates=new  HashMap<String,String>();
	       while(resultSet.next()){
	    	   String id=resultSet.getString(1);
	    	   String desc=resultSet.getString(2);
	    	   providerStates.put(id, desc);
	    	   options+=id+"@"+desc+"&";
	       }
	       request.getSession().setAttribute("providerStates", providerStates);
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);
			}
	   finally
	  	{
	  		/* Nested Try Catch to ensure resource closure */
	  		try // First try closing the result set
	  		{
	  			try
	  			{
	  				if (resultSet != null) resultSet.close();
	  			}//end of try
	  			catch (SQLException sqlExp)
	  			{
	  				log.error("Error while closing the Resultset in AsynchronusAction getStates()",sqlExp);
	  				throw new TTKException(sqlExp, "async");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
	  			{
	  				try
	  				{
	  					if (statement != null) statement.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Statement in AsynchronusAction getStates()",sqlExp);
	  					throw new TTKException(sqlExp, "async");
	  				}//end of catch (SQLException sqlExp)
	  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  				{
	  					try
	  					{
	  						if(con != null) con.close();
	  					}//end of try
	  					catch (SQLException sqlExp)
	  					{
	  						log.error("Error while closing the Connection in AsynchronusAction getStates()",sqlExp);
	  						throw new TTKException(sqlExp, "async");
	  					}//end of catch (SQLException sqlExp)
	  				}//end of finally Connection Close
	  			}//end of finally Statement Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "async");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			resultSet = null;
	  			statement = null;
	  			con = null;
	  		}//end of finally
	  	}//end of finally
	  
	   
      writer.print(options);
      writer.flush();
      if(writer!=null)writer.close();
        return null;
	}
	public ActionForward getAreas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet resultSet=null;
						
	    String stateId=(String)request.getParameter("stateId");
	    stateId=(stateId==null)?"0":stateId.trim();
	    String options="";
    	String query="SELECT CITY_TYPE_ID,CITY_DESCRIPTION FROM TPA_CITY_CODE WHERE STATE_TYPE_ID='"+stateId+"' ORDER BY CITY_DESCRIPTION";
	   try{
	    con=ResourceManager.getConnection();
		statement=con.prepareStatement(query);
		  resultSet=statement.executeQuery();
		    HashMap<String,String> providerAreas=new  HashMap<String,String>();
	       while(resultSet.next()){
	    	   String id=resultSet.getString(1);
	    	   String desc=resultSet.getString(2);
	    	   providerAreas.put(id, desc);
	    	   options+=id+"@"+desc+"&";
	       }
	       request.getSession().setAttribute("providerAreas", providerAreas);
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);
			}
	   finally
	  	{
	  		/* Nested Try Catch to ensure resource closure */
	  		try // First try closing the result set
	  		{
	  			try
	  			{
	  				if (resultSet != null) resultSet.close();
	  			}//end of try
	  			catch (SQLException sqlExp)
	  			{
	  				log.error("Error while closing the Resultset in AsynchronusAction getAreas()",sqlExp);
	  				throw new TTKException(sqlExp, "async");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
	  			{
	  				try
	  				{
	  					if (statement != null) statement.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Statement in AsynchronusAction getAreas()",sqlExp);
	  					throw new TTKException(sqlExp, "async");
	  				}//end of catch (SQLException sqlExp)
	  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  				{
	  					try
	  					{
	  						if(con != null) con.close();
	  					}//end of try
	  					catch (SQLException sqlExp)
	  					{
	  						log.error("Error while closing the Connection in AsynchronusAction getMemberDetails()",sqlExp);
	  						throw new TTKException(sqlExp, "async");
	  					}//end of catch (SQLException sqlExp)
	  				}//end of finally Connection Close
	  			}//end of finally Statement Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "async");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			resultSet = null;
	  			statement = null;
	  			con = null;
	  		}//end of finally
	  	}//end of finally
	  
      writer.print(options);
      writer.flush();
      if(writer!=null)writer.close();
        return null;
	}

public ActionForward getIsdOrStd(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
  PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet rs=null;
	try{
		String iors=request.getParameter("iors");
		String query;
		if("ISD".equals(iors)) query="select ISD_CODE from TPA_COUNTRY_CODE where country_id="+request.getParameter("countryId");
		else query="select STD_CODE from TPA_STATE_CODE where STATE_TYPE_ID='"+request.getParameter("stateId")+"'";
	con=ResourceManager.getConnection();
	statement=con.prepareStatement(query);		
      rs=statement.executeQuery();
      if(rs.next())writer.print(rs.getInt(1));      
      else writer.print("");
      writer.flush();	    
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		}
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
  				log.error("Error while closing the Resultset in AsynchronusAction getIsdOrStd()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getIsdOrStd()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getIsdOrStd()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			rs = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally	
    return null;
}

public ActionForward getIcdDescription(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;					
   
   try{
    con=ResourceManager.getConnection();
     String query;
     String id=request.getParameter("id");
     if("desc".equals(id))query="select icd_desc from test_icd";
     else{
    	 String icd_desc=request.getParameter("icd_desc");     
        query="select icd_id from test_icd where icd_desc='"+icd_desc+"'";
     }
       
	statement=con.prepareStatement(query);
	resultSet=statement.executeQuery();
	  
       while(resultSet.next()) writer.println(resultSet.getString(1));
       writer.flush();
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		}
   finally
 	{
 		/* Nested Try Catch to ensure resource closure */
 		try // First try closing the result set
 		{
 			try
 			{
 				if (resultSet != null) resultSet.close();
 			}//end of try
 			catch (SQLException sqlExp)
 			{
 				log.error("Error while closing the Resultset in AsynchronusAction getIcdDescription()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (statement != null) statement.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getMemberDetails()",sqlExp);
 					throw new TTKException(sqlExp, "async");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 				{
 					try
 					{
 						if(con != null) con.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Connection in AsynchronusAction getMemberDetails()",sqlExp);
 						throw new TTKException(sqlExp, "async");
 					}//end of catch (SQLException sqlExp)
 				}//end of finally Connection Close
 			}//end of finally Statement Close
 		}//end of try
 		catch (TTKException exp)
 		{
 			throw new TTKException(exp, "async");
 		}//end of catch (TTKException exp)
 		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			statement = null;
 			con = null;
		if(writer!=null)writer.close();
 		}//end of finally
 	}//end of finally
  
    return null;
}
public ActionForward getStateNames(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;					
   
   try{
    con=ResourceManager.getConnection();
     String query;
     String id=request.getParameter("id");
     if("stateId".equals(id)){
    	 String stateCode=request.getParameter("stateCode");
    	 query="Select STATE_TYPE_ID,STATE_NAME from TPA_STATE_CODE where STATE_TYPE_ID='"+stateCode+"'";
     }
     else{
    	 String cityCode=request.getParameter("cityCode");
    	 query="select CITY_TYPE_ID,CITY_DESCRIPTION from TPA_CITY_CODE where CITY_TYPE_ID='"+cityCode+"'"; 
     }
       
	statement=con.prepareStatement(query);
	resultSet=statement.executeQuery();
	  
       while(resultSet.next()) writer.println(resultSet.getString(1)+"@"+resultSet.getString(2));       
       writer.flush();
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		}
   finally
 	{
 		/* Nested Try Catch to ensure resource closure */
 		try // First try closing the result set
 		{
 			try
 			{
 				if (resultSet != null) resultSet.close();
 			}//end of try
 			catch (SQLException sqlExp)
 			{
 				log.error("Error while closing the Resultset in AsynchronusAction getStateNames()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (statement != null) statement.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getStateNames()",sqlExp);
 					throw new TTKException(sqlExp, "async");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 				{
 					try
 					{
 						if(con != null) con.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Connection in AsynchronusAction getStateNames()",sqlExp);
 						throw new TTKException(sqlExp, "async");
 					}//end of catch (SQLException sqlExp)
 				}//end of finally Connection Close
 			}//end of finally Statement Close
 		}//end of try
 		catch (TTKException exp)
 		{
 			throw new TTKException(exp, "async");
 		}//end of catch (TTKException exp)
 		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			statement = null;
 			con = null;
		if(writer!=null)writer.close();
 		}//end of finally
 	}//end of finally
  
    return null;
}
public ActionForward getProductNetworkType(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;					
   
   try{
    con=ResourceManager.getConnection();     
     String productSeqID=request.getParameter("productSeqID"); 	 
     String query="select gc.description from app.tpa_ins_product ip join app.tpa_general_code gc on (ip.product_cat_type_id=gc.general_type_id) where ip.product_seq_id="+productSeqID+"";
    
	statement=con.prepareStatement(query);
	resultSet=statement.executeQuery();
	  
       while(resultSet.next()) writer.println(resultSet.getString(1));       
       writer.flush();
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		}
   finally
 	{
 		/* Nested Try Catch to ensure resource closure */
 		try // First try closing the result set
 		{
 			try
 			{
 				if (resultSet != null) resultSet.close();
 			}//end of try
 			catch (SQLException sqlExp)
 			{
 				log.error("Error while closing the Resultset in AsynchronusAction getProductNetworkType()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (statement != null) statement.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getProductNetworkType()",sqlExp);
 					throw new TTKException(sqlExp, "async");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 				{
 					try
 					{
 						if(con != null) con.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Connection in AsynchronusAction getProductNetworkType()",sqlExp);
 						throw new TTKException(sqlExp, "async");
 					}//end of catch (SQLException sqlExp)
 				}//end of finally Connection Close
 			}//end of finally Statement Close
 		}//end of try
 		catch (TTKException exp)
 		{
 			throw new TTKException(exp, "async");
 		}//end of catch (TTKException exp)
 		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			statement = null;
 			con = null;
		if(writer!=null)writer.close();
 		}//end of finally
 	}//end of finally
  
    return null;
}

public ActionForward getMemberDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	CallableStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String memberId=request.getParameter("memberId");
	     memberId=(memberId==null)?"":memberId.trim();     
	     statement=con.prepareCall("{CALL AUTHORIZATION_PKG.SELECT_MEMBER(?,?)}");    
	     statement.setString(1,memberId);
	     statement.registerOutParameter(2,OracleTypes.CURSOR);
		statement.execute();
	   resultSet=(ResultSet)statement.getObject(2);	
		if(resultSet==null||!resultSet.next()||resultSet.getLong("MEMBER_SEQ_ID")==0){		
			writer.write("MNE@");//Member Not Exist		 
		}else{
			String memberSeqId=(resultSet.getLong("MEMBER_SEQ_ID")==0)?"":resultSet.getLong("MEMBER_SEQ_ID")+"";
			String memberName=(resultSet.getString("MEM_NAME")==null)?"":resultSet.getString("MEM_NAME");
			String memberAge=(resultSet.getString("MEM_AGE")==null)?"":resultSet.getString("MEM_AGE");
			String emirateId=(resultSet.getString("EMIRATE_ID")==null)?"":resultSet.getString("EMIRATE_ID");
			String payerId=(resultSet.getString("PAYER_ID")==null)?"":resultSet.getString("PAYER_ID");
			String insSeqId=(resultSet.getLong("INS_SEQ_ID")==0)?"":resultSet.getLong("INS_SEQ_ID")+"";
			String insCompName=(resultSet.getString("INS_COMP_NAME")==null)?"":resultSet.getString("INS_COMP_NAME");
			String policySeqId=(resultSet.getLong("POLICY_SEQ_ID")==0)?"":resultSet.getLong("POLICY_SEQ_ID")+"";
			String patientGender=(resultSet.getString("GENDER")==null)?"":resultSet.getString("GENDER");
			String policyNumber=(resultSet.getString("POLICY_NUMBER")==null)?"":resultSet.getString("POLICY_NUMBER");
			String corporateName=(resultSet.getString("CORPORATE_NAME")==null)?"":resultSet.getString("CORPORATE_NAME");
			String policyStartDate=TTKCommon.convertDateAsString("dd/MM/yyyy",resultSet.getDate("POLICY_START_DATE"));
			String policyEndtDate=TTKCommon.convertDateAsString("dd/MM/yyyy",resultSet.getDate("POLICY_END_DATE"));
			String nationality=(resultSet.getString("NATIONALITY")==null)?"":resultSet.getString("NATIONALITY");
			String sumInsured=(resultSet.getString("SUM_INSURED")==null)?"":resultSet.getString("SUM_INSURED");
			String availableSumInsured=(resultSet.getString("AVA_SUM_INSURED")==null)?"":resultSet.getString("AVA_SUM_INSURED");
			String vipyn=(resultSet.getString("VIP_YN")==null)?"":resultSet.getString("VIP_YN");
            String incepDate=(resultSet.getString("CLM_MEM_INSP_DATE")==null)?"":resultSet.getString("CLM_MEM_INSP_DATE");			
			writer.write("SUCC@"+memberSeqId+"@"+memberName+"@"+memberAge+"@"+emirateId+"@"+payerId+"@"+insSeqId+"@"+insCompName+"@"+policySeqId+"@"+patientGender+"@"+policyNumber+"@"+corporateName+"@"+policyStartDate+"@"+policyEndtDate+"@"+nationality+"@"+sumInsured+"@"+availableSumInsured+"@"+vipyn+"@"+incepDate);
		writer.flush();		 
	}		
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occured
        }
   finally
	{
		/* Nested Try Catch to ensure resource closure */
		try // First try closing the result set
		{
			try
			{
				if (resultSet != null) resultSet.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in AsynchronusAction getMemberDetails()",sqlExp);
				throw new TTKException(sqlExp, "async");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (statement != null) statement.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in AsynchronusAction getMemberDetails()",sqlExp);
					throw new TTKException(sqlExp, "async");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(con != null) con.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in AsynchronusAction getMemberDetails()",sqlExp);
						throw new TTKException(sqlExp, "async");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "async");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			statement = null;
 			con = null;
		if(writer!=null)writer.close();
 		}//end of finally
	}//end of finally
    return null;
}
public ActionForward getMemberDetailsProviderLogin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	CallableStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String memberId=request.getParameter("memberId");
	     memberId=(memberId==null)?"":memberId.trim();     
	     statement=con.prepareCall("{CALL AUTHORIZATION_PKG.SELECT_MEMBER(?,?)}");    
	     statement.setString(1,memberId);
	     statement.registerOutParameter(2,OracleTypes.CURSOR);
		statement.execute();
	   resultSet=(ResultSet)statement.getObject(2);	
		if(resultSet.next()){		
			writer.write("SUCC");
			writer.flush();		 
		}
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occured
        }
   finally
 	{
 		/* Nested Try Catch to ensure resource closure */
 		try // First try closing the result set
 		{
 			try
 			{
 				if (resultSet != null) resultSet.close();
 			}//end of try
 			catch (SQLException sqlExp)
 			{
 				log.error("Error while closing the Resultset in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (statement != null) statement.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 					throw new TTKException(sqlExp, "async");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 				{
 					try
 					{
 						if(con != null) con.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Connection in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 						throw new TTKException(sqlExp, "async");
 					}//end of catch (SQLException sqlExp)
 				}//end of finally Connection Close
 			}//end of finally Statement Close
 		}//end of try
 		catch (TTKException exp)
 		{
 			throw new TTKException(exp, "async");
 		}//end of catch (TTKException exp)
 		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			statement = null;
 			con = null;
		if(writer!=null)writer.close();
 		}//end of finally
 	}//end of finally
    return null;
}

public ActionForward getProviderDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String providerId=request.getParameter("providerId");
	     providerId=(providerId==null)?"":providerId.trim();     
	     statement=con.prepareStatement("SELECT I.HOSP_SEQ_ID,I.HOSP_LICENC_NUMB,I.HOSP_NAME FROM APP.TPA_HOSP_INFO I JOIN APP.TPA_HOSP_EMPANEL_STATUS ES ON (I.HOSP_SEQ_ID=ES.HOSP_SEQ_ID AND ES.EMPANEL_STATUS_TYPE_ID='EMP') WHERE I.HOSP_LICENC_NUMB='"+providerId+"'");    
	   resultSet=statement.executeQuery();	
		if(resultSet==null||!resultSet.next()){		
			writer.write("PNE@");//Provider Not Exist		 
		}else{
			String providerSeqId=(resultSet.getLong(1)==0)?"":resultSet.getLong(1)+"";	
			String providerNum=(resultSet.getString(2)==null)?"":resultSet.getString(2);
			String providerName=(resultSet.getString(3)==null)?"":resultSet.getString(3);
			
		writer.write("SUCC@"+providerSeqId+"@"+providerNum+"@"+providerName);
		writer.flush();		 
	}		
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occured
		}
   finally
 	{
 		/* Nested Try Catch to ensure resource closure */
 		try // First try closing the result set
 		{
 			try
 			{
 				if (resultSet != null) resultSet.close();
 			}//end of try
 			catch (SQLException sqlExp)
 			{
 				log.error("Error while closing the Resultset in AsynchronusAction getProviderDetails()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (statement != null) statement.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getProviderDetails()",sqlExp);
 					throw new TTKException(sqlExp, "async");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 				{
 					try
 					{
 						if(con != null) con.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Connection in AsynchronusAction getProviderDetails()",sqlExp);
 						throw new TTKException(sqlExp, "async");
 					}//end of catch (SQLException sqlExp)
 				}//end of finally Connection Close
 			}//end of finally Statement Close
 		}//end of try
 		catch (TTKException exp)
 		{
 			throw new TTKException(exp, "async");
 		}//end of catch (TTKException exp)
 		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			statement = null;
 			con = null;
		if(writer!=null)writer.close();
 		}//end of finally
 	}//end of finally
    return null;
}
public ActionForward getProviderDetails2(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String providerName=request.getParameter("providerName");
	     providerName=(providerName==null)?"":providerName.trim();     
	     statement=con.prepareStatement("SELECT HOSP_NAME||' ('||HOSP_LICENC_NUMB||')',HOSP_SEQ_ID,HOSP_LICENC_NUMB FROM TPA_HOSP_INFO WHERE HOSP_LICENC_NUMB = REGEXP_SUBSTR('"+providerName+"', '[^/()]+', 1, 2)");    
		
	   resultSet=statement.executeQuery();	
		if(resultSet==null||!resultSet.next()){		
			writer.write("PNE@");//Provider Not Exist		 
		}else{
			String providerName2=(resultSet.getString(1)==null)?"":resultSet.getString(1);
			String providerSeqId=(resultSet.getLong(2)==0)?"":resultSet.getLong(2)+"";			
			String providerNum=(resultSet.getString(3)==null)?"":resultSet.getString(3);
		writer.write("SUCC@"+providerSeqId+"@"+providerName2+"@"+providerNum);
		writer.flush();		 
	}		
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occured
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet != null) resultSet.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getProviderDetails2()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getProviderDetails2()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getProviderDetails2()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally
    return null;
}
public ActionForward getClinicianDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String clinicianId=request.getParameter("clinicianId");
	     clinicianId=(clinicianId==null)?"":clinicianId.trim();     
	     statement=con.prepareStatement("SELECT P.CONTACT_SEQ_ID,P.PROFESSIONAL_ID,P.CONTACT_NAME FROM APP.TPA_HOSP_PROFESSIONALS P WHERE P.PROFESSIONAL_ID='"+clinicianId+"'");    
		
	   resultSet=statement.executeQuery();	
		if(resultSet==null||!resultSet.next()){		
			writer.write("CNE@");//Clinician Not Exist		 
		}else{
			String clinicianName=(resultSet.getString(3)==null)?"":resultSet.getString(3);
		writer.write("SUCC@"+clinicianName);
		writer.flush();		 
	}		
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occured
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet != null) resultSet.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getClinicianDetails()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getClinicianDetails()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getClinicianDetails()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally 
    return null;
}
public ActionForward getClinicianDetails2(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String clinicianName=request.getParameter("clinicianName");
	     clinicianName=(clinicianName==null)?"":clinicianName.trim();     
	     statement=con.prepareStatement("SELECT CONTACT_NAME ||' ('||PROFESSIONAL_ID||')' AS CONTACT_NAME,THP.PROFESSIONAL_ID FROM TPA_HOSP_PROFESSIONALS THP WHERE THP.PROFESSIONAL_ID=REGEXP_SUBSTR('"+clinicianName+"', '[^/()]+', 1, 2)");    
		
	   resultSet=statement.executeQuery();	
		if(resultSet==null||!resultSet.next()){		
			writer.write("CNE@");//Clinician Not Exist		 
		}else{
			String clinicianName2=(resultSet.getString(1)==null)?"":resultSet.getString(1);
			String clinicianId=(resultSet.getString(2)==null)?"":resultSet.getString(2);
		writer.write("SUCC@"+clinicianName2+"@"+clinicianId);
		writer.flush();		 
	}		
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occured
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet != null) resultSet.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getClinicianDetails2()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getClinicianDetails2()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getClinicianDetails2()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally
    return null;
}
public ActionForward getIcdCodeDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	CallableStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String icdCode=request.getParameter("icdCode");
	     String seqId=request.getParameter("seqID");
	     String authType=request.getParameter("authType");
	     icdCode=(icdCode==null)?"":icdCode.trim();
	     seqId=(seqId==null || seqId =="" )?"0":seqId.trim();
	     authType=(authType==null)?"":authType.trim();
	     statement=con.prepareCall("{CALL AUTHORIZATION_PKG.SELECT_ICD(?,?,?,?)}");    
	     statement.setString(1,icdCode);
	     statement.setLong(2,new Long(seqId));
	     statement.setString(3,authType);
	     statement.registerOutParameter(4,OracleTypes.CURSOR);
		statement.execute();
	   resultSet=(ResultSet)statement.getObject(4);	
		if(resultSet==null||!resultSet.next()||resultSet.getLong("ICD_CODE_SEQ_ID")==0){		
			writer.write("INE@");//ICD Code Not Exist		 
		}else{
			String icdCodeSeqId=(resultSet.getLong("ICD_CODE_SEQ_ID")==0)?"":resultSet.getLong("ICD_CODE_SEQ_ID")+"";
			String icdDescription=(resultSet.getString("ICD_DESCRIPTION")==null)?"":resultSet.getString("ICD_DESCRIPTION");
			String primary=(resultSet.getString("PRIMARY")==null)?"":resultSet.getString("PRIMARY");
			Integer mat_icd_yn = resultSet.getInt("mat_icd_yn");
			request.getSession().setAttribute("mat_icd_yn", mat_icd_yn);
			request.getSession().setAttribute("primary", primary);
		writer.write("SUCC@"+icdCodeSeqId+"@"+icdDescription+"@"+primary+"@"+mat_icd_yn);
		writer.flush();		 
	}		
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occured
		writer.flush();
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet != null) resultSet.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getMemberDetails()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getIcdCodeDetails()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getIcdCodeDetails()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally
    return null;
}
public ActionForward getActivityCodeDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	CallableStatement statement=null;
	ResultSet resultSet=null;
	ResultSet tarrifrs=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String preAuthSeqID=request.getParameter("preAuthSeqID");
	     String activityCode=request.getParameter("activityCode");
	     String activityStartDate=request.getParameter("activityStartDate");
	     activityCode=(activityCode==null)?"":activityCode.trim();
	     preAuthSeqID=(preAuthSeqID==null)?"":preAuthSeqID.trim();
	     activityStartDate=(activityStartDate==null)?"":activityStartDate.trim();
	     statement=con.prepareCall("{CALL AUTHORIZATION_PKG.SELECT_ACTIVITY_CODE(?,?,?,?,?)}");    
	     statement.setString(1,preAuthSeqID);
	     statement.setString(2,activityCode);	    
	     statement.setDate(3,new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(activityStartDate).getTime()));
	     statement.registerOutParameter(4,OracleTypes.CURSOR);
	     statement.registerOutParameter(5,OracleTypes.CURSOR);
		statement.execute();
	   resultSet=(ResultSet)statement.getObject(4);	
		if(resultSet==null||!resultSet.next()||resultSet.getLong("ACTIVITY_SEQ_ID")==0){		
			writer.write("AVCNE@");//Activity Code Not Exist		 
		}else{
			String activityCodeSeqId=(resultSet.getLong("ACTIVITY_SEQ_ID")==0)?"":resultSet.getLong("ACTIVITY_SEQ_ID")+"";
			String activityCodeDescription=(resultSet.getString("ACTIVITY_DESCRIPTION")==null)?"":resultSet.getString("ACTIVITY_DESCRIPTION");
			String activityTypeId=(resultSet.getString("ACTIVITY_TYPE_ID")==null)?"":resultSet.getString("ACTIVITY_TYPE_ID");
			tarrifrs=(ResultSet)statement.getObject(5);	
			String grossAmt="";
			String discountAmt="";
			String discountGrossAmt="";
			String bindleId="";
			String packageId="";
			String internalCode="";
			if(tarrifrs!=null&&tarrifrs.next()){
				grossAmt=tarrifrs.getBigDecimal("GROSS_AMOUNT")==null?"":tarrifrs.getBigDecimal("GROSS_AMOUNT")+"";
				discountAmt=tarrifrs.getBigDecimal("DISC_AMOUNT")==null?"":tarrifrs.getBigDecimal("DISC_AMOUNT")+"";
				discountGrossAmt=tarrifrs.getBigDecimal("DISC_GROSS_AMOUNT")==null?"":tarrifrs.getBigDecimal("DISC_GROSS_AMOUNT")+"";
				bindleId=tarrifrs.getString("BUNDLE_ID")==null?"":tarrifrs.getString("BUNDLE_ID");
				packageId=tarrifrs.getString("PACKAGE_ID")==null?"":tarrifrs.getString("PACKAGE_ID");
				internalCode=tarrifrs.getString("INTERNAL_CODE")==null?"":tarrifrs.getString("INTERNAL_CODE");
			}
		writer.write("SUCC@"+activityCodeSeqId+"@"+activityCodeDescription+"@"+activityTypeId+"@"+grossAmt+"@"+discountAmt+"@"+discountGrossAmt+"@"+bindleId+"@"+packageId+"@"+internalCode);
		writer.flush();		 
	}		
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occurred
		writer.flush();
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet != null) resultSet.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getActivityCodeDetails()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getActivityCodeDetails()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getActivityCodeDetails()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
	finally{
  						try{
  							if(tarrifrs!=null) tarrifrs.close();
  						}catch(SQLException sqlExp)
  						{
  							log.error("Error while closing the Statement in AsynchronusAction getActivityCodeDetails()",sqlExp);
  		  					throw new TTKException(sqlExp, "async");
  						}
  					}
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet = null;
  			tarrifrs = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally
    return null;
}

public ActionForward getDiagnosisDesc(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	log.debug("Inside AsynchronusAction getDiagnosisDesc ");
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     statement=con.prepareStatement("SELECT TRIM(LONG_DESC)||'('||TRIM(ICD_CODE)||')' AS ICD_DESCRIPTION FROM TPA_ICD10_MASTER_DETAILS");
	     resultSet=(ResultSet)statement.executeQuery();
	     
	     while(resultSet.next()){
	    	 writer.println(resultSet.getString(1));      
	     }
	     writer.flush();	
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.flush();
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet != null) resultSet.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getDiagnosisDesc()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getDiagnosisDesc()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getDiagnosisDesc()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally
    return null;
}


public ActionForward  getObservTypeDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement1=null,statement2=null;
	ResultSet resultSet1=null,resultSet2=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String observType=request.getParameter("observType");
	     observType=(observType==null)?"":observType.trim();
	     statement1=con.prepareStatement("SELECT S.OBSERVATION_CODE_ID,S.OBSERVATION_CODE FROM APP.TPA_OBSERVATION_VALUE_CODES S where s.observation_type_id='"+observType+"'");
	     resultSet1=(ResultSet)statement1.executeQuery();
	     String result="";
	     while(resultSet1.next()){
	    	 String codeId=(resultSet1.getString(1)==null)?"":resultSet1.getString(1);
	    	 String codeDesc=(resultSet1.getString(2)==null)?"":resultSet1.getString(2);	    	 
	    	 result+=codeId+"@"+codeDesc+"#";       
	     }
	     result+="|";
	     statement2=con.prepareStatement("select C.OBS_VALUE_TYPE_CODE_ID,C.VALUE_TYPE from app.TPA_OBSER_VALUE_TYPE_CODES C WHERE C.OBSERVATION_TYPE_ID='"+observType+"'");
	     resultSet2=(ResultSet)statement2.executeQuery();
	   
	     while(resultSet2.next()){	    	
	    	 String valueTypeId=(resultSet2.getString(1)==null)?"":resultSet2.getString(1);
	    	 String valueTypeDesc=(resultSet2.getString(2)==null)?"":resultSet2.getString(2);
	    	 result+=valueTypeId+"@"+valueTypeDesc+"#";      
	     }
	     writer.println(result);
	     writer.flush();	
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.flush();
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet1 != null) resultSet1.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getObservTypeDetails()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement1 != null) statement1.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getObservTypeDetails()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getObservTypeDetails()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet1 = null;
  			statement1 = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally 
    return null;
}

public ActionForward getIcdCodes(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	log.debug("Inside AsynchronusAction getIcdCodes ");
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     statement=con.prepareStatement("SELECT ICD_CODE FROM TPA_ICD10_MASTER_DETAILS");
	     resultSet=(ResultSet)statement.executeQuery();
	     
	     while(resultSet.next()){
	    	 writer.println(resultSet.getString(1));      
	     }
	     writer.flush();	
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.flush();
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet != null) resultSet.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getIcdCodes()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getIcdCodes()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getIcdCodes()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally
    return null;
}
public ActionForward getUnitTypePrice(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	log.debug("Inside AsynchronusAction getUnitTypePrice ");
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	CallableStatement statement=null;
   try{	   
	    String activityCode=request.getParameter("activityCode");
	    activityCode=activityCode==null?"":activityCode.trim();
	    String activityStartDate=request.getParameter("activityStartDate");
	    activityStartDate=activityStartDate==null?"":activityStartDate.trim();
	    String unitType=request.getParameter("unitType");
	    String providerSeqID=request.getParameter("providerSeqID");
	    String internalCode=request.getParameter("internalCode");
	    
	    unitType=unitType==null?"":unitType.trim();
	    con=ResourceManager.getConnection();     
		     statement=con.prepareCall("{CALL AUTHORIZATION_PKG.SELECT_DDC_PRICE(?,?,?,?,?,?,?)}");    
		     statement.setString(1,activityCode);
		     statement.setDate(2,new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(activityStartDate).getTime()));
		     statement.setString(3,unitType);
		     statement.setString(4,providerSeqID);
		     statement.setString(5,internalCode);
		     statement.registerOutParameter(6,OracleTypes.VARCHAR);
		     statement.registerOutParameter(7,OracleTypes.VARCHAR);
			statement.execute();
		   String price=statement.getString(6);
		   String discount=statement.getString(7);
		   price=(price==null||"".equals(price))?"0":price;
		   discount=(discount==null||"".equals(discount))?"0":discount;
	     writer.print(price+"@"+discount);      
	     writer.flush();	
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		 writer.print("0@0");  
		writer.flush();
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (statement != null) statement.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getUnitTypePrice()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getUnitTypePrice()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			statement = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally 
    return null;
}

public ActionForward getCurencyType(ActionMapping mapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, TTKException
{
	String countryID=request.getParameter("countryId");
	PrintWriter pw=response.getWriter();
	Connection connection=null;
	ResultSet resultSet=null;
	PreparedStatement preStatement=null;
	try {
		connection=ResourceManager.getConnection();
		preStatement=connection.prepareStatement("select jk.currency_id from app.tpa_country_code hj join app.tpa_currency_code jk on (hj.country_id=jk.country_id) where hj.country_id=?");
		preStatement.setString(1,countryID);
		resultSet=preStatement.executeQuery();
		if(resultSet.next())
		{
			pw.print(resultSet.getString("currency_id"));
			//  
		}
		pw.flush();
	} 
	catch (TTKException | SQLException e)
	{
		e.printStackTrace();
	} 
	finally
	{
 		/* Nested Try Catch to ensure resource closure */
 		try // First try closing the result set
 		{
 			try
 			{
 				if (resultSet != null) resultSet.close();
 			}//end of try
 			catch (SQLException sqlExp)
 			{
 				log.error("Error while closing the Resultset in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (preStatement != null) preStatement.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 					throw new TTKException(sqlExp, "async");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 				{
 					try
 					{
 						if(connection != null) connection.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Connection in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 						throw new TTKException(sqlExp, "async");
 					}//end of catch (SQLException sqlExp)
 				}//end of finally Connection Close
 			}//end of finally Statement Close
 		}//end of try
 		catch (TTKException exp)
 		{
 			throw new TTKException(exp, "async");
 		}//end of catch (TTKException exp)
 		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			preStatement = null;
 			connection = null;
		if(pw!=null)pw.close();
 		}//end of finally
 	}
	return null;
	
}


public ActionForward getTotalLivesPricing(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	CallableStatement statement=null;
	ResultSet resultSet=null;
   try{	 
	   con=ResourceManager.getConnection();     
	     String flag=request.getParameter("flag");
	     String policy_no=request.getParameter("policy_no");
	     String tot_lives=request.getParameter("tot_lives");
	     String grp_prof_seq_id=request.getParameter("grp_prof_seq_id");
	     //  
	    //   
	    //   
	    //   

	     
	        
	     statement=con.prepareCall("{CALL POLICY_COPY_ISSUE_AUTOMATION.GET_LIVES_ALRT_MSG(?,?,?,?,?)}");    
	     //  
	     statement.setString(1,flag);
	     statement.setString(2,policy_no);
	     statement.setString(3,tot_lives);
	     statement.setString(4,grp_prof_seq_id);
	     statement.registerOutParameter(5,OracleTypes.VARCHAR);
		statement.execute();
		String alertmessage=statement.getString(5);
	 //  resultSet=(ResultSet)statement.getObject(5);	
	 //    
		if(alertmessage != null){		
			writer.write(alertmessage);
			 //   
			writer.flush();		 
			}
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.write("EO@");//Exception Occured
        }
   finally
 	{
 		/* Nested Try Catch to ensure resource closure */
 		try // First try closing the result set
 		{
 			try
 			{
 				if (resultSet != null) resultSet.close();
 			}//end of try
 			catch (SQLException sqlExp)
 			{
 				log.error("Error while closing the Resultset in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (statement != null) statement.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 					throw new TTKException(sqlExp, "async");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 				{
 					try
 					{
 						if(con != null) con.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Connection in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 						throw new TTKException(sqlExp, "async");
 					}//end of catch (SQLException sqlExp)
 				}//end of finally Connection Close
 			}//end of finally Statement Close
 		}//end of try
 		catch (TTKException exp)
 		{
 			throw new TTKException(exp, "async");
 		}//end of catch (TTKException exp)
 		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			statement = null;
 			con = null;
		if(writer!=null)writer.close();
 		}//end of finally
 	}//end of finally
    return null;
}

public ActionForward getActualDiscountAmt(ActionMapping mapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, ParseException, TTKException
{
	String activityCode=request.getParameter("activityCode");
	String claimSeqID=request.getParameter("claimSeqID");
	String activityStartDate=request.getParameter("activityStartDate");
	String authType=request.getParameter("authType");
	PrintWriter pw=response.getWriter();
	Connection connection=null;
	ResultSet resultSet=null;
	CallableStatement cStmtObject=null;
	BigDecimal discountPerUnit = null;
	try {
		connection=ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)connection.prepareCall("{CALL AUTHORIZATION_PKG.SELECT_ACTIVITY_CODE(?,?,?,?,?,?,?)}");
		cStmtObject.setString(1,claimSeqID);
		cStmtObject.setString(2,authType);
		cStmtObject.setString(3,activityCode);
		cStmtObject.setString(4,"SEARCHOUT");
		cStmtObject.setDate(5,new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(activityStartDate).getTime()));
		cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
		cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
		cStmtObject.execute();
		resultSet=(ResultSet)cStmtObject.getObject(7);
		if(resultSet.next())
		{
			if(resultSet.getBigDecimal("UNIT_DISCOUNT")!=null){
				discountPerUnit=resultSet.getBigDecimal("UNIT_DISCOUNT");
			}
		}
		pw.print(discountPerUnit);
		pw.flush();
	} 
	catch (TTKException | SQLException e)
	{
		e.printStackTrace();
	} 
	finally
	{
 		/* Nested Try Catch to ensure resource closure */
 		try // First try closing the result set
 		{
 			try
 			{
 				if (resultSet != null) resultSet.close();
 			}//end of try
 			catch (SQLException sqlExp)
 			{
 				log.error("Error while closing the Resultset in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (cStmtObject != null) cStmtObject.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 					throw new TTKException(sqlExp, "async");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 				{
 					try
 					{
 						if(connection != null) connection.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Connection in AsynchronusAction getMemberDetailsProviderLogin()",sqlExp);
 						throw new TTKException(sqlExp, "async");
 					}//end of catch (SQLException sqlExp)
 				}//end of finally Connection Close
 			}//end of finally Statement Close
 		}//end of try
 		catch (TTKException exp)
 		{
 			throw new TTKException(exp, "async");
 		}//end of catch (TTKException exp)
 		finally // Control will reach here in anycase set null to the objects
 		{
 			resultSet = null;
 			cStmtObject = null;
 			connection = null;
		if(pw!=null)pw.close();
 		}//end of finally
 	}
	return null;
	
}

public ActionForward getPreAuthRefStatus(ActionMapping mapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, ParseException, TTKException
{
	String rownum=request.getParameter("rownum");
	TableData tableData = TTKCommon.getTableData(request);
	PreAuthVO preAuthVO=(PreAuthVO)tableData.getRowInfo(Integer.parseInt(rownum));
	PrintWriter pw=response.getWriter();
	pw.print(preAuthVO.getStatus());
	pw.close();
	return null;
}

public ActionForward getPolicyInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
PrintWriter	writer= response.getWriter();
//System.out.println("getPolicyInfogetPolicyInfogetPolicyInfogetPolicyInfo");
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet rs=null;
	try{
		String policyNumber=(String)request.getParameter("policyNumber");
		policyNumber=(policyNumber==null)?null:policyNumber.trim();
	con=ResourceManager.getConnection();
	
	statement=con.prepareStatement("SELECT CASE WHEN UPPER(NVL(D.AXA_VIDAL_IND,'AXA'))='AXA' then 'Y' else 'N' end as FLAG, CASE WHEN UPPER(NVL(D.AXA_VIDAL_IND,'AXA'))='AXA' THEN 'Fetch Key Coverages provision is only available with past policy numbers that exists in the Vidal Health system.' ELSE NULL END AS ALERT_MSG FROM APP.TPA_PRICING_PREV_POL_DATA D WHERE D.POLICY_NO='"+policyNumber+"'");		
      rs=statement.executeQuery();
      if(rs.next()){
    	  writer.print(rs.getString(1));
      }
      else writer.print("");
      
      writer.flush();	    
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);			
		}
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
  				log.error("Error while closing the Resultset in AsynchronusAction getProviderInvoiceNO()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getProviderInvoiceNO()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getProviderInvoiceNO()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			rs = null;
  			statement = null;
  			con = null;
		if(writer!=null)writer.close();
  		}//end of finally
  	}//end of finally	
    return null;
}


public ActionForward checkAccessForOverride(ActionMapping mapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, TTKException
{
	String activitySeqId=request.getParameter("activityDtlSeqId");
	PrintWriter pw=response.getWriter();
	response.setContentType("application/json");
	Connection connection=null;
	ResultSet resultSet=null;
	CallableStatement statement=null;
	try {
		connection=ResourceManager.getConnection();
		statement=connection.prepareCall("{CALL AUTHORIZATION_PKG.GET_DENIAL_CATEGORY(?,?)}");    
	    statement.setString(1,activitySeqId);
	    statement.registerOutParameter(2,OracleTypes.CURSOR);
		statement.execute();
		resultSet=(ResultSet)statement.getObject(2);
		int accessablecount=0;
		String restMsg="";
		String[] dataArr=new String[2];
		boolean isOverridable=false;
		if(resultSet.next())
		{
			dataArr[0]=resultSet.getString("OVERRIDE_YN");
			dataArr[1]=resultSet.getString("DEN_CAT");
		}
		if(dataArr.length==2){
			/*if(dataArr[1]!=null){
			String[] denialCategoryArr=dataArr[1].split("@");
			int actualLength=denialCategoryArr.length;
				for (int inc=0;inc<denialCategoryArr.length;inc++) {
					String denialCategory=denialCategoryArr[inc].toUpperCase()+"Override";
					boolean isAccessable=TTKCommon.isAuthorized(request, denialCategory);
					if(isAccessable==true)
						accessablecount++;
				}
				if(actualLength==accessablecount){
					if("Y".equals(dataArr[0]))
					{
						isOverridable=true;
						accessablecount=0;
					}
				}
		}else
			isOverridable=true;
		if(accessablecount==3&&"N".equals(dataArr[0]))
			restMsg="Sorry, You don't have access for override this activity";*/
		if("N".equals(dataArr[0])){
			isOverridable=false;
		}else{
			if(dataArr[1]!=null){
				String[] denialCategoryArr=dataArr[1].split("@");
				int actualLength=denialCategoryArr.length;
					for (int inc=0;inc<denialCategoryArr.length;inc++) {
						String denialCategory=denialCategoryArr[inc].toUpperCase()+"Override";
						boolean isAccessable=TTKCommon.isAuthorized(request, denialCategory);
						if(isAccessable==true)
							accessablecount++;
					}
					if(actualLength==accessablecount){
						if("Y".equals(dataArr[0]))
						{
							isOverridable=true;
							accessablecount=0;
						}else{
							isOverridable=false;
						}
					}else
						isOverridable=false;
			}else
				isOverridable=true;
		}
		if(isOverridable==false)
			restMsg="Sorry, You don't have access for override this activity";
		String concatData=String.valueOf(isOverridable)+"@"+restMsg;
		pw.print(concatData);
	}
		pw.flush();
	} 
	catch (TTKException | SQLException e)
	{
		e.printStackTrace();
	} 
	finally
	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (statement != null) statement.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getUnitTypePrice()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(connection != null) connection.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getUnitTypePrice()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			statement = null;
  			statement = null;
  			connection = null;
		if(pw!=null)pw.close();
  		}//end of finally
  	}
	return null;
	
}
public ActionForward getBroker(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet resultSet=null;
					
    String brokerId=(String)request.getParameter("brokerId");
    brokerId=(brokerId==null)?"0":brokerId.trim();
    String id="";
    String active="";
    //String query="select state_type_id,state_name from TPA_STATE_CODE where country_id="+countryId+" ORDER BY state_name";
    String query=" SELECT INS_COMP_CODE_NUMBER,ACTIVE_YN FROM TPA_BRO_INFO WHERE INS_SEQ_ID="+brokerId;
  // String query2="SELECT INS_SEQ_ID,INS_COMP_NAME,INS_COMP_CODE_NUMBER FROM TPA_BRO_INFO WHERE NVL(ACTIVE_YN,'N')='Y'";
   try{
  System.out.println("inside async ::");
	   con=ResourceManager.getConnection();
	statement=con.prepareStatement(query);
	  resultSet=statement.executeQuery();
	    
	    while(resultSet.next()){
    	  id=resultSet.getString(1);
    	  active=resultSet.getString(2);
    	}
	    if(active==""||active==null) active="Inactive";
	    else active="Active";
	    id=id+":"+active;
       request.getSession().setAttribute("Broker", id);
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		}
   finally
  	{
  		/* Nested Try Catch to ensure resource closure */
  		try // First try closing the result set
  		{
  			try
  			{
  				if (resultSet != null) resultSet.close();
  			}//end of try
  			catch (SQLException sqlExp)
  			{
  				log.error("Error while closing the Resultset in AsynchronusAction getStates()",sqlExp);
  				throw new TTKException(sqlExp, "async");
  			}//end of catch (SQLException sqlExp)
  			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  			{
  				try
  				{
  					if (statement != null) statement.close();
  				}//end of try
  				catch (SQLException sqlExp)
  				{
  					log.error("Error while closing the Statement in AsynchronusAction getStates()",sqlExp);
  					throw new TTKException(sqlExp, "async");
  				}//end of catch (SQLException sqlExp)
  				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
  				{
  					try
  					{
  						if(con != null) con.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Connection in AsynchronusAction getStates()",sqlExp);
  						throw new TTKException(sqlExp, "async");
  					}//end of catch (SQLException sqlExp)
  				}//end of finally Connection Close
  			}//end of finally Statement Close
  		}//end of try
  		catch (TTKException exp)
  		{
  			throw new TTKException(exp, "async");
  		}//end of catch (TTKException exp)
  		finally // Control will reach here in anycase set null to the objects
  		{
  			resultSet = null;
  			statement = null;
  			con = null;
  		}//end of finally
  	}//end of finally
  
   
  writer.print(id);
  writer.flush();
  if(writer!=null)writer.close();
    return null;
}

public ActionForward getStartDateEndDate(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
PrintWriter	writer= response.getWriter();
	Connection con=null;
	PreparedStatement statement=null;
	ResultSet rs=null;
	try{
	con=ResourceManager.getConnection();
	statement=con.prepareStatement("SELECT to_char(min(ep.effective_from_date),'DD/MM/YYYY') min_start_date,to_char(MAX(ep.effective_to_date),'DD/MM/YYYY') max_end_date FROM app.tpa_group_registration gr JOIN app.tpa_enr_policy ep ON(gr.group_reg_seq_id=ep.group_reg_seq_id) WHERE gr.group_id=? AND extract(YEAR FROM ep.effective_from_date)=? GROUP BY gr.group_id");
    String underWritingYear=(String)request.getParameter("underWritingYear");
         underWritingYear=(underWritingYear==null)?null:underWritingYear.trim();
	String corporateid=(String)request.getParameter("corporatename");
         corporateid=(corporateid==null)?null:corporateid.trim();

      statement.setString(1,corporateid);
	  statement.setString(2,underWritingYear);
      rs=statement.executeQuery();
      if(rs.next()){
	  writer.println(rs.getString("min_start_date")+"|"+rs.getString("max_end_date"));
	  
	  }else{
	  writer.print("NoValue");
	  }
      
      writer.flush();
      writer.close();
	}catch(Exception e){e.printStackTrace();}
	finally{
		if(rs!=null)rs.close(); rs= null;
		if(statement!=null)statement.close();statement = null;
		if(con!=null)con.close();con = null;
		if(writer!=null)writer.close();
	}
    return null;
 }

}