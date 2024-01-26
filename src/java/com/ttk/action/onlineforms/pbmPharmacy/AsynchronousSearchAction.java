package com.ttk.action.onlineforms.pbmPharmacy; 
/**
 * @ (#) PbmPharmacyGeneralAction.java 27 MAR 2017
 * Project      : TTK HealthCare Services
 * File         : PbmPharmacyGeneralAction.java
 * Author       : Nagababu K
 * Company      : RCS Technologies
 * Date Created : 27 MAR 2017
 *
 * @author       :  Nagababu K
 * Modified by   :
 * Modified date :
 * Reason        :
 */

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.ttk.dao.ResourceManager;

/**
 * This class is used for Searching the List Policies to see the Online Account Info.
 * This also provides deletion and updation of products.
 */
public class AsynchronousSearchAction extends DispatchAction {

    private static final Logger log = Logger.getLogger( AsynchronousSearchAction.class );
private static final String clinicianProcedure="{CALL HOSP_PBM_PKG.SELECT_PBM_CLIN_LIST(?,?,?,?,?,?,?)}";
private static final String diagnosisProcedure="{CALL HOSP_PBM_PKG.SELECT_PBM_ICD_LIST(?,?,?,?,?,?,?)}";
private static final String drugProcedure="{CALL HOSP_PBM_PKG.SELECT_PBM_DRUG_LIST(?,?,?,?,?,?,?,?)}";

public void  sentSearchData(HttpServletRequest request, HttpServletResponse response,String procedureName)
		throws Exception{

	PrintWriter	writer= response.getWriter();
	StringBuilder searchDataBuilder=new StringBuilder();
	
	String strPageLinkNum=request.getParameter("sdPageLinkNum");
	String strSearchMode=request.getParameter("searchMode");
	String strPageForwardNum=request.getParameter("sdPageForwardNum");
	String strSearchFieldValue=request.getParameter(request.getParameter("paramName"));
	String strSearchFieldMode=request.getParameter("searchFieldMode");
	String dateOfTreatment=request.getParameter("dateOfTreatment");
	String selectMethod=request.getParameter("selectMethod");
   try{	 
	  
	   if("new".equals(strSearchMode)){
		
		int startNum,endNum;
		if(strPageForwardNum==null||strPageForwardNum.length()<1||"1".equals(strPageForwardNum)){
			strPageForwardNum="1";
		}
		startNum=((new Integer(strPageForwardNum)*100)-100)+1;
		
		endNum=(startNum+100);
		
		ArrayList<String> allSearchData=getData(procedureName,strSearchFieldValue,startNum,endNum,strSearchFieldMode,dateOfTreatment,selectMethod);
		request.getSession().setAttribute("allSearchData",allSearchData );
		
		if(allSearchData!=null&&allSearchData.size()>0){
			int endNumFirst;
			if(allSearchData.size()>10)endNumFirst=10;
			else endNumFirst=allSearchData.size();
				
			searchDataBuilder.append(allSearchData.size());
			
			searchDataBuilder.append("@");
			
			
			for(int i=0;i<endNumFirst;i++){
	        	searchDataBuilder.append(allSearchData.get(i).replace("'", ""));
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
	        	searchDataBuilder.append(allSearchData.get(startNumb).replace("'", ""));
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


private ArrayList<String> getData(String procedureName,String searchValue,int startNum,int endNum,String strSearchFieldMode,String dateOfTreatment, String selectMethod) {

	Connection con=null;
	CallableStatement callStm=null;
	ResultSet resultSet=null;
	ArrayList<String> allSearchData=new ArrayList<>();
   try{	 		   
	  
		 con=ResourceManager.getConnection();   
		 searchValue=(searchValue==null)?"":searchValue.trim();
	     callStm=con.prepareCall(procedureName);
	     callStm.setString(1, strSearchFieldMode);
	     callStm.setString(2, searchValue);
	     callStm.setString(3, null);
	     callStm.setString(4, null);
	     callStm.setInt(5, startNum);
	     callStm.setInt(6, endNum);
	     if("onSelectDrugDesc".equals(selectMethod))
	     {
	    	 callStm.setString(7, dateOfTreatment);
	    	 callStm.registerOutParameter(8, OracleTypes.CURSOR);
	    	 callStm.execute();
	    	 resultSet=(ResultSet)callStm.getObject(8);
	     }
	     else
	     {
	    	 callStm.registerOutParameter(7, OracleTypes.CURSOR);
	    	 callStm.execute();
	    	 resultSet=(ResultSet)callStm.getObject(7);
	     }
	        if(resultSet!=null){
	        	
	       		    	 while(resultSet.next()){     		    		 
    		    			 allSearchData.add(resultSet.getString("SEARCH_DATA")+"|");
    		    			
    		    	 }

    		     }
	     
	}catch(Exception exception){
		log.error(exception.getMessage(), exception);
		}
   
	finally{
		
		try{
			
			try{
				
		 if(resultSet!=null)resultSet.close();
		 if(callStm!=null)callStm.close();
		
			}catch(Exception e){e.printStackTrace();}
			
		if(con!=null)con.close();
		
		}catch(Exception e){e.printStackTrace();}
	 }  
    return allSearchData;

}


public ActionForward  doClinicianSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

sentSearchData(request, response, clinicianProcedure);   
    return null;

}


public ActionForward  doDrugDetailsSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{
	sentSearchData(request, response, drugProcedure);   
	  return null;
}
public ActionForward  doIcdDetailsSearch(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{
	sentSearchData(request, response, diagnosisProcedure);   
	  return null;
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
	finally{
		if(resultSet!=null)resultSet.close();
		if(statement!=null)statement.close();
		if(con!=null)con.close();
		if(writer!=null)writer.close();
	 }  
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
	   
	    String query="SELECT D.ICD_CODE,D.LONG_DESC FROM TPA_ICD10_MASTER_DETAILS D WHERE D.ICD10_SEQ_ID="+strIcdSeqID;
	   try{
		   String icdCode="";
		   String icdCodeDesc="";
	      con=ResourceManager.getConnection();
		  statement=con.prepareStatement(query);
		  resultSet=statement.executeQuery();
		   if(resultSet!=null){
			   
			   if(resultSet.next()){
				   icdCode=resultSet.getString("ICD_CODE");
		    	    icdCodeDesc=resultSet.getString("LONG_DESC");
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
		finally{
			if(resultSet!=null)resultSet.close();	
			if(statement!=null)statement.close();
			if(con!=null)con.close();
		
		 }
return null;	  
}


public ActionForward getDrugDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{

	   PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet resultSet=null;
						
	    String strDrugCodeSeqID=request.getParameter("drugCodeSeqID");
	    String searchMode=request.getParameter("searchMode");
	    
	 

	    String query="SELECT P.GRANULAR_UNIT FROM APP.tpa_pharmacy_master_details P WHERE P.ACT_MAS_DTL_SEQ_ID ="+strDrugCodeSeqID;
	   try{
		   String qtygranularUnit="";
		  
	      con=ResourceManager.getConnection();
		  statement=con.prepareStatement(query);
		  resultSet=statement.executeQuery();
		   if(resultSet!=null){
			   
			   if(resultSet.next()){
				   qtygranularUnit=resultSet.getString("GRANULAR_UNIT");
		    	   
			   }
		   }
	       
		  
		   if("QAN".equals(searchMode)){
			
		      writer.print(qtygranularUnit);
		   }
		      writer.flush();
		      if(writer!=null)writer.close();
		        return null;
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);
			}
		finally{
			if(resultSet!=null)resultSet.close();	
			if(statement!=null)statement.close();
			if(con!=null)con.close();					
		 }
return null;	  
}

public ActionForward getClinicianDetails(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception{

	   PrintWriter	writer= response.getWriter();
		Connection con=null;
		PreparedStatement statement=null;
		ResultSet resultSet=null;
						
	    String strClinicianSeqID=request.getParameter("clinicianSeqID");
	    String strClinicianMode=request.getParameter("clinicianMode");
	   
	    String query="SELECT CLM.CLINITIAN_ID,CLM.PROFESSIONAL_NAME FROM DHA_CLINICIANS_LIST_MASTER CLM WHERE CLM.CLINI_SEQ_ID="+strClinicianSeqID;
	   try{
		   String clinicianID="";
		   String clinicianName="";
	      con=ResourceManager.getConnection();
		  statement=con.prepareStatement(query);
		  resultSet=statement.executeQuery();
		   if(resultSet!=null){
			   
			   if(resultSet.next()){
				   clinicianID=resultSet.getString("CLINITIAN_ID");
				   clinicianName=resultSet.getString("PROFESSIONAL_NAME");
			   }
		   }
	       
		   if("NAME".equals(strClinicianMode)){
		      writer.print(clinicianName);
		   }else if("ID".equals(strClinicianMode)){
			      writer.print(clinicianID);
		    }
		      writer.flush();
		      if(writer!=null)writer.close();
		        return null;
		}catch(Exception exception){
			log.error(exception.getMessage(), exception);
			}
		finally{
			if(resultSet!=null)resultSet.close();			
			if(statement!=null)statement.close();
			if(con!=null)con.close();
		 }
return null;	  
}

}//end of AsynchronousSearchAction
