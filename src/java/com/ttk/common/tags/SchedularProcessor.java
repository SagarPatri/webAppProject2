/**
 * @ (#) SchedularProcessor.java Nov 20, 2007
 * Project       : TTK HealthCare Services
 * File          : SchedularProcessor.java
 * Author        : Raghavendra T M
 * Company       : Span Systems Corporation
 * Date Created  : Nov 20, 2007
 *
 * @author       : Raghavendra T M
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.common.tags;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.ttk.common.BulkUploadClaimsReptEmailScheduler;
import com.ttk.common.CurrencyConvScheduler;
import com.ttk.common.CustomerCallBackSchedular;
import com.ttk.common.CustomerEscalationSchedular;
import com.ttk.common.DHPOGNNewTransaction;
import com.ttk.common.DHPOGlobalnetClaimBifurcation;
import com.ttk.common.DHPOVHNewTransaction;
import com.ttk.common.EmailScheduler;
import com.ttk.common.EmailSchedulerPreauth;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.SMSScheduler;
import com.ttk.common.SMSSchedulerPreauth;
//AS PER koc 1179 REQUEST

public class SchedularProcessor {
	
	private static final Logger log = Logger.getLogger( SchedularProcessor.class );
	public void process() throws SchedulerException, ParseException
	{
	log.info("Schedular started");    
		try{
			
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			
			JobDetail jobDetailEmailPreauth = new JobDetail("EmailJOBPREAUTH", Scheduler.DEFAULT_GROUP, EmailSchedulerPreauth.class);
			JobDetail jobDetailEmail = new JobDetail("EmailJOB", Scheduler.DEFAULT_GROUP, EmailScheduler.class);
			JobDetail jobDetailSMS = new JobDetail("SMSJOB", Scheduler.DEFAULT_GROUP, SMSScheduler.class);
			JobDetail jobDetailSMSPreauth = new JobDetail("SMSJOBPREAUTH", Scheduler.DEFAULT_GROUP, SMSSchedulerPreauth.class);//sms preauth schedular
			JobDetail jobDetailCurrencyConv = new JobDetail("CURRENCYCONVJOB", Scheduler.DEFAULT_GROUP, CurrencyConvScheduler.class);//Currency converter scheduler
			//JobDetail jobCustomerCallBack = new JobDetail("CustomerCallBackJOB",Scheduler.DEFAULT_GROUP,CustomerCallBackSchedular.class);//intX CustomerCare
			//JobDetail jobCustomerEscalation = new JobDetail("CustomerEscalationJOB",Scheduler.DEFAULT_GROUP,CustomerEscalationSchedular.class);//intX CustomerCare Escalation			
			
			
			String strEmailCronStringPreauth = TTKPropertiesReader.getPropertyValue("EmailCronStringPreauth");//only preauthSchedular
			String strEmailCronString = TTKPropertiesReader.getPropertyValue("EmailCronString");
			String strSMSCronStringPreauth = TTKPropertiesReader.getPropertyValue("SMSCronStringPreauth");//sms preauth schedular
			String strSMSCronString = TTKPropertiesReader.getPropertyValue("SMSCronString");	
			String strCurrencyConvCronString = TTKPropertiesReader.getPropertyValue("CurrencyConvCronString");	
			//String strCustomerCallBack	 = TTKPropertiesReader.getPropertyValue("CustomerCallBackCornString");//intX CustomerCare
			//String strCustomerEscalation	 = TTKPropertiesReader.getPropertyValue("CustomerEscalationCornString");//intX CustomerCare Escalation

			
			
			
			CronTrigger cronTrigEmailPreauth = new CronTrigger("crontriggeremailPreauth", "crongroupemailPreauth", strEmailCronStringPreauth);//only preauthSchedular
			CronTrigger cronTrigEmail = new CronTrigger("crontriggeremail", "crongroupemail", strEmailCronString);
			CronTrigger cronTrigSMSPreauth = new CronTrigger("crontriggersmsPreauth", "crongroupsmsPreauth", strSMSCronStringPreauth);//sms preauth schedular
			CronTrigger cronTrigSMS = new CronTrigger("crontriggersms", "crongroupsms", strSMSCronString);
			CronTrigger cronTrigCurrencyConv = new CronTrigger("crontriggercurrencyconv", "crongroupcurrencyconv", strCurrencyConvCronString);
			//CronTrigger cronstrCustomerCallBack = new CronTrigger("crontriggercustomercallback","crontriggercustomercallback",strCustomerCallBack);//intX CustomerCare
			//CronTrigger cronstrCustomerEscalation = new CronTrigger("crontriggercustomerescalation","crontriggercustomerescalation",strCustomerEscalation);//intX CustomerCare Escalation
				
			
			scheduler.scheduleJob(jobDetailEmail, cronTrigEmail );
			scheduler.scheduleJob(jobDetailEmailPreauth,cronTrigEmailPreauth);  
			scheduler.scheduleJob(jobDetailSMSPreauth, cronTrigSMSPreauth );//sms preauth schedular
			scheduler.scheduleJob(jobDetailSMS, cronTrigSMS );
			scheduler.scheduleJob(jobDetailCurrencyConv, cronTrigCurrencyConv );
			//scheduler.scheduleJob(jobCustomerCallBack,cronstrCustomerCallBack );//intX CustomerCare
			//scheduler.scheduleJob(jobCustomerEscalation,cronstrCustomerEscalation );//intX CustomerCare Escalation	
			
			
			// added for sending claims Excle BulkUpload auto scheduler Mail report to support team.
			JobDetail jobBulkClaimsUploadReport=new JobDetail("bulkClaimsUploadReport",scheduler.DEFAULT_GROUP,BulkUploadClaimsReptEmailScheduler.class);
			String strBulkClaimsUploadReport	 = TTKPropertiesReader.getPropertyValue("BulkClaimsUploadRpt");
			CronTrigger cronstrjobHaadMember=new CronTrigger("strBulkClaimsUploadReport","strBulkClaimsUploadReport", strBulkClaimsUploadReport);
			scheduler.scheduleJob(jobBulkClaimsUploadReport,cronstrjobHaadMember);
			
			// Start the Scheduler running
			scheduler.start();
			log.info("done");    
			}//end of try
		catch(SchedulerException sExp)
		{
			sExp.printStackTrace();
		}//end of catch(SchedulerException sExp)
		/*catch(ParseException pExp)
		{
			pExp.printStackTrace();
		}*///end of catch(ParseException pExp)
		catch(Exception exp)
		{
			exp.printStackTrace();
		}//end of catch(ParseException exp)
	}//end of process()
}//end of SchedularProcessor


