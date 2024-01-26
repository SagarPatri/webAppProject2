package com.ttk.dao.impl.processedcliams;

import org.apache.log4j.Logger;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.factory.DAOFactory;

public class ProcessedClaimDAOFactory extends DAOFactory{
	private static Logger log = Logger.getLogger(ProcessedClaimDAOFactory.class );
	@Override
	public BaseDAO getDAO(String strIdentifier) throws TTKException {
		 BaseDAO baseDAO = null;
	        log.info("Inside ClaimDAOFactory: "+strIdentifier);
		
	        if (strIdentifier.equals("processedclaim"))
	        {
	            baseDAO = new ProcessedClaimDAOImpl();
	        }
	        
	        
	        
		return baseDAO;
	}

}
