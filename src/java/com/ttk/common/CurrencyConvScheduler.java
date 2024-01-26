/**
 * @ (#) CurrencyConvScheduler.java Aug 9, 2007
 * Project 	     : Vidal Health Care
 * File          : CurrencyConvScheduler.java
 * Author        : Kishor kumar S H
 * Company       : Vidal Health Care
 * Date Created  : 15th May 2018
 *
 * @author       :  Kishor kumar S H
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.ttk.business.common.messageservices.CommunicationManager;
import com.ttk.common.exception.TTKException;

public class CurrencyConvScheduler implements Job {
	private static Logger log = Logger.getLogger( CurrencyConvScheduler.class );
	private static final String strCurrencyConvScheduler="CurrencyConvScheduler";
	CommunicationManager communicationManager = null;
	public void execute(JobExecutionContext arg0) {
		try
		{
			communicationManager = this.getCommunicationManagerObject();
			
			log.info("Inside run method in CurrencyConvScheduler");
			CurrencyConverterClient converterClient	=	new CurrencyConverterClient();
			Map<String,Object> currencyData	=	converterClient.getCurrencyData();
			/*for(Map.Entry<String, Object> entry: currencyData.entrySet())
			{
				//System.out.println(entry.getKey()+ " -- "+entry.getValue());
			}*/
			//HashMap<String, Object> currencyData=new HashMap<String, Object>();//added for testing
			
			if(currencyData.isEmpty()){
				System.out.println("=======================Mail Should Trigger===============");
				String strFlag="N";
				communicationManager.updateLiveCurrencydata(currencyData,strFlag);
			}else{
				System.out.println("=======================Mail Should Not Trigger===============");
				String strFlag="Y";
				communicationManager.updateLiveCurrencydata(currencyData,strFlag);
			}
			
			
			//Read from website.......... S T A R T S
			/*Document doc = Jsoup.connect("https://www.xe.com/currencytables/?from=QAR").get();
			Element tableElements = doc.getElementById("historicalRateTbl");
			Elements tableHeaderEles = tableElements.select("thead tr th");
			Elements tableRowElements = tableElements.select(":not(thead) tr");	
			ArrayList<String[]> alCurrency	=	new ArrayList<>();
			String strArr[]					=	null;
			for (int i = 0; i < tableRowElements.size(); i++) {
				Element row = tableRowElements.get(i);
				Elements rowItems = row.select("td");
				strArr	=	new String[4];

				for (int j = 0; j < rowItems.size(); j++)
					strArr[j]	=	rowItems.get(j).text();
				
				alCurrency.add(strArr);
			}
			communicationManager.updateLiveCurrencydata(alCurrency);*/
			//Read from website.......... E N D S

			log.info("Inside run method in CurrencyConvScheduler Ends");
			
		}catch(Exception e){
			//Thread.sleep(10000);
			TTKCommon.logStackTrace(e);
		}//end of catch(Exception e)
	}
	
	/**
	 * Returns the PreAuthSupportManager session object for invoking methods on it.
	 * @return PreAuthSupportManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private CommunicationManager getCommunicationManagerObject() throws TTKException
	{
		CommunicationManager communicationManager = null;
		try
		{
			if(communicationManager == null)
			{
				InitialContext ctx = new InitialContext();
				communicationManager = (CommunicationManager) ctx.lookup("java:global/TTKServices/business.ejb3/CommunicationManagerBean!com.ttk.business.common.messageservices.CommunicationManager");
			}//end of if(preAuthSupportManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strCurrencyConvScheduler);
		}//end of catch
		return communicationManager;
	}//end getCommunicationManagerObject()

}//end of CurrencyConvScheduler
