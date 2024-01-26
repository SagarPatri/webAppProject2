package com.ttk.business.onlineforms.providerLogin;

/**
 *   @ (#) ClaimManager.java 17 May 2017
 *   Project 	   : TTK HealthCare Services
 *   File          : ClaimManager.java
 *   Author        : Kishor kumar S H
 *   Company       : Rcs Tech
 *   Date Created  : 17 May 2017
 *
 *   @author       :  Kishor kumar S H
 *   Modified by   :
 *   Modified date :
 *   Reason        :
 *
 */


import javax.ejb.Local;
//added as per kOC 1140 and 1142 (1165)

@Local

public interface OnlineClaimManager {

    /**
     * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
     * @exception throws TTKException
     */
    
    
}//end of ClaimManager