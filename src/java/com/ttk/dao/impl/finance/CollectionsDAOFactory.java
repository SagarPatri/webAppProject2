package com.ttk.dao.impl.finance;

import org.apache.log4j.Logger;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.factory.DAOFactory;

public class CollectionsDAOFactory extends DAOFactory{
	private static Logger log = Logger.getLogger(CollectionsDAOFactory.class );
	
	@Override
	public BaseDAO getDAO(String strIdentifier) throws TTKException { 
	BaseDAO baseDAO = null;
    log.info("Inside CollectionsDAOFactory: "+strIdentifier);
    if (strIdentifier.equals("collections"))
    {
        baseDAO = new CollectionsDAOImpl();
    }//end of if (strIdentifier.equals("collections"))
    log.info("Returning : "+baseDAO);
    return baseDAO;
    
	}
	

}
