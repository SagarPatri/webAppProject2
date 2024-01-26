package com.ttk.action.onlineforms.providerLogin;
/**
 * @ (#) AsynchronousSearchAction.java 27 April 2017
 * Project      : TTK HealthCare Services
 * File         : AsynchronousSearchAction.java
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : 27 April 2017
 *
 * @author       :  Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 */

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

/**
 * This class is used for Searching the List Policies to see the Online Account Info.
 * This also provides deletion and updation of products.
 */
public class AsynchronousSearchAction extends DispatchAction {

    private static final Logger log = Logger.getLogger( AsynchronousSearchAction.class );
private static final String clinicianProcedure="{CALL HOSP_DIAGNOSYS_PKG.SELECT_CLINICIAN_LIST(?,?,?,?,?,?,?,?)}";
private static final String clinicianNameProcedure="{CALL HOSP_DIAGNOSYS_PKG.SELECT_CLINICIAN_NAME_LIST(?,?,?,?,?,?,?,?)}";
private static final String drugProcedure="{CALL HOSP_DIAGNOSYS_PKG.SELECT_PBM_DRUG_LIST(?,?,?,?,?,?)}";
private static final String activityProcedure="{CALL HOSP_DIAGNOSYS_PKG.SELECT_ACTIVITY_LIST(?,?,?,?,?,?,?,?)}";
//cr--650
private static final String activityProcedure2="{CALL AUTHORIZATION_PKG.select_activity_description(?,?,?,?,?,?,?,?,?)}";
//private static final String activityProcedure2="{CALL AUTHORIZATION_PKG.select_activity_description(?,?,?,?,?,?,?,?,?)}";

private static final String drugProviderProcedure="{CALL HOSP_DIAGNOSYS_PKG.SELECT_DRUG_LIST(?,?,?,?,?,?,?,?,?)}";


public void  sentSearchData(HttpServletRequest request, HttpServletResponse response,String procedureName,String searchFlag)
		throws Exception{

	PrintWriter	writer= response.getWriter();
	StringBuilder searchDataBuilder=new StringBuilder();
	
	String strPageLinkNum=request.getParameter("sdPageLinkNum");
	String strSearchMode=request.getParameter("searchMode");
	String strPageForwardNum=request.getParameter("sdPageForwardNum");
	String strSearchFieldValue=request.getParameter(request.getParameter("paramName"));
	String hospSeqId	=	request.getParameter("hospSeqId");
	String alkootId		=	request.getParameter("alkootId");
	String treatmentDate=request.getParameter("treatmentDate");
	String selectMethod=request.getParameter("selectMethod");
   try{
	  
	   if("new".equals(strSearchMode)){
		
		int startNum,endNum;
		if(strPageForwardNum==null||strPageForwardNum.length()<1||"1".equals(strPageForwardNum)){
			strPageForwardNum="1";
		}
		startNum=((new Integer(strPageForwardNum)*100)-100)+1;
		
		endNum=(startNum+100);
		
		ArrayList<String> allSearchData=getData(procedureName,strSearchFieldValue,startNum,endNum,searchFlag,hospSeqId,alkootId,treatmentDate,selectMethod);
		request.getSession().setAttribute("allSearchData",allSearchData);
		
		
		if(allSearchData!=null&&allSearchData.size()>0){
			int endNumFirst;
			if(allSearchData.size()>10)endNumFirst=10;
			else endNumFirst=allSearchData.size();
				
			searchDataBuilder.append(allSearchData.size());
			
			searchDataBuilder.append("@");
			
			
			for(int i=0;i<endNumFirst;i++){
	        	searchDataBuilder.append(allSearchData.get(i));
	        }		
	        writer.print(searchDataBuilder);
		     writer.flush();	
	   }
		}else {
		   
		ArrayList<String> allSearchData=(ArrayList<String>)request.getSession().getAttribute("allSearchData");
			
		if(allSearchData!=null&&allSearchData.size()>0){
		       int startNumb=0;
		       int endNumb=0;
		       if(strPageLinkNum==null||strPageLinkNum.length()<1||"1".equals(strPageLinkNum)){
		    	   strPageLinkNum="1";
				}
		      int intPageLink=new Integer(strPageLinkNum);
		      
		       startNumb=((intPageLink*10)-10);
		       
		       if(allSearchData.size()>(startNumb+10))endNumb=startNumb+10;
		       else    endNumb=allSearchData.size();
		   	
				searchDataBuilder.append(allSearchData.size());
				
				searchDataBuilder.append("@");
		       
			for(;startNumb<endNumb;startNumb++){
	        	searchDataBuilder.append(allSearchData.get(startNumb));
	        }		
			
	        writer.print(searchDataBuilder);
		     writer.flush();	
	   }
			
	   }
	     
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.flush();
		}

}


public void  sentSearchData2(HttpServletRequest request, HttpServletResponse response,String procedureName,String searchFlag,String type)
		throws Exception{

	PrintWriter	writer= response.getWriter();
	StringBuilder searchDataBuilder=new StringBuilder();
	
	String strPageLinkNum=request.getParameter("sdPageLinkNum");
	String strSearchMode=request.getParameter("searchMode");
	String strPageForwardNum=request.getParameter("sdPageForwardNum");
	String strSearchFieldValue=request.getParameter(request.getParameter("paramName"));
	String hospSeqId	=	request.getParameter("hospSeqId");
	String alkootId		=	request.getParameter("alkootId");
	String treatmentDate=request.getParameter("treatmentDate");
	String selectMethod=request.getParameter("selectMethod");
   try{
	  
	   if("new".equals(strSearchMode)){
		
		int startNum,endNum;
		if(strPageForwardNum==null||strPageForwardNum.length()<1||"1".equals(strPageForwardNum)){
			strPageForwardNum="1";
		}
		startNum=((new Integer(strPageForwardNum)*100)-100)+1;
		
		endNum=(startNum+100);
		PreAuthDetailVO preAuthDetailVO = (PreAuthDetailVO)request.getSession().getAttribute("preauthDetail");
	//preAuthDetailVO.get
		ArrayList<String> allSearchData=getData2(procedureName,strSearchFieldValue,startNum,endNum,searchFlag,preAuthDetailVO.getProviderSeqId().toString(),preAuthDetailVO.getMemberId(),treatmentDate,selectMethod,type);
		request.getSession().setAttribute("allSearchData",allSearchData);
		
		
		if(allSearchData!=null&&allSearchData.size()>0){
			int endNumFirst;
			if(allSearchData.size()>10)endNumFirst=10;
			else endNumFirst=allSearchData.size();
				
			searchDataBuilder.append(allSearchData.size());
			
			searchDataBuilder.append("@");
			
			
			for(int i=0;i<endNumFirst;i++){
	        	searchDataBuilder.append(allSearchData.get(i));
	        }		
	        writer.print(searchDataBuilder);
		     writer.flush();	
	   }
		}else {
		   
		ArrayList<String> allSearchData=(ArrayList<String>)request.getSession().getAttribute("allSearchData");
			
		if(allSearchData!=null&&allSearchData.size()>0){
		       int startNumb=0;
		       int endNumb=0;
		       if(strPageLinkNum==null||strPageLinkNum.length()<1||"1".equals(strPageLinkNum)){
		    	   strPageLinkNum="1";
				}
		      int intPageLink=new Integer(strPageLinkNum);
		      
		       startNumb=((intPageLink*10)-10);
		       
		       if(allSearchData.size()>(startNumb+10))endNumb=startNumb+10;
		       else    endNumb=allSearchData.size();
		   	
				searchDataBuilder.append(allSearchData.size());
				
				searchDataBuilder.append("@");
		       
			for(;startNumb<endNumb;startNumb++){
	        	searchDataBuilder.append(allSearchData.get(startNumb));
	        }		
			
	        writer.print(searchDataBuilder);
		     writer.flush();	
	   }
			
	   }
	     
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.flush();
		}

}

private ArrayList<String> getData(String procedureName,String searchValue,int startNum,int endNum,String searchFlag,String hospSeqId,String alkootId,String treatmentDate,String selectMethod) 
throws TTKException{
	Connection con=null;
	CallableStatement callStm=null;
	ResultSet resultSet=null;
	ArrayList<String> allSearchData=new ArrayList<>();
   try{
		 con=ResourceManager.getConnection();   
		 searchValue=(searchValue==null)?"":searchValue.trim();
	     callStm=con.prepareCall(procedureName);
	     callStm.setString(1, searchValue);
	     callStm.setString(2, hospSeqId);//required for Internal code fetching - activity details
	     callStm.setString(3, null);
	     callStm.setInt(4, startNum);
	     callStm.setInt(5, endNum);
	     callStm.setString(6, searchFlag);
	     callStm.setString(7, alkootId!=null?alkootId.trim():"");
	     if("onSelectDrugDesc".equals(selectMethod))
	     {
	    	 callStm.setString(8, treatmentDate);
	    	 callStm.registerOutParameter(9, OracleTypes.CURSOR);
		     callStm.execute();
	    	 resultSet=(ResultSet)callStm.getObject(9);
	     }
	     else
	     {
	    	 callStm.registerOutParameter(8, OracleTypes.CURSOR);
		     callStm.execute();
	    	 resultSet=(ResultSet)callStm.getObject(8);
	     }
    	 
	        if(resultSet!=null){
	       		    	 while(resultSet.next()){	    		 
		    			 allSearchData.add(resultSet.getString("SEARCH_DATA").replace("'", "\"")+"|");
    		    	 }
    		     }
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
 				log.error("Error while closing the Resultset in AsynchronusAction getData()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (callStm != null) callStm.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getData()",sqlExp);
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
 						log.error("Error while closing the Connection in AsynchronusAction getData()",sqlExp);
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
 			callStm = null;
 			con = null;
 		}//end of finally
 	}//end of finally	
    return allSearchData;

}


public ActionForward  doClinicianSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	HttpSession session=request.getSession();
	UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) session.getAttribute("UserSecurityProfile");
sentSearchData(request, response, clinicianProcedure,userSecurityProfile.getHospSeqId().toString());   
    return null;

}

public ActionForward  doClinicianNameSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	HttpSession session=request.getSession();
	UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) session.getAttribute("UserSecurityProfile");
sentSearchData(request, response, clinicianNameProcedure,userSecurityProfile.getHospSeqId().toString());   
    return null;

}


public ActionForward  doDrugDetailsSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{
	sentSearchData(request, response, drugProcedure,"");   
	  return null;
}

//Activity Code and Desc Search
public ActionForward  doActivityCodeSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{
	String icdMode	=	request.getParameter("icdMode");
	sentSearchData(request, response, activityProcedure,icdMode);   
	  return null;
}


public ActionForward  doActivitySearchClaims(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{
	
	String icdMode	=	request.getParameter("icdMode");
	String type	=	request.getParameter("type");
	sentSearchData2(request, response, activityProcedure2,icdMode,type);   
    return null;
}


//Drug Code and Desc Search
public ActionForward  doDrugCodeSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{
	String icdMode	=	request.getParameter("icdMode");
	sentSearchData(request, response, drugProviderProcedure,icdMode);   
	  return null;
}



public ActionForward  doDiagDetailsSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	StringBuilder searchDataBuilder=new StringBuilder();
	
	String strPageLinkNum=request.getParameter("sdPageLinkNum");
	String strSearchMode=request.getParameter("searchMode");
	String strPageForwardNum=request.getParameter("sdPageForwardNum");
	String strIcdCodeDesc=request.getParameter("icdDesc");
	String strIcdCode=request.getParameter("icdCode");
	String strIcdMode=request.getParameter("icdMode");
	//System.out.println("page::"+strPageLinkNum+": searchmode:"+strSearchMode+":icdcodedesc:"+strIcdCodeDesc+":icd:"+strIcdCode+":icdmode:"+strIcdMode);
	
   try{	 
	  
	   if("new".equals(strSearchMode)){
		
		int startNum,endNum;
		if(strPageForwardNum==null||strPageForwardNum.length()<1||"1".equals(strPageForwardNum)){
			strPageForwardNum="1";
		}
		startNum=((new Integer(strPageForwardNum)*100)-100)+1;
		
		endNum=(startNum+100);
		
		ArrayList<String> allSearchData=getIcdData(strIcdCodeDesc,strIcdCode,strIcdMode,startNum,endNum);
		request.getSession().setAttribute("allSearchData",allSearchData );
		
		if(allSearchData!=null&&allSearchData.size()>0){
			int endNumFirst;
			if(allSearchData.size()>10)endNumFirst=10;
			else endNumFirst=allSearchData.size();
				
			searchDataBuilder.append(allSearchData.size());
			
			searchDataBuilder.append("@");
			
			
			for(int i=0;i<endNumFirst;i++){
	        	searchDataBuilder.append(allSearchData.get(i));
	        }		
	        writer.print(searchDataBuilder);
		     writer.flush();	
	   }
		}else {
		   
		ArrayList<String> allSearchData=(ArrayList<String>)request.getSession().getAttribute("allSearchData" );
			
		if(allSearchData!=null&&allSearchData.size()>0){
		       int startNumb=0;
		       int endNumb=0;
		       if(strPageLinkNum==null||strPageLinkNum.length()<1||"1".equals(strPageLinkNum)){
		    	   strPageLinkNum="1";
				}
		      int intPageLink=new Integer(strPageLinkNum);
		      
		       startNumb=((intPageLink*10)-10);
		       
		       if(allSearchData.size()>(startNumb+10))endNumb=startNumb+10;
		       else    endNumb=allSearchData.size();
		   	
				searchDataBuilder.append(allSearchData.size());
				
				searchDataBuilder.append("@");
		       
			for(;startNumb<endNumb;startNumb++){
	        	searchDataBuilder.append(allSearchData.get(startNumb));
	        }		
		//System.out.println("data::"+searchDataBuilder.toString());	
	        writer.print(searchDataBuilder);
		     writer.flush();	
	   }
			
	   }
	     
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		writer.flush();
		}
   
    return null;
}




   private ArrayList<String> getIcdData(String icdCodeDesc,String strIcdCode,String strIcdMode,int startNum,int endNum) 
   throws TTKException {

		Connection con=null;
		CallableStatement callStm=null;
		ResultSet resultSet=null;
		//ArrayList<String> startSearchData=null;
		//ArrayList<String> containsSearchData=null;
		ArrayList<String> allSearchData=new ArrayList<>();
		StringBuilder builder=null;
	   try{	 
			 con=ResourceManager.getConnection();   
			 icdCodeDesc=(icdCodeDesc==null)?"":icdCodeDesc.trim();
		     callStm=con.prepareCall("{CALL HOSP_DIAGNOSYS_PKG.SELECT_PBM_ICD_LIST(?,?,?,?,?,?,?,?)}");
		     if("CODE".equals(strIcdMode)){
			     callStm.setString(1, strIcdCode);
			     callStm.setString(2, null);
		     }else {
		    	 callStm.setString(1, null);
			     callStm.setString(2, icdCodeDesc);
		     }
		     callStm.setString(3, strIcdMode);
		     callStm.setString(4, null);
		     callStm.setString(5, null);
		     callStm.setInt(6, startNum);
		     callStm.setInt(7, endNum);
		     callStm.registerOutParameter(8, OracleTypes.CURSOR);
		     callStm.execute();
		     
		     resultSet=(ResultSet)callStm.getObject(8);
		   
		        if(resultSet!=null){
		        //	startSearchData=new ArrayList<>();
		        	//containsSearchData=new ArrayList<>();
	    		    	 while(resultSet.next()){   
	    		    		 
	    		    		 builder=new StringBuilder();
	    		    		
	    		    			 builder.append(resultSet.getString("ICD10_SEQ_ID"));
	    		    			 builder.append("#");	    		    			
	    		    			 builder.append(resultSet.getString("DESCREPTION"));
	    		    			 builder.append("--");
	    		    			 builder.append(resultSet.getString("rcnt"));
	    		    			 builder.append("|");
	    		    			 allSearchData.add(builder.toString());
	    		    			 
	    		    	 }
	    		    	 
	    		     }
		     
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
	 				log.error("Error while closing the Resultset in AsynchronusAction getIcdData()",sqlExp);
	 				throw new TTKException(sqlExp, "async");
	 			}//end of catch (SQLException sqlExp)
	 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
	 			{
	 				try
	 				{
	 					if (callStm != null) callStm.close();
	 				}//end of try
	 				catch (SQLException sqlExp)
	 				{
	 					log.error("Error while closing the Statement in AsynchronusAction getIcdData()",sqlExp);
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
	 						log.error("Error while closing the Connection in AsynchronusAction getIcdData()",sqlExp);
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
	 			callStm = null;
	 			con = null;
	 		}//end of finally
	 	}//end of finally	
	    return allSearchData;

   }
   

public ActionForward  doDrugDescSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	PrintWriter	writer= response.getWriter();
	Connection con=null;
	Statement statement=null;
	ResultSet resultSet=null;
   try{	 
	   StringBuilder startBuilder=new StringBuilder();
	   StringBuilder containsBuilder=new StringBuilder();
	   
	     con=ResourceManager.getConnection();     
	     String drugCodeDesc=request.getParameter("drugCodeDesc");
	     drugCodeDesc=(drugCodeDesc==null)?"":drugCodeDesc.trim();
	     
	     statement=con.createStatement();
	     resultSet=(ResultSet)statement.executeQuery("SELECT M.ACTIVITY_CODE,M.ACTIVITY_DESCRIPTION FROM APP.TPA_PHARMACY_MASTER_DETAILS M WHERE UPPER(M.ACTIVITY_DESCRIPTION) LIKE UPPER('%"+drugCodeDesc+"%')");
	     
	        if(resultSet!=null){
    		    	 while(resultSet.next()){    		    		
    		    		 if(resultSet.getString("ACTIVITY_DESCRIPTION").startsWith(drugCodeDesc)){
    		    			 startBuilder.append(resultSet.getString("ACTIVITY_CODE"));
    		    			 startBuilder.append("#");
    		    			 startBuilder.append(resultSet.getString("ACTIVITY_DESCRIPTION"));
    		    			 startBuilder.append("|");
    		    		 }else {
    		    			 containsBuilder.append(resultSet.getString("ACTIVITY_CODE"));
    		    			 containsBuilder.append("#");
    		    			 containsBuilder.append(resultSet.getString("ACTIVITY_DESCRIPTION"));
    		    			 containsBuilder.append("|");
    		    		 }
    		    	 }
    		     }
	     
	     writer.print(startBuilder.toString()+containsBuilder.toString());
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
				log.error("Error while closing the Resultset in AsynchronusAction doDrugDescSearch()",sqlExp);
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
					log.error("Error while closing the Statement in AsynchronusAction doDrugDescSearch()",sqlExp);
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
						log.error("Error while closing the Connection in AsynchronusAction doDrugDescSearch()",sqlExp);
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
			if(writer!=null) writer.close();
		}//end of finally
	}//end of finally	 
    return null;
}

public ActionForward getIcdDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{

	   PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet resultSet=null;
						
	    String strIcdSeqID=request.getParameter("icdSeqID");
	    String strIcdMode=request.getParameter("icdMode");
	    String searchFlag=request.getParameter("searchFlag");
	   String query="";
	   if("getActDesc".equals(searchFlag))
		   query="SELECT D.ACTIVITY_CODE, D.ACTIVITY_DESCRIPTION  FROM TPA_ACTIVITY_MASTER_DETAILS D WHERE D.ACT_MAS_DTL_SEQ_ID="+strIcdSeqID;
	   else if("getActCode".equals(searchFlag))
		   query="SELECT D.ACTIVITY_CODE, D.ACTIVITY_DESCRIPTION  FROM TPA_ACTIVITY_MASTER_DETAILS D WHERE D.ACT_MAS_DTL_SEQ_ID="+strIcdSeqID;
	   else if("getDrugDesc".equals(searchFlag))
		   query="SELECT D.ACTIVITY_CODE, (D.ACTIVITY_DESCRIPTION||'--'||D.GRANULAR_UNIT)  FROM TPA_PHARMACY_MASTER_DETAILS D WHERE D.ACT_MAS_DTL_SEQ_ID ="+strIcdSeqID;
	   else if("getDrugCode".equals(searchFlag))
		   query="SELECT (D.ACTIVITY_CODE||'--'||D.GRANULAR_UNIT), D.ACTIVITY_DESCRIPTION  FROM TPA_PHARMACY_MASTER_DETAILS D WHERE D.ACT_MAS_DTL_SEQ_ID="+strIcdSeqID;
	   else
		   query="SELECT D.ICD_CODE,D.LONG_DESC FROM TPA_ICD10_MASTER_DETAILS D WHERE D.ICD10_SEQ_ID="+strIcdSeqID;
	   try{
		   String icdCode="";
		   String icdCodeDesc="";
	      con=ResourceManager.getConnection();
		  statement=con.prepareStatement(query);
		  resultSet=statement.executeQuery();
		   if(resultSet!=null){
			   
			   if(resultSet.next()){
				   icdCode=resultSet.getString(1);
		    	    icdCodeDesc=resultSet.getString(2);
			   }
		   }
	       
		   if("DESC".equals(strIcdMode)){
		      writer.print(icdCodeDesc);
		   }else if("CODE".equals(strIcdMode)){
			      writer.print(icdCode);
		    }
		      writer.flush();
		      if(writer!=null)writer.close();
		        return null;
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
	 				log.error("Error while closing the Resultset in AsynchronusAction getData()",sqlExp);
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
	 					log.error("Error while closing the Statement in AsynchronusAction getData()",sqlExp);
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
	 						log.error("Error while closing the Connection in AsynchronusAction getData()",sqlExp);
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
	 			if(writer!=null) writer.close();
	 		}//end of finally
	 	}//end of finally	
return null;	  
}
private ArrayList<String> getData2(String procedureName,String searchValue,int startNum,int endNum,String searchFlag,String hospSeqId,String alkootId,String treatmentDate,String selectMethod,String type) 
throws TTKException{
	Connection con=null;
	CallableStatement callStm=null;
	ResultSet resultSet=null;
	ArrayList<String> allSearchData=new ArrayList<>();
   try{
	  
		 con=ResourceManager.getConnection();   
		 searchValue=(searchValue==null)?"":searchValue.trim();
	     callStm=con.prepareCall(procedureName);
	     callStm.setString(1, searchValue);
	     callStm.setString(2, hospSeqId);//required for Internal code fetching - activity details
	     callStm.setString(3, null);
	     callStm.setInt(4, startNum);
	     callStm.setInt(5, endNum);
	     callStm.setString(6, "DESC");
	     callStm.setString(7, alkootId!=null?alkootId.trim():"");
	     
	     if("onSelectDrugDesc".equals(selectMethod))
	     {
	    	 callStm.setString(8, treatmentDate);
	    	 callStm.registerOutParameter(9, OracleTypes.CURSOR);
		     callStm.execute();
	    	 resultSet=(ResultSet)callStm.getObject(9);
	     }
	     else
	     {callStm.setString(8, type!=null?type.trim():"");
	    	 callStm.registerOutParameter(9, OracleTypes.CURSOR);
		     callStm.execute();
	    	 resultSet=(ResultSet)callStm.getObject(9);
	     }
    	 
	        if(resultSet!=null){
	       		    	 while(resultSet.next()){	    		 
		    			 allSearchData.add(resultSet.getString("SEARCH_DATA").replace("'", "\"")+"|");
    		    	 }
	        }
	     	   
	     	   
	     	   

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
 				log.error("Error while closing the Resultset in AsynchronusAction getData()",sqlExp);
 				throw new TTKException(sqlExp, "async");
 			}//end of catch (SQLException sqlExp)
 			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 			{
 				try
 				{
 					if (callStm != null) callStm.close();
 				}//end of try
 				catch (SQLException sqlExp)
 				{
 					log.error("Error while closing the Statement in AsynchronusAction getData()",sqlExp);
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
 						log.error("Error while closing the Connection in AsynchronusAction getData()",sqlExp);
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
 			callStm = null;
 			con = null;
 		}//end of finally
 	}//end of finally	
   return allSearchData;

}




}//end of AsynchronousSearchAction
