package com.ttk.business.preauth;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.preauth.PreAuthDAOFactory;
import com.ttk.dao.impl.preauth.PreAuthDashboardDAOImpl;
import com.ttk.dao.impl.preauth.PreAuthSupportDAOImpl;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.preauth.AuthorizationDashboardVO;
import com.ttk.dto.preauth.BufferDetailVO;
import com.ttk.dto.preauth.PreAuthAssignmentVO;
import com.ttk.dto.preauth.PreDashboardVO;

@Stateless

@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)
public class PreAuthDashboardManagerBean implements PreAuthDashboardManager{
	
	  private PreAuthDAOFactory preAuthDAOFactory = null;
	  private PreAuthDashboardDAOImpl PreAuthDashboardDAO = null;
	  
	  /**
	     * This method returns the instance of the data access object to initiate the required tasks
	     * @param strIdentifier String object identifier for the required data access object
	     * @return BaseDAO object
	     * @exception throws TTKException
	     */
	  
	    private BaseDAO getPreAuthDashboardDAO(String strIdentifier) throws TTKException
	    {
	        try
	        {
	            if (preAuthDAOFactory == null)
	                preAuthDAOFactory = new PreAuthDAOFactory();
	            if(strIdentifier!=null)
	            {
	                return preAuthDAOFactory.getDAO(strIdentifier);
	            }//end of if(strIdentifier!=null)
	            else
	                return null;
	        }//end of try
	        catch(Exception exp)
	        {
	            throw new TTKException(exp, "error."+strIdentifier+".dao");
	        }//end of catch(Exception exp)
	    }//End of getPreAuthSupportDAO(String strIdentifier)

	@Override
	public PreDashboardVO getManagementDetails(Long userSeqId)
			throws TTKException {
		// TODO Auto-generated method stub
		
		PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
    	return (PreDashboardVO) PreAuthDashboardDAO.getManagementDetails(userSeqId);
		
	}

	@Override
	public ArrayList getDataEntryUserInfo(ArrayList alSearchCreteria)
			throws TTKException {
		// TODO Auto-generated method stub
		PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
    	return  PreAuthDashboardDAO.getDataEntryUserInfo(alSearchCreteria);
	}

	@Override
	public ArrayList getDataEntrySelfAssinmentCase(ArrayList alSearchCreteria)
			throws TTKException {
		// TODO Auto-generated method stub
		PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
    	return  PreAuthDashboardDAO.getDataEntrySelfAssinmentCase(alSearchCreteria);
	}

	@Override
	public ArrayList getDataEntryActiveUsers(ArrayList al) throws TTKException {
		// TODO Auto-generated method stub
	     PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
    	return  PreAuthDashboardDAO.getDataEntryActiveUsers(al);
	}

	@Override
	public ArrayList<CacheObject> getAssignedUserList(Long contactSeqid)
			throws TTKException {
		// TODO Auto-generated method stub
		 PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
		return PreAuthDashboardDAO.getAssignedUserList(contactSeqid);
	}

	@Override
	public Long saveAssignedUser(
			PreAuthAssignmentVO preAuthAssignmentVO)
			throws TTKException {
		// TODO Auto-generated method stub
		PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
		return PreAuthDashboardDAO.saveAssignedUser(preAuthAssignmentVO);
	}

	@Override
	public ArrayList getPreauthUserDashBoadinfo(ArrayList alSearchCreteria)
			throws TTKException {
		// TODO Auto-generated method stub
		PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
		return PreAuthDashboardDAO.getPreauthUserDashBoadinfo(alSearchCreteria);
	}

	@Override
	public ArrayList getPreauthBoardinfo(ArrayList alSearchCreteria)
			throws TTKException {
		// TODO Auto-generated method stub
		PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
		return PreAuthDashboardDAO.getPreauthBoardinfo(alSearchCreteria);
	}

	@Override
	public PreDashboardVO getDoctorDashboardDetails(Long userSeqId)
			throws TTKException {
		// TODO Auto-generated method stub
		PreAuthDashboardDAO = (PreAuthDashboardDAOImpl)this.getPreAuthDashboardDAO("dashboard");
		return PreAuthDashboardDAO.getDoctorDashboardDetails(userSeqId);
	}


	
}
