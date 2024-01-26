package com.ttk.business.onlineforms.providerLogin;


/**
 * @ (#) ClaimManagerBean.java 17 May 2017
 * Project 	     : TTK HealthCare Services
 * File          : ClaimManagerBean.java
 * Author        : Kishor kumar S H
 * Company       : Rcs Tech
 * Date Created  : 17 May 2017
 *
 * @author       :  Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 */


import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.onlineforms.providerLogin.ClaimsDAOImpl;
import com.ttk.dao.impl.preauth.PreAuthDAOFactory;
//added as per KOC 1140 and 1142(1165)

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class OnlineClaimManagerBean implements OnlineClaimManager{

    private PreAuthDAOFactory preAuthDAOFactory = null;
    private ClaimsDAOImpl claimsDAO = null;

    /**
     * This method returns the instance of the data access object to initiate the required tasks
     * @param strIdentifier String object identifier for the required data access object
     * @return BaseDAO object
     * @exception throws TTKException
     */
    private BaseDAO getClaimDAO(String strIdentifier) throws TTKException
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
    }//End of getPreAuthDAO(String strIdentifier)

    
    /**
     * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
     * @exception throws TTKException
     */
     

   
}//end of ClaimManagerBean
