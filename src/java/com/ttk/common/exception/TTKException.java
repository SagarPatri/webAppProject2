/**
 * @ (#) TTKException.java July 29, 2005
 * Project      :
 * File         : TTKException.java
 * Author       : Nagaraj D V
 * Company      :
 * Date Created : July 29, 2005
 *
 * @author       :  Nagaraj D V
 * Modified by   :
 * Modified date :
 * Reason        :
 */
 
package com.ttk.common.exception;

import gnu.inet.ftp.ServerResponseException;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.jms.JMSException;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import jxl.JXLException;
import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;

/**
 * This class is a wrapper class for all the exceptions thrown in the TTK web application.
 * It retrieves the actual exception object and processes it to set the appropriate error message key/code.
 */
public class TTKException extends Exception{

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 8802223732478084001L;

	private static Logger log = Logger.getLogger(TTKException.class );
    protected Throwable rootCause = null;
    private String strMessage = null;
    private String strIdentfier = null;
    private String dynamicErrorDesc = "";

    public TTKException()
    {
        super();
    }//end of constructor TTKException()

    /**
     * Sets the message code based on the appropriate flow.
     *
     * @param rootCause Throwable the throwable object which contains the actual exception
     * @param strIdentifier String an indicator to identify the current flow
     */
    public TTKException(Throwable rootCause, String strIdentifier)
    {
        log.debug("Inside TTKException....");
        this.rootCause = rootCause;
        this.setIdentfier(strIdentifier);
        rootCause.printStackTrace();
        
        //get the appropriate exception and build the error keys
        //if any BatchUpdateException comes,it should be handled in this block.
        if(rootCause instanceof BatchUpdateException)
        {
            SQLException sqlExp = (SQLException)rootCause;
           // System.out.println("sqlExp...getMessage0:::"+sqlExp.getMessage());
           if(sqlExp.getMessage().contains("ORA-20054"))//added for firstyrVacc
            {
                this.setMessage("error.claims.settlement.save");
            }//end of added for firstyrVacc
            else if(sqlExp.getMessage().contains("ORA-20055"))
            {
                this.setMessage("error.endorsement.members.edit");
            }//end of else if(sqlExp.getMessage().contains("ORA-20055"))
            else if(sqlExp.getMessage().contains("ORA-20059"))
            {
                this.setMessage("error.endorsement.members.adddelete");
            }//end of else if(sqlExp.getMessage().contains("ORA-20059"))
            else if(sqlExp.getMessage().contains("ORA-20066"))
            {
                this.setMessage("error.endorsement.member.addpremium");
            }//end of else if(sqlExp.getMessage().contains("ORA-20066"))
            else if(sqlExp.getMessage().contains("ORA-20076"))
            {
                this.setMessage("error.endorsement.members.adddelete");
            }//end of if(sqlExp.getMessage().contains("ORA-20076"))
            else if(sqlExp.getMessage().contains("ORA-20087"))
            {
                this.setMessage("error.endorsement.policydetails.policycancelled");
            }//end of else if(sqlExp.getMessage().contains("ORA-20087"))
            else if(sqlExp.getMessage().contains("ORA-20107"))
            {
                this.setMessage("error.preauth.preauthdetails.review");
            }//end of else if(sqlExp.getMessage().contains("ORA-20107"))
            else if(sqlExp.getMessage().contains("ORA-20108"))
            {
                this.setMessage("error.preauth.assignto.reassign");
            }//end of else if(sqlExp.getMessage().contains("ORA-20108"))
            else if(sqlExp.getMessage().contains("ORA-20152"))
            {
                this.setMessage("error.claims.claimsdetails.reviewdone");
            }//end of else if(sqlExp.getMessage().contains("ORA-20152"))
            else if(sqlExp.getMessage().contains("ORA-20256"))
            {
                this.setMessage("error.floataccount.debitnote.deposit");
            }//end of else if(sqlExp.getMessage().contains("ORA-20256"))
            else if(sqlExp.getMessage().contains("ORA-20530"))
            {
                this.setMessage("error.webservice.policydomicilaryamount");
            }//end of else if(sqlExp.getMessage().contains("ORA-20530"))
            else if(sqlExp.getMessage().contains("ORA-20541"))
            {
                this.setMessage("error.enrollment.domiciliary.domiciliarytype");
            }//end of else if(sqlExp.getMessage().contains("ORA-20541"))
            else if(sqlExp.getMessage().contains("ORA-20719"))
            {
                this.setMessage("error.enrollment.policydetails.suminsuredpremium");
            }//end of else if(sqlExp.getMessage().contains("ORA-20719"))
            else if(sqlExp.getMessage().contains("ORA-20720"))
            {
                this.setMessage("error.enrollment.policydetails.premiumgreaterthanplan");
            }//end of else if(sqlExp.getMessage().contains("ORA-20720"))
            else if(sqlExp.getMessage().contains("ORA-20734"))
            {
                this.setMessage("error.administration.tds.revisiondate");
            }//end of else if(sqlExp.getMessage().contains("ORA-20734"))
            else if(sqlExp.getMessage().contains("ORA-20806"))
            {
            	this.setMessage("error.administration.usermanagement.ttkuser.activation");
            }//end of  else if(sqlExp.getMessage().contains("ORA-20806"))
            else if(sqlExp.getMessage().contains("ORA-20808"))
            {
            	this.setMessage("error.administration.usermanagement.ttkuser.inactivation");
            }//end of  else if(sqlExp.getMessage().contains("ORA-20808"))
			else if(sqlExp.getMessage().contains("ORA-20863"))
			{
			    	 this.setMessage("error.preauth.claims.modification.not.allowed");
			}
			else if(sqlExp.getMessage().contains("ORA-20865"))
	    	{
		    	 this.setMessage("error.preauth.claims.overide.inscompany.not.allowed");
		    }
            if(sqlExp.getMessage().contains("ORA-20866"))//added for firstyrVacc
            {
                this.setMessage("error.preauth.claims.overide.inscompany.notallowed");
            }//end of added for firstyrVacc
            if(sqlExp.getMessage().contains("ORA-20869"))//added for intx
            {
                this.setMessage("error.intx.Online.provider.contacts");
            }//end of added for firstyrVacc
            if(sqlExp.getMessage().contains("ORA-20869"))//added for intx
            {
                this.setMessage("error.intx.Online.provider.contacts");
            }//end of added for firstyrVacc
            else if(sqlExp.getMessage().contains("ORA-20380"))
				  this.setMessage("add.atleast.one.diagnosis");
            else if(sqlExp.getErrorCode()==20400)
				  this.setMessage("error.duplicate.networkCodeOrOrder"); 
            else if(sqlExp.getMessage().contains("ORA-00001"))
            	 this.setMessage("error.primaryKeyViolated"); 
            else if(sqlExp.getMessage().contains("ORA-20012"))						
                this.setMessage("error.usermanagement.login.partner.userinvalid");
            
			else if(sqlExp.getMessage().contains("ORA-20275")) 
				  this.setMessage("error.pricing.lives.notmatching");  
			else if(sqlExp.getMessage().contains("ORA-20276")) 
				  this.setMessage("error.pricing.total.lives.notmatcing");  
			else if(sqlExp.getMessage().contains("ORA-20277")) 
				  this.setMessage("error.pricing.maternity.notmatcing");
			else if(sqlExp.getMessage().contains("ORA-20278")) 
				  this.setMessage("error.pricing.maternity.notmatcing");
			else if(sqlExp.getMessage().contains("ORA-20323"))
				 this.setMessage("error.ailment.dentalBenefitType");
			else if(sqlExp.getMessage().contains("ORA-20374"))
				this.setMessage("diagnosis.code.already.exists");
			else if(sqlExp.getMessage().contains("ORA-20943")) 
				  this.setMessage("error.event.quotation.generate");
			else if(sqlExp.getMessage().contains("ORA-20274"))   
				 this.setMessage("error.activity.date.expired");
			
            
			else if(sqlExp.getMessage().contains("ORA-20837"))
				this.setMessage("error.pricing.inputscree2.maternitymismatch");		
			else if(sqlExp.getMessage().contains("ORA-20838"))
				this.setMessage("error.employee.login.incorrectdate");	
			else
                this.setMessage("error.database");
        }//end of if(rootCause instanceof BatchUpdateException)
        else if(rootCause instanceof SQLException)
        {
            SQLException sqlExp = (SQLException)rootCause;
            //System.out.println("sqlExp...getMessage1:::"+sqlExp.getMessage());
            if(sqlExp.getMessage().contains("ORA-20444"))						
                this.setMessage("error.admin.product.general");
            else  if(sqlExp.getMessage().contains("ORA-20207"))
            	this.setMessage("error.claim.general.curencyrate");
            else if(sqlExp.getMessage().contains("ORA-00001"))						
                this.setMessage("error.rootcause.claim.1stsave");
          
            else if(sqlExp.getMessage().contains("ORA-20579"))						
                this.setMessage("error.inwardEntry.enrollment.batchscheme");
            else if(sqlExp.getMessage().contains("ORA-20361"))							// bank name error code			
                this.setMessage("error.wrong.bankname");
            else if(sqlExp.getMessage().contains("ORA-20397"))							// bank name error code			
                this.setMessage("error.preauth.claimCompleted.override");
            else if(sqlExp.getMessage().contains("ORA-20959"))
                this.setMessage("error.empanelment.tariff.dateerror");
            
            else if(sqlExp.getMessage().contains("ORA-20961"))							// bank name error code			
                this.setMessage("error.pre-auth.enhance.existing.preauth");
            else if(sqlExp.getMessage().contains("ORA-20962"))							// bank name error code			
                this.setMessage("error.principle.inception.date");
            else if(sqlExp.getMessage().contains("ORA-20964"))							// bank name error code			
                this.setMessage("error.employee.canceled.person.updation.error");
            else if(sqlExp.getMessage().contains("ORA-20960"))							// bank name error code			
                this.setMessage("error.empanelment.tariff.invalidactcode");        
            else if(sqlExp.getErrorCode()==1)
                this.setMessage(this.getIdentfier()+".add.duplicate");
            else if(sqlExp.getErrorCode()==1400)
                this.setMessage("error.empty");
            else if(sqlExp.getErrorCode()==1401)
                this.setMessage("error.huge");
            else if(sqlExp.getErrorCode()==20610)
                this.setMessage("error.onlineenrollment.savedependent");
            else if(sqlExp.getErrorCode()==1722)
                this.setMessage("error.validnumber");
            else if(sqlExp.getErrorCode()==2292)
                this.setMessage("error.references");
            //  added for hyundai buffer 
            else if(sqlExp.getErrorCode()==20001)
				this.setMessage("error.invalid.emirateid");     
	        //end  added for hyundai buffer     
            else if(sqlExp.getErrorCode()==20002)
                this.setMessage("error.hospital.status.contact");
            else if(sqlExp.getErrorCode()==20003)
                this.setMessage("error.tariff.plan.baserevise");
            else if(sqlExp.getErrorCode()==20004)
                this.setMessage("error.hospital.status.validation");
            else if(sqlExp.getErrorCode()==20005)
                this.setMessage("error.hospital.status.changestatus");
            else if(sqlExp.getErrorCode()==20006)
                this.setMessage("error.hospital.status.payorder");
            else if(sqlExp.getErrorCode()==20007)
                this.setMessage("error.hospital.status.renew");
            else if(sqlExp.getErrorCode()==20008)
                this.setMessage("error.hospital.status.empanel");
            else if(sqlExp.getErrorCode()==20009)
                this.setMessage("error.hospital.status.disempanel");
            else if(sqlExp.getErrorCode()==20010)
                this.setMessage("error.hospital.status.expired");
            else if(sqlExp.getErrorCode()==20011)
                this.setMessage("error.hospital.grading.general");
            else if(sqlExp.getErrorCode()==20013)
                this.setMessage("error.hospital.status.closed");
            else if(sqlExp.getErrorCode()==20014)
                this.setMessage("error.hospital.status.mou");
            else if(sqlExp.getErrorCode()==20015)
                this.setMessage("error.hospital.status.account");
            else if(sqlExp.getErrorCode()==20016)
                this.setMessage("error.hospital.validation.stopempanel");
            else if(sqlExp.getErrorCode()==20017)
                this.setMessage("error.admin.hospital.associated");
            else if(sqlExp.getErrorCode()==20018)
                this.setMessage("error.empanelment.insurance.empdate");
            else if(sqlExp.getErrorCode()==20019)
                this.setMessage("error.tariff.plan.delete");
            else if(sqlExp.getErrorCode()==20020)
                this.setMessage("error.tariff.plan.associate");
            else if(sqlExp.getErrorCode()==20021)
                this.setMessage("error.tariff.plan.revise");
            else if(sqlExp.getErrorCode()==20022)
                this.setMessage("error.empanelment.insurance.abbreviation");
            else if(sqlExp.getErrorCode()==20023)
                this.setMessage("error.empanelment.validation.empaneldate");
            else if(sqlExp.getErrorCode()==20024)
                this.setMessage("error.hospital.validation.validateddate");
            else if(sqlExp.getErrorCode()==20025)
                this.setMessage("error.hospital.validation.markeddate");
            else if(sqlExp.getErrorCode()==20026)
                this.setMessage("error.insurance.productdetail.startdate");
            else if(sqlExp.getErrorCode()==20027)
                this.setMessage("error.hospital.tariff.baserevision1");
            else if(sqlExp.getErrorCode()==20028)
                this.setMessage("error.hospital.tariff.baserevision2");
            else if(sqlExp.getErrorCode()==20029)
                this.setMessage("error.overlap");
            else if(sqlExp.getErrorCode()==20030)
                this.setMessage("error.suspension");
            else if(sqlExp.getErrorCode()==20031)
                this.setMessage("error.usermanagement.role.changeusertype");
            else if(sqlExp.getErrorCode()==20032)
                this.setMessage("error.usermanagement.usergroups.defaultgroup");
            else if(sqlExp.getErrorCode()==20033)
                this.setMessage("error.enrollment.policydetails.review");
            else if(sqlExp.getErrorCode()==20034)
                this.setMessage("error.enrollment.members.relationship");
            else if(sqlExp.getErrorCode()==20035)
                this.setMessage("error.enrollment.policydetails.finalconfirm");
            else if(sqlExp.getErrorCode()==20036)
                this.setMessage("error.endorsement");
            else if(sqlExp.getErrorCode()==20037)
                this.setMessage("error.endorsement.completed");
            else if(sqlExp.getErrorCode()==20038)
                this.setMessage("error.inwardentry.batchpolicy.completed");
            else if(sqlExp.getErrorCode()==20039)
                this.setMessage("error.enrollment.policydetails.period");
            else if(sqlExp.getErrorCode()==20040)
                this.setMessage("error.enrollment.policydetails.policystartdate");
            else if(sqlExp.getErrorCode()==20041)
                this.setMessage("error.enrollment.policydetails.policysubtype");
            else if(sqlExp.getErrorCode()==20042)
                this.setMessage("error.enrollment.policydetails.details");
            else if(sqlExp.getErrorCode()==20043)
                this.setMessage("error.inwardentry.batchpolicies.policytype");
            else if(sqlExp.getErrorCode()==20044)
                this.setMessage("error.enrollment.policydetails.reviewmemberinfo");
            else if(sqlExp.getErrorCode()==20045)
                this.setMessage("error.endorsement.policydetails.endorsementnotcomplete");
            else if(sqlExp.getErrorCode()==20046)
                this.setMessage("error.ttkoffice.report");
            else if(sqlExp.getErrorCode()==20047)
                this.setMessage("error.ttkoffice.reportto");
            else if(sqlExp.getErrorCode()==20048)
                this.setMessage("error.usermanagement.login.userinvalid");
            else if(sqlExp.getErrorCode()==20049)
                this.setMessage("error.usermanagement.login.userinactive");
            else if(sqlExp.getErrorCode()==20050)
                this.setMessage("error.inwardentry.batchComplete.delete");
            else if(sqlExp.getErrorCode()==20051)
                this.setMessage("error.inwardentry.batchpolicy.uncompleted");
            else if(sqlExp.getErrorCode()==20052)
                this.setMessage("error.inwardentry.policydetails.endorsement");
            else if(sqlExp.getErrorCode()==20053)
                this.setMessage("error.inwardentry.policydetails.policy");
            else if(sqlExp.getErrorCode()==20054)
                this.setMessage("error.endorsement.members.add");
            else if(sqlExp.getErrorCode()==20055)
                this.setMessage("error.endorsement.members.edit");
            else if(sqlExp.getErrorCode()==20056)
                this.setMessage("error.endorsement.members.delete");
            else if(sqlExp.getErrorCode()==20057)
                this.setMessage("error.endorsement.members.deletelimit");
            else if(sqlExp.getErrorCode()==20058)
                this.setMessage("error.inwardentry.policydetails.policyinfo");
            else if(sqlExp.getErrorCode()==20059)
                this.setMessage("error.endorsement.members.adddelete");
            else if(sqlExp.getErrorCode()==20060)
                this.setMessage("error.inwardentry.policydetails.policytype");
            else if(sqlExp.getErrorCode()==20061)
                this.setMessage("error.endorsement.endorsement.keyfieldchange");
            else if(sqlExp.getErrorCode()==20062)
                this.setMessage("error.endorsement.policydetails.keyfieldchange");
            else if(sqlExp.getErrorCode()==20064)
                this.setMessage("error.enrollment.search.delete");
            else if(sqlExp.getErrorCode()==20065)
                this.setMessage("error.endorsement.search.delete");
            else if(sqlExp.getErrorCode()==20066)
                this.setMessage("error.endorsement.member.addpremium");
            else if(sqlExp.getErrorCode()==20067)
                this.setMessage("error.endorsement.member.addmember");
            else if(sqlExp.getErrorCode()==20068)
                this.setMessage("error.endorsement.search.deletepartialendorsement");
            else if(sqlExp.getErrorCode()==20069)
                this.setMessage("error.enrollment.policydetails.productname");
            else if(sqlExp.getErrorCode()==20070)
                this.setMessage("error.enrollment.policydetails.policystartdateproduct");
            else if(sqlExp.getErrorCode()==20071)
                this.setMessage("error.insurance.productdetail.productenddate");
            else if(sqlExp.getErrorCode()==20072)
                this.setMessage("error.insurance.productdetail.productalreadyassociated");
            else if(sqlExp.getErrorCode()==20073)
                this.setMessage("error.administration.productdetail.productassociated");
            else if(sqlExp.getErrorCode()==20074)
                this.setMessage("error.administration.productdetail.producthonoured");
            else if(sqlExp.getErrorCode()==20075)
                this.setMessage("error.enrollment.policydetails.policyenddate");
            else if(sqlExp.getErrorCode()==20076)
                this.setMessage("error.endorsement.members.adddelete");
            else if(sqlExp.getErrorCode()==20077)
                this.setMessage("error.endorsement.member.dateofinception");
            else if(sqlExp.getErrorCode()==20078)
                this.setMessage("error.myprofile.changepassword.oldpasswordmismatch");
            else if(sqlExp.getErrorCode()==20079)
                this.setMessage("error.myprofile.changepassword.identicalpasswords");
            else if(sqlExp.getErrorCode()==20080)
                this.setMessage("error.enrollment.group.change");
            else if(sqlExp.getErrorCode()==20081)
                this.setMessage("error.inwardentry.batchdetails.changeinsurancecompnay");
            else if(sqlExp.getErrorCode()==20082)
                this.setMessage("error.insurance.productdetail.productstartdate");
            else if(sqlExp.getErrorCode()==20083)
                this.setMessage("error.enrollment.members.suspension");
            else if(sqlExp.getErrorCode()==20084)
                this.setMessage("error.enrollment.policydetail.termstatuschange");
            else if(sqlExp.getErrorCode()==20085)
                this.setMessage("error.enrollment.members.ped");
            else if(sqlExp.getErrorCode()==20086)
                this.setMessage("error.enrollment.policydetails.finalconfirmpremium");
            else if(sqlExp.getErrorCode()==20087)
                this.setMessage("error.endorsement.policydetails.policycancelled");
            else if(sqlExp.getErrorCode()==20088)
                this.setMessage("error.empanelment.groupregistration.changegrouptype");
            else if(sqlExp.getErrorCode()==20089)
                this.setMessage("error.endorsement.endorsement.review");
            else if(sqlExp.getErrorCode()==20090)
                this.setMessage("error.enrollment.members.relationshipallow");
            else if(sqlExp.getErrorCode()==20091)
                this.setMessage("error.administration.policies.buffer");
            else if(sqlExp.getErrorCode()==20092)
                this.setMessage("error.webservice.invalidrelation");
            else if(sqlExp.getErrorCode()==20093)
                this.setMessage("error.webservice.policyexists");
            else if(sqlExp.getErrorCode()==20094)
                this.setMessage("error.webservice.membernotexists");
            else if(sqlExp.getErrorCode()==20095)
                this.setMessage("error.webservice.memberrenewed");
            else if(sqlExp.getErrorCode()==20096)
                this.setMessage("error.webservice.corpolicynotexitsts");
            else if(sqlExp.getErrorCode()==20097)
                this.setMessage("error.administration.policies.buffer.allocation");
            else if(sqlExp.getErrorCode()==20098)
                this.setMessage("error.webservice.duplicatemember");
            else if(sqlExp.getErrorCode()==20099)
                this.setMessage("error.webservice.policyinotherflow");
            else if(sqlExp.getErrorCode()==20100)
                this.setMessage("error.preauth.medical.primaryailment");
            else if(sqlExp.getErrorCode()==20101)
                this.setMessage("error.preauth.medical.icdpcsailment");
            else if(sqlExp.getErrorCode()==20102)
                this.setMessage("error.preauth.medical.peddelete");
            else if(sqlExp.getErrorCode()==20103)
                this.setMessage("error.preauth.preauthdetails.duplicatepreauth");
            else if(sqlExp.getErrorCode()==20104)
                this.setMessage("error.preauth.authorization.save");
            else if(sqlExp.getErrorCode()==20105)
                this.setMessage("error.preauth.preauthdetails.approve");
            else if(sqlExp.getErrorCode()==20106)
                this.setMessage("error.preauth.preauthdetails.closeproximity");
            else if(sqlExp.getErrorCode()==20107)
                this.setMessage("error.preauth.preauthdetails.review");
            else if(sqlExp.getErrorCode()==20108)
                this.setMessage("error.preauth.assignto.reassign");
            else if(sqlExp.getErrorCode()==20109)
                this.setMessage("error.preauth.assignto.assign");
            else if(sqlExp.getErrorCode()==20110)
                this.setMessage("error.preauth.policyopt");
            else if(sqlExp.getErrorCode()==20111)
                this.setMessage("error.preauth.supportdoc.deletebuffer");
            else if(sqlExp.getErrorCode()==20112)
                this.setMessage("error.preauth.preauthdetails.discrepancy");
            else if(sqlExp.getErrorCode()==20113)
                this.setMessage("error.preauth.authorization.discrepancy");
            else if(sqlExp.getErrorCode()==20114)
                this.setMessage("error.preauth.authorization.shortfall");
            else if(sqlExp.getErrorCode()==20115)
                this.setMessage("error.preauth.authorization.investigation");
            else if(sqlExp.getErrorCode()==20116)
                this.setMessage("error.preauth.tariff.ailment");
            else if(sqlExp.getErrorCode()==20118)
                this.setMessage("error.preauth.preauthdetails.authorise");
            else if(sqlExp.getErrorCode()==20119)
                this.setMessage("error.preauth.preauthdetails.policytype");
            else if(sqlExp.getErrorCode()==20121)
                this.setMessage("error.preauth.shortfalls.delete");
            else if(sqlExp.getErrorCode()==20122)
                this.setMessage("error.endorsement.members.cancel");
            else if(sqlExp.getErrorCode()==20123)
                this.setMessage("error.preauth.authorization.approvallimit");
            else if(sqlExp.getErrorCode()==20124)
                this.setMessage("error.preauth.medical.employeeno");
            else if(sqlExp.getErrorCode()==20125)
                this.setMessage("error.preauth.details.rcvdate");
            else if(sqlExp.getErrorCode()==20126)
                this.setMessage("error.preauth.authorization.reject");
            else if(sqlExp.getErrorCode()==20127)
                this.setMessage("error.preauth.authorization.cancelmember");
            else if(sqlExp.getErrorCode()==20128)
                this.setMessage("error.preauth.authorization.reapprove");
            else if(sqlExp.getErrorCode()==20129)
                this.setMessage("error.preauth.preauthdetails.manualPA");
            else if(sqlExp.getErrorCode()==20130)
                this.setMessage("error.preauth.preauthdetails.associatememberinfo");
            else if(sqlExp.getErrorCode()==20131)
                this.setMessage("error.claims.settlement.endorsepolicy");
            else if(sqlExp.getErrorCode()==20132)
                this.setMessage("error.preauth.manual.exists");
            else if(sqlExp.getErrorCode()==20133)
                this.setMessage("error.preauth.shortfall.reminder");
            else if(sqlExp.getErrorCode()==20134)
                this.setMessage("error.claims.dischargevoucher.delete");
            //  added for hyundai buffer 
            else if(sqlExp.getErrorCode()==20135)
				this.setMessage("error.preauth.promote.buffer");     
	        //end  added for hyundai buffer  
          //added for hyundai buffer
            else if(sqlExp.getErrorCode()==20136)
                this.setMessage("error.claims.preauth.lessAmount");
            //end added for hyundai buffer
            else if(sqlExp.getErrorCode()==20137)
                this.setMessage("error.preauth.authorization.accountheadbreakup");
            else if(sqlExp.getErrorCode()==20138)
                this.setMessage("error.claims.claimdetails.paclaimprevent");
            else if(sqlExp.getErrorCode()==20139)
                this.setMessage("error.claims.settlement.membersuspended");
            else if(sqlExp.getErrorCode()==20140)
                this.setMessage("error.claims.settlement.policyperiod");
            else if(sqlExp.getErrorCode()==20141)
                this.setMessage("error.claims.settlement.associateenrollmentid");
            else if(sqlExp.getErrorCode()==20142)
                this.setMessage("error.claims.settlement.policynbrchange");
            /*else if(sqlExp.getErrorCode()==20142)
                this.setMessage("error.preauth.bufferdetails.buffermodification");*/
            else if(sqlExp.getErrorCode()==20143)
                this.setMessage("error.claims.medical.nonpackage");
            else if(sqlExp.getErrorCode()==20144)
                this.setMessage("error.preauth.preauthdetails.associateenrollmentid");
            else if(sqlExp.getErrorCode()==20145)
                this.setMessage("error.administration.buffer.utilizedamount");
            else if(sqlExp.getErrorCode()==20146)
                this.setMessage("error.preauth.preauthdetails.medical");
            else if(sqlExp.getErrorCode()==20147)
                this.setMessage("error.preauth.preauthdetails.coding");
            else if(sqlExp.getErrorCode()==20148)
                this.setMessage("error.preauth.rulevalidation");
            //added for hyundai Buffer
            else if(sqlExp.getErrorCode()==20149)
                this.setMessage("error.Buffer.rulevalidation");
          //end added for hyundai Buffer
           else if(sqlExp.getErrorCode()==20150)
                this.setMessage("error.inwardentry.claimsdetails.shortfall");
            else if(sqlExp.getErrorCode()==20151)
                this.setMessage("error.inwardentry.claimsdetails.claim");
            else if(sqlExp.getErrorCode()==20152)
                this.setMessage("error.claims.claimsdetails.review");
            else if(sqlExp.getErrorCode()==20153)
                this.setMessage("error.inwardentry.claimsdetails.complete");
            else if(sqlExp.getErrorCode()==20154)
                this.setMessage("error.inwardentry.search.delete");
            else if(sqlExp.getErrorCode()==20155)
                this.setMessage("error.claims.search.delete");
            else if(sqlExp.getErrorCode()==20156)
                this.setMessage("error.claims.bills.delete");
            else if(sqlExp.getErrorCode()==20157)
                this.setMessage("error.preauth.medical.package");
            else if(sqlExp.getErrorCode()==20158)
                this.setMessage("error.claims.claimsdetails.settlement");
            else if(sqlExp.getErrorCode()==20159)
                this.setMessage("error.claims.dischargevoucher.settlement");
            else if(sqlExp.getErrorCode()==20160)
                this.setMessage("error.inwardentry.claimsdetails.ammendment");
            else if(sqlExp.getErrorCode()==20161)
                this.setMessage("error.inwardentry.claimsdetails.ammendmentbill");
            else if(sqlExp.getErrorCode()==20162)
                this.setMessage("error.claims.claimsdetails.duplicateclaim");
            else if(sqlExp.getErrorCode()==20163)
                this.setMessage("error.claims.claimsdetails.preauth");
            else if(sqlExp.getErrorCode()==20164)
                this.setMessage("error.claims.claimsdetails.enrollmentid");
            else if(sqlExp.getErrorCode()==20165)
                this.setMessage("error.claims.bills.billdate");
            else if(sqlExp.getErrorCode()==20166)
                this.setMessage("error.claims.settlement.cancelmember");
            else if(sqlExp.getErrorCode()==20167)
                this.setMessage("error.claims.settlement.reapprove");
            else if(sqlExp.getErrorCode()==20168)
                this.setMessage("error.claims.settlement.ammendment");
            else if(sqlExp.getErrorCode()==20169)
                this.setMessage("error.claims.settlement.approvedPALimit");
            else if(sqlExp.getErrorCode()==20170)
                this.setMessage("error.enrollment.member.reducedsuminsured");
            else if(sqlExp.getErrorCode()==20171)
                this.setMessage("error.preauth.authorization.admissiondate");
            else if(sqlExp.getErrorCode()==20172)
                this.setMessage("error.preauth.authorization.admissiondate");
            else if(sqlExp.getErrorCode()==20173)
                this.setMessage("error.claims.settlement.admissiondate");
            else if(sqlExp.getErrorCode()==20174)
                this.setMessage("error.claims.settlement.approvedamt");
            else if(sqlExp.getErrorCode()==20175)
            	this.setMessage("error.claims.settlement.billsnotentered");
            else if(sqlExp.getErrorCode()==20176)
            	this.setMessage("error.claims.settlement.enterhospitaladdress");
            else if(sqlExp.getErrorCode()==20177)
            	this.setMessage("error.claims.settlement.correctpayeeaddress");
            else if(sqlExp.getErrorCode()==20178)
            	this.setMessage("error.claims.settlement.correctcorporateaddress");
            else if(sqlExp.getErrorCode()==20179)
                this.setMessage("error.inwardentry.claimsdetails.deleteclaims");
            else if(sqlExp.getErrorCode()==20180)
                this.setMessage("error.claims.settlement.reject");
            else if(sqlExp.getErrorCode()==20181)
                this.setMessage("error.claims.dischargevoucher.nhcp");
            /*else if(sqlExp.getErrorCode()==20181)
                this.setMessage("error.enrollment.policydetails.renewpolicydate");*/
            else if(sqlExp.getErrorCode()==20182)
                this.setMessage("error.claims.claimdetails.overridepa");
            else if(sqlExp.getErrorCode()==20183)
                this.setMessage("error.claims.claimdetails.overrideclaim");
            else if(sqlExp.getErrorCode()==20184)
                this.setMessage("error.claims.claimdetails.ammendment");
            else if(sqlExp.getErrorCode()==20185)
                this.setMessage("error.claims.claimdetails.otherclaim");
            else if(sqlExp.getErrorCode()==20186)
                this.setMessage("error.claims.claimdetails.rejectpa");
            else if(sqlExp.getErrorCode()==20187)
                this.setMessage("error.inwardentry.claimdetails.ammendment");
            else if(sqlExp.getErrorCode()==20188)
                this.setMessage("error.preauthclaim.rule.initiatecheck");
            else if(sqlExp.getErrorCode()==20189)
                this.setMessage("error.claims.claimdetails.domicilarylimit");
            else if(sqlExp.getErrorCode()==20190)
                this.setMessage("error.claims.claimdetails.dischargevoucher");
            else if(sqlExp.getErrorCode()==20191)
                this.setMessage("error.claims.medical.ailment");
            else if(sqlExp.getErrorCode()==20193)
                this.setMessage("error.claims.dischargevoucher.notrequired");
            else if(sqlExp.getErrorCode()==20194)
                this.setMessage("error.claims.dischargevoucher.finance");
            else if(sqlExp.getErrorCode()==20195)
                this.setMessage("error.claims.dischargevoucher.status");
            else if(sqlExp.getErrorCode()==20196)
                this.setMessage("error.claims.claimdetails.overridedvdelete");
            else if(sqlExp.getErrorCode()==20197)
                this.setMessage("error.claims.claimdetails.selectmember");
            else if(sqlExp.getErrorCode()==20198)
                this.setMessage("error.claims.claimdetails.selectmemberammendment");
            else if(sqlExp.getErrorCode()==20199)
                this.setMessage("error.claims.claimdetails.selectmemberprevhosp");
            else if(sqlExp.getErrorCode()==20201)
                this.setMessage("error.finance.transaction.withdrawn");
            else if(sqlExp.getErrorCode()==20202)
                this.setMessage("error.finance.bankaccount.changebank");
            else if(sqlExp.getErrorCode()==20203)
                this.setMessage("error.finance.bankaccount.chequeseries");
            else if(sqlExp.getErrorCode()==20204)
                this.setMessage("error.finance.transaction.reverse");
            else if(sqlExp.getErrorCode()==20205)
                this.setMessage("error.finance.floataccount.changeaccount");
            else if(sqlExp.getErrorCode()==20207)
                this.setMessage("error.finance.floataccount.transaction");
            else if(sqlExp.getErrorCode()==20208)
                this.setMessage("error.finance.floataccount.floatbalance");
            else if(sqlExp.getErrorCode()==20209)
                this.setMessage("error.finance.floataccount.minimumbalance");
            else if(sqlExp.getErrorCode()==20210)
                this.setMessage("error.finance.bankaccount.chequenumber");
            else if(sqlExp.getErrorCode()==20211)
                this.setMessage("error.finance.floataccount.Payments");
            else if(sqlExp.getErrorCode()==20212)
                this.setMessage("error.finance.floataccount.chequenumber");
            else if(sqlExp.getErrorCode()==20213)
                this.setMessage("error.finance.floataccount.cheque");
            else if(sqlExp.getErrorCode()==20214)
                this.setMessage("error.finance.account.delete");
            else if(sqlExp.getErrorCode()==20215)
                this.setMessage("error.finance.account.acctnbr");
            else if(sqlExp.getErrorCode()==20217)
                this.setMessage("error.finance.chequeinformation.chequestatus");
            else if(sqlExp.getErrorCode()==20218)
                this.setMessage("error.finance.chequeinformation.reissuedcheque");
            else if(sqlExp.getErrorCode()==20219)
                this.setMessage("error.finance.chequeinformation.voidcheque");
            else if(sqlExp.getErrorCode()==20220)
                this.setMessage("error.finance.chequeinformation.clearedcheque");
            else if(sqlExp.getErrorCode()==20221)
                this.setMessage("error.finance.chequeinformation.stalecheque");
            else if(sqlExp.getErrorCode()==20222)
                this.setMessage("error.finance.bankaccount.search");
            else if(sqlExp.getErrorCode()==20223)
                this.setMessage("error.finance.bankaccount.chequeseries.delete");
            else if(sqlExp.getErrorCode()==20224)
                this.setMessage("error.finance.bankaccount.trandate");
            else if(sqlExp.getErrorCode()==20225)
                this.setMessage("error.finance.floataccount.trandate");
            else if(sqlExp.getErrorCode()==20227)
                this.setMessage("error.finance.floataccount.floatNo");
            else if(sqlExp.getErrorCode()==20228)
                this.setMessage("error.finance.banks.bankslist");
            else if(sqlExp.getErrorCode()==20229)
                this.setMessage("error.finance.floataccount.floatdetail");
            else if(sqlExp.getErrorCode()==20230)
                this.setMessage("error.finance.bankaccount.bankaccountcloseddate");
            else if(sqlExp.getErrorCode()==20231)
                this.setMessage("error.finance.bankaccount.guaranteedetails");
            else if(sqlExp.getErrorCode()==20232)
                this.setMessage("error.finance.chequeinformation.date");
            else if(sqlExp.getErrorCode()==20233)
                this.setMessage("error.finance.bankaccount.issuedChequeUpdate");
            else if(sqlExp.getErrorCode()==20234)
                this.setMessage("error.finance.bankaccount.bankbalanceexists");
            else if(sqlExp.getErrorCode()==20235)
                this.setMessage("error.finance.floataccount.floatbalanceexists");
            else if(sqlExp.getErrorCode()==20236)
                this.setMessage("error.finance.bankaccount.banknegativebalance");
            else if(sqlExp.getErrorCode()==20237)
                this.setMessage("error.finance.floataccount.floatnegativebalance");
            else if(sqlExp.getErrorCode()==20238)
                this.setMessage("error.finance.floataccount.closedbankaccount");
            else if(sqlExp.getErrorCode()==20240)
                this.setMessage("error.finance.floataccount.closeddate");
            else if(sqlExp.getErrorCode()==20241)
                this.setMessage("error.finance.bankaccount.bankname");
            else if(sqlExp.getErrorCode()==20242)
                this.setMessage("error.finance.floataccount.floatacctnumber");
            else if(sqlExp.getErrorCode()==20243)
                this.setMessage("error.finance.authorisedsignatory.duplicateuser");
            else if(sqlExp.getErrorCode()==20244)
                this.setMessage("error.finance.floataccount.paidclaims");
            else if(sqlExp.getErrorCode()==20245)
                this.setMessage("error.finance.floataccount.accountclose");
            else if(sqlExp.getErrorCode()==20247)
                this.setMessage("error.finance.floataccount.paymentadviceupload");
            else if(sqlExp.getErrorCode()==20251)
                this.setMessage("error.webservice.finance.chequeissued");
            else if(sqlExp.getErrorCode()==20252)
                this.setMessage("error.webservice.finance.chequeissuedate");
            else if(sqlExp.getErrorCode()==20253)
                this.setMessage("error.webservice.finance.invalidinfo");
            
            //self fund
            else if(sqlExp.getErrorCode()==20265)
                this.setMessage("error.selffund.validateVidalID");
            else if(sqlExp.getErrorCode()==20266)
                this.setMessage("error.selffund.validateOTPNumber");
            else if(sqlExp.getErrorCode()==20267)
                this.setMessage("error.selffund.OutdatedOTP");
            else if(sqlExp.getErrorCode()==20268)
                this.setMessage("error.selffund.OTPBlocked");
            else if(sqlExp.getErrorCode()==20269)
                this.setMessage("error.selffund.OTPWrong");
            else if(sqlExp.getErrorCode()==20270)
                this.setMessage("error.selffund.noBalance");
            else if(sqlExp.getErrorCode()==20271)
                this.setMessage("error.intx.ActiveVidalId");
            else if(sqlExp.getErrorCode()==20256)
                this.setMessage("error.selffund.validateEnrollID");
            
            
            else if(sqlExp.getErrorCode()==20254)
                this.setMessage("error.floataccount.debitnote.delete");
            else if(sqlExp.getErrorCode()==20255)
                this.setMessage("error.floataccount.debitnote.final");
            else if(sqlExp.getErrorCode()==20256)
                this.setMessage("error.floataccount.debitnote.deposit");
            else if(sqlExp.getErrorCode()==20257)
                this.setMessage("error.floataccount.debitnote.debittransaction");
            else if(sqlExp.getErrorCode()==20258)
                this.setMessage("error.invoice.final.associatepolicy");
            else if(sqlExp.getErrorCode()==20259)
                this.setMessage("error.reports.finance.invoicereport");
            else if(sqlExp.getErrorCode()==20260)
                this.setMessage("error.reports.finance.citiclaimsdetailsrpt");
            else if(sqlExp.getErrorCode()==20261)
                this.setMessage("error.administration.policy.BufferAllocation");
            //added for hyundai buffer
            else if(sqlExp.getErrorCode()==20262)
                this.setMessage("error..supportDoc.NormalBufferExhaustion");
            else if(sqlExp.getErrorCode()==20263)
                this.setMessage("error.supportDoc.CriticalMedicalExhaustion");
            else if(sqlExp.getErrorCode()==20264)
                this.setMessage("error.supportDoc.CriticalCorpusExhaustion");
            else if(sqlExp.getErrorCode()==20301)
                this.setMessage("error.administration.productrule.exists");
            else if(sqlExp.getErrorCode()==20302)
                this.setMessage("error.administration.policyrule.notdefined");
            else if(sqlExp.getErrorCode()==20303)
                this.setMessage("error.preauth.process.manualpreauth");
            else if(sqlExp.getErrorCode()==20304)
                this.setMessage("error.claims.process.manualclaim");
            else if(sqlExp.getErrorCode()==20323)
            	this.setMessage("error.ailment.dentalBenefitType");
            else if(sqlExp.getErrorCode()==20351)
                this.setMessage("error.usermanagement.onlineaccess.invalidid");
            else if(sqlExp.getErrorCode()==20401)
                this.setMessage("error.customercare.calldetails.close");
            else if(sqlExp.getErrorCode()==20500)
                this.setMessage("error.webservice.policyinfonotentered");
            else if(sqlExp.getErrorCode()==20501)
                this.setMessage("error.webservice.memberalredyuploaded");
            else if(sqlExp.getErrorCode()==20502)
                this.setMessage("error.webservice.policygroupnotmatch");
            else if(sqlExp.getErrorCode()==20503)
                this.setMessage("error.webservice.previouspolicynumbersame");
            else if(sqlExp.getErrorCode()==20504)
                this.setMessage("error.webservice.ageanddobmissing");
            else if(sqlExp.getErrorCode()==20505)
                this.setMessage("error.webservice.suminsuredorpremiummissing");
            else if(sqlExp.getErrorCode()==20506)
                this.setMessage("error.webservice.batchcompleted");
            else if(sqlExp.getErrorCode()==20507)
                this.setMessage("error.webservice.policydoesnotexists");
            else if(sqlExp.getErrorCode()==20508)
                this.setMessage("error.administration.productrule.rulestartdate");
            else if(sqlExp.getErrorCode()==20509)
                this.setMessage("error.enrollment.members.effectivedate");
            else if(sqlExp.getErrorCode()==20510)// koc Claims_investigation_delete
                this.setMessage("error.inwardentry.claims.delete");
            else if(sqlExp.getErrorCode()==20511)
                this.setMessage("error.enrollment.policy.rulenotdefined");
            else if(sqlExp.getErrorCode()==20512)
                this.setMessage("error.webservice.invalidmemberstatus");
            else if(sqlExp.getErrorCode()==20513)
                this.setMessage("error.webservice.namenotmatching");
            /*else if(sqlExp.getErrorCode()==20514)
                this.setMessage("error.inwardentry.batchdetails.addpolicy");*/   
            else if(sqlExp.getErrorCode()==20515)
                this.setMessage("error.webservice.membernotfound");
            else if(sqlExp.getErrorCode()==20516)
                this.setMessage("error.webservice.duplicatebatch");
            else if(sqlExp.getErrorCode()==20517)
                this.setMessage("error.enrollment.policydetails.renewalpolicy");
            else if(sqlExp.getErrorCode()==20518)
            	this.setMessage("error.webservice.invalidschema");
            else if(sqlExp.getErrorCode()==20519)
                this.setMessage("error.webservice.endorsementdate");
            else if(sqlExp.getErrorCode()==20520)
                this.setMessage("error.webservice.policycomplete");
            else if(sqlExp.getErrorCode()==20521)
                this.setMessage("error.webservice.endorsementcomplete");
            else if(sqlExp.getErrorCode()==20522)
                this.setMessage("error.enrollment.members.approvecardprint");
            else if(sqlExp.getErrorCode()==20523)
                this.setMessage("error.enrollment.policydetails.validityperiod");
            else if(sqlExp.getErrorCode()==20524)
                this.setMessage("error.enrollment.policydetails.renewvalidityperiod");
            else if(sqlExp.getErrorCode()==20526)
                this.setMessage("error.webservice.familysuminsuredmissing");
            else if(sqlExp.getErrorCode()==20527)
                this.setMessage("error.webservice.policy.date");
            else if(sqlExp.getErrorCode()==20528)
                this.setMessage("error.webservice.membersuminsuredmissing");
            else if(sqlExp.getErrorCode()==20529)
                this.setMessage("error.webservice.policydomicilary");
            else if(sqlExp.getErrorCode()==20530)
                this.setMessage("error.webservice.policydomicilaryamount");
            else if(sqlExp.getErrorCode()==20531)
                this.setMessage("error.enrollment.members.cardprintnotallowed");
            else if(sqlExp.getErrorCode()==20532)
                this.setMessage("error.enrollment.members.bonuslimit");
            else if(sqlExp.getErrorCode()==20533)
                this.setMessage("error.enrollment.members.cancelmember");
            else if(sqlExp.getErrorCode()==20535)
                this.setMessage("error.policy.policydetails.productchange");
            else if(sqlExp.getErrorCode()==20536)
                this.setMessage("error.endorsement.endorsement.suminsured");
            else if(sqlExp.getErrorCode()==20537)
                this.setMessage("error.endorsement.endorsement.cancelmember");
            else if(sqlExp.getErrorCode()==20538)
                this.setMessage("error.webservice.invalidttkid");
            else if(sqlExp.getErrorCode()==20539)
                this.setMessage("error.enrollment.member.delete");
            else if(sqlExp.getErrorCode()==20540)
                this.setMessage("error.softcopyupload.enrollmentno.mandatory");
            else if(sqlExp.getErrorCode()==20541)
                this.setMessage("error.enrollment.domiciliary.domiciliarytype");
            else if(sqlExp.getErrorCode()==20542)
                this.setMessage("error.notification.invalidcommnbr");
            else if(sqlExp.getErrorCode()==20543)
                this.setMessage("error.enrollment.policydetails.renewal");
            else if(sqlExp.getErrorCode()==20544)
                this.setMessage("error.webservice.invalidlocationcode");
            else if(sqlExp.getErrorCode()==20545)
                this.setMessage("error.empanelment.groupregn.locationcode");
            else if(sqlExp.getErrorCode()==20546)
                this.setMessage("error.webservice.invalidproductcode");
            else if(sqlExp.getErrorCode()==20547)
                this.setMessage("error.webservice.dateofexit");
            else if(sqlExp.getErrorCode()==20548)
                this.setMessage("error.webservice.cancelmember");
            else if(sqlExp.getErrorCode()==20549)
                this.setMessage("error.webservice.cancelmemberperiod");
            else if(sqlExp.getErrorCode()==20550)
                this.setMessage("error.administration.product.duplicateprodcode");
            else if(sqlExp.getErrorCode()==20551)
                this.setMessage("error.administration.policy.webconfiglinks");
            else if(sqlExp.getErrorCode()==20552)
                this.setMessage("error.webservice.mandatoryself");
            else if(sqlExp.getErrorCode()==20553)
                this.setMessage("error.webservice.suminsuredwebconfig");
            else if(sqlExp.getErrorCode()==20554)
                this.setMessage("error.enrollment.members.domiciliary");
            /*else if(sqlExp.getErrorCode()==20555)
                this.setMessage("error.webservice.suminsuredmatch");*/
            else if(sqlExp.getErrorCode()==20556)
                this.setMessage("error.administration.domamount");
            else if(sqlExp.getErrorCode()==20557)
                this.setMessage("error.endorsement.endorsement.changeinfo");
            else if(sqlExp.getErrorCode()==20558)
                this.setMessage("error.endorsement.endorsement.addmodpolicyinfo");
            else if(sqlExp.getErrorCode()==20559)
                this.setMessage("error.endorsement.endorsement.paclaim");
            else if(sqlExp.getErrorCode()==20660)
                this.setMessage("error.weblogin.createself");
            else if(sqlExp.getErrorCode()==20661)
                this.setMessage("error.weblogin.normalsuminsured");
            else if(sqlExp.getErrorCode()==20662)
                this.setMessage("error.weblogin.preclaimexist");
            else if(sqlExp.getErrorCode()==20663)
                this.setMessage("error.weblogin.relwindowperiod");
            else if(sqlExp.getErrorCode()==20664)
                this.setMessage("error.weblogin.dateofmarriage");
            else if(sqlExp.getErrorCode()==20665)
                this.setMessage("error.weblogin.dateofbirth");
            else if(sqlExp.getErrorCode()==20666)
                this.setMessage("error.administration.dateofmarriagerel");
            else if(sqlExp.getErrorCode()==20667)
                this.setMessage("error.administration.dateofbirthrel");
            else if(sqlExp.getErrorCode()==20668)
                this.setMessage("error.weblogin.spouseage");
            else if(sqlExp.getErrorCode()==20669)
                this.setMessage("error.weblogin.memberdelete");
            else if(sqlExp.getErrorCode()==20670)
                this.setMessage("error.weblogin.childage");
            else if(sqlExp.getErrorCode()==20671)
                this.setMessage("error.weblogin.spouseagedom");
            else if(sqlExp.getErrorCode()==20672)
                this.setMessage("error.weblogin.paintimationmemcancel");
            else if(sqlExp.getErrorCode()==20673)
                this.setMessage("error.weblogin.selfrelgender");
            else if(sqlExp.getErrorCode()==20674)
                this.setMessage("error.weblogin.relconflict");
            else if(sqlExp.getErrorCode()==20675)
                this.setMessage("error.weblogin.spouserelmatch");
            else if(sqlExp.getErrorCode()==20676)
                this.setMessage("error.weblogin.parent'sagediscrepancy");
            else if(sqlExp.getErrorCode()==20677)
                this.setMessage("error.administration.policy.webconfiglinksimage");
            else if(sqlExp.getErrorCode()==20678)
                this.setMessage("error.webservice.member.renewal");
            else if(sqlExp.getErrorCode()==20679)
                this.setMessage("error.empanelment.groupregn.notifytype");
            else if(sqlExp.getErrorCode()==20680)
                this.setMessage("error.maintenance.familysuminsured");
            else if(sqlExp.getErrorCode()==20681)
                this.setMessage("error.maintenance.memsuminsured");
            else if(sqlExp.getErrorCode()==20682)
                this.setMessage("error.administration.policy.bufferdetails");
            else if(sqlExp.getErrorCode()==20683)
                this.setMessage("error.maintenance.subtype");
            else if(sqlExp.getErrorCode()==20684)
                this.setMessage("error.finance.chequeprint");
            else if(sqlExp.getErrorCode()==20685)
                this.setMessage("error.preauth.search.filter");
            else if(sqlExp.getErrorCode()==20686)
                this.setMessage("error.onlineenrollment.addmember");
            else if(sqlExp.getErrorCode()==20687)
                this.setMessage("error.webservice.renewmember");
            else if(sqlExp.getErrorCode()==20688)
                this.setMessage("error.preauthclaim.reassociate");
            else if(sqlExp.getErrorCode()==20689)
                this.setMessage("error.maintenance.notification.ttkoffice");
            else if(sqlExp.getErrorCode()==20690)
                this.setMessage("error.usermanagement.onlineaccess.citbanklogin");
            else if(sqlExp.getErrorCode()==20691)
                this.setMessage("error.webservice.policyowner");
            else if(sqlExp.getErrorCode()==20692)
                this.setMessage("error.administration.policy.bufferzero");
            else if(sqlExp.getErrorCode()==20693)
                this.setMessage("error.weblogin.clearplan");
            else if(sqlExp.getErrorCode()==20694)
                this.setMessage("error.weblogin.siagechanges");
            else if(sqlExp.getErrorCode()==20695)
                this.setMessage("error.support.onlineassistance.badrequest");
            else if(sqlExp.getErrorCode()==20696)
                this.setMessage("error.claims.claimdetails.mismatchenrnclm");
            else if(sqlExp.getErrorCode()==20697)
                this.setMessage("error.webservice.relagedependent");
            else if(sqlExp.getErrorCode()==20698)
                this.setMessage("error.webservice.certificateorderno");
            else if(sqlExp.getErrorCode()==20699)
                this.setMessage("error.webservice.customercodemissed");
            else if(sqlExp.getErrorCode()==20701)
                this.setMessage("error.claims.claimdetails.inpatientnumber");
            else if(sqlExp.getErrorCode()==20702)
                this.setMessage("error.claims.claimdetails.bufferamount");
            else if(sqlExp.getErrorCode()==20703)
                this.setMessage("error.preauth.preauthdetails.revert");
            else if(sqlExp.getErrorCode()==20704)
                this.setMessage("error.preauth.preauthdetails.revertapprove");
            else if(sqlExp.getErrorCode()==20705)
                this.setMessage("error.coding.icddetails.invalidicdcode");
            else if(sqlExp.getErrorCode()==20706)
                this.setMessage("error.coding.icddetails.invalidproccode");
            else if(sqlExp.getErrorCode()==20707)
                this.setMessage("error.claims.dischargevoucher.statusreceive");
            else if(sqlExp.getErrorCode()==20708)
                this.setMessage("error.claims.hospcashbenefit");
            else if(sqlExp.getErrorCode()==20709)
                this.setMessage("error.claims.hospcashbenefit.override");
            else if(sqlExp.getErrorCode()==20710)
                this.setMessage("error.claims.hospcashbenefit.overridebenefit");
            else if(sqlExp.getErrorCode()==20711)
                this.setMessage("error.webservice.dupcustomercode");
            else if(sqlExp.getErrorCode()==20712)
                this.setMessage("error.ipruweblogin.invalidpolicyno");
            else if(sqlExp.getErrorCode()==20713)
                this.setMessage("error.webservice.dupcertificatenbr");
            else if(sqlExp.getErrorCode()==20714)
                this.setMessage("error.webservice.dupempnbr");
            else if(sqlExp.getErrorCode()==20715)
                this.setMessage("error.preauth.authsettle.cancelmember");
            else if(sqlExp.getErrorCode()==20716)
                this.setMessage("error.enrollment.policydetails.defautlsuminsured");
            else if(sqlExp.getErrorCode()==20717)
                this.setMessage("error.enrollment.policydetails.suminsuredplan");
            else if(sqlExp.getErrorCode()==20718)
                this.setMessage("error.enrollment.policydetails.additionalsuminsured");
            else if(sqlExp.getErrorCode()==20719)
                this.setMessage("error.enrollment.policydetails.suminsuredpremium");
            else if(sqlExp.getErrorCode()==20720)
                this.setMessage("error.enrollment.policydetails.premiumgreaterthanplan");
            else if(sqlExp.getErrorCode()==20721)
                this.setMessage("error.enrollment.policydetails.deletesuminsured");
            else if(sqlExp.getErrorCode()==20722)
                this.setMessage("error.softcopyupload.employee.missing");
            else if(sqlExp.getErrorCode()==20723)
                this.setMessage("error.enrollment.premium.defaultsuminsured");
            else if(sqlExp.getErrorCode()==20724)
                this.setMessage("error.preauth.buffer.policybufexceed");
            else if(sqlExp.getErrorCode()==20725)
                this.setMessage("error.preauth.buffer.familybufexceed");
            else if(sqlExp.getErrorCode()==20726)
                this.setMessage("error.webservice.uploadhappening");
            else if(sqlExp.getErrorCode()==20727)
                this.setMessage("error.claims.claimdetails.overrideoldclaim");
            else if(sqlExp.getErrorCode()==20728)
                this.setMessage("error.claims.claimdetails.reassociateenrolid");
            else if(sqlExp.getErrorCode()==20729)
                this.setMessage("error.webservice.customisepwd");
            else if(sqlExp.getErrorCode()==20730)
                this.setMessage("error.enrollment.member.policycancellation");
            else if(sqlExp.getErrorCode()==20731)
                this.setMessage("error.webservice.dupclientcode");
            else if(sqlExp.getErrorCode()==20732)
                this.setMessage("error.administration.planpremium");
            else if(sqlExp.getErrorCode()==20733)
                this.setMessage("error.enrollment.polsubtype.plan");
            else if(sqlExp.getErrorCode()==20735)
                this.setMessage("error.empanelment.status.tdsprocessing");
            else if(sqlExp.getErrorCode()==20736)
                this.setMessage("error.finance.bankaccount.tdsprocessing");
            else if(sqlExp.getErrorCode()==20737)
                this.setMessage("error.preauth.settlement.sufficientbalance");
            else if(sqlExp.getErrorCode()==20738)
                this.setMessage("error.enrollment.endorsement.complete");
            else if(sqlExp.getErrorCode()==20739)
                this.setMessage("error.enrollment.members.renewal");
            else if(sqlExp.getErrorCode()==20740)
                this.setMessage("error.enrollment.policydetails.inscompany");
            else if(sqlExp.getErrorCode()==20741)
                this.setMessage("error.enrollment.policydetails.policyperiod");
            else if(sqlExp.getErrorCode()==20742)
                this.setMessage("error.enrollment.policydetails.policyperiodpreclm");
            else if(sqlExp.getErrorCode()==20743)
                this.setMessage("error.enrollment.policydetails.memvalidityperiod");
            else if(sqlExp.getErrorCode()==20745)
                this.setMessage("error.enrollment.policydetails.polmemcancel");
            else if(sqlExp.getErrorCode()==20746)
                this.setMessage("error.enrollment.policydetails.renewproduct");
            else if(sqlExp.getErrorCode()==20747)
                this.setMessage("error.enrollment.policydetails.renewpolicy");
            else if(sqlExp.getErrorCode()==20748)
                this.setMessage("error.finance.tds.monthlyremittance");
            else if(sqlExp.getErrorCode()==20749)
                this.setMessage("error.finance.tds.ackinfo");
            else if(sqlExp.getErrorCode()==20750)
                this.setMessage("error.enrollment.policydetails.dobo");
            else if(sqlExp.getErrorCode()==20751)
                this.setMessage("error.enrollment.policydetails.oldpolicyperiod");
            else if(sqlExp.getErrorCode()==20752)
                this.setMessage("error.enrollment.policydetails.invalidpolnbr");
            else if(sqlExp.getErrorCode()==20754)
                this.setMessage("error.preauth.uncompletedpreauth.deletion");
            else if(sqlExp.getErrorCode()==20755)
                this.setMessage("error.preauth.completedpreauth.deletion");
            else if(sqlExp.getErrorCode()==20756)
                this.setMessage("error.preauth.buffered.deletion");
            else if(sqlExp.getErrorCode()==20757)
                this.setMessage("error.webservice.noactiondeletedfamily");
            else if(sqlExp.getErrorCode()==20758)
                this.setMessage("error.webservice.renewdeletedfamily");
            else if(sqlExp.getErrorCode()==20759)
                this.setMessage("error.weblogin.configureSI");
            else if(sqlExp.getErrorCode()==20760)
                this.setMessage("error.customercare.closestatus");
            else if(sqlExp.getErrorCode()==20761)
                this.setMessage("error.preauth.shortfall.reminderdate");
            else if(sqlExp.getErrorCode()==20762)
                this.setMessage("error.enrollment.members.suminsured");
            else if(sqlExp.getErrorCode()==20763)
                this.setMessage("error.finance.floataccount.associategroup");
            else if(sqlExp.getErrorCode()==20764)
                this.setMessage("error.finance.floataccount.deletegroup");
            else if(sqlExp.getErrorCode()==20765)
                this.setMessage("error.finance.floataccount.mergefloatacct");
            else if(sqlExp.getErrorCode()==20766)
                this.setMessage("error.finance.chequeinfo.prevchqnewchqinfo");
            else if(sqlExp.getErrorCode()==20767)
                this.setMessage("error.maintenance.chequeinfo.chqnbrchange");
            else if(sqlExp.getErrorCode()==20768)
                this.setMessage("error.webservice.dupbatchnbr");
            else if(sqlExp.getErrorCode()==20769)
                this.setMessage("error.finance.floataccount.samefloattype");
            else if(sqlExp.getErrorCode()==20770)
                this.setMessage("error.finance.floataccount.floatstatus");
            else if(sqlExp.getErrorCode()==20771)
                this.setMessage("error.enrollment.policydetails.DOBOchange");
            else if(sqlExp.getErrorCode()==20772)
                this.setMessage("error.enrollment.policydetails.ProdDOBOchange");
            else if(sqlExp.getErrorCode()==20773)
                this.setMessage("error.enrollment.policydetails.RenewDOBOchange");
            else if(sqlExp.getErrorCode()==20774)
                this.setMessage("error.finance.tds.acknowledgementcomplete");
            else if(sqlExp.getErrorCode()==20775)
                this.setMessage("error.misreports.finance.USPendingReport");
            else if(sqlExp.getErrorCode()==20801)
                this.setMessage("error.weblogin.actionnotallowed");
            else if(sqlExp.getErrorCode()==20802)
                this.setMessage("error.weblogin.suminsured");
            else if(sqlExp.getErrorCode()==20803)
                this.setMessage("error.weblogin.forgotpassword");
            else if(sqlExp.getErrorCode()==20804)
                this.setMessage("error.finance.tds.certifictegen");
            else if(sqlExp.getErrorCode()==20999)
                this.setMessage("error.webservice.validationfail");
            else if(sqlExp.getErrorCode()==21000)
                this.setMessage("error.claims.claimreport.rowcount");
            else if(sqlExp.getErrorCode()==29279)
                this.setMessage("error.usermanagement.user.invalidmailid");
            else if(sqlExp.getErrorCode()==20600)
                this.setMessage("error.maintenance.claims.newreqamtgt");
            else if(sqlExp.getErrorCode()==20601)
            	this.setMessage("error.maintenance.claims.cqnotreleased");
            else if(sqlExp.getErrorCode()==20602)
                this.setMessage("error.maintenance.claims.overrideclmtochgamt");
            else if(sqlExp.getErrorCode()==20603)
                this.setMessage("error.maintenance.claims.modnotallowcbc");
            else if(sqlExp.getErrorCode()==20776)
            	this.setMessage("error.claims.bills.sertaxnbr");
            else if(sqlExp.getErrorCode()==20777)
            	this.setMessage("error.claims.bills.sertaxdup");
            else if(sqlExp.getErrorCode()==20778)
            	this.setMessage("error.claims.settlement.discamt");
            else if(sqlExp.getErrorCode()==20779)
            	this.setMessage("error.claims.settlement.copayamt");
            else if(sqlExp.getErrorCode()==20780)
            	this.setMessage("error.claims.settlement.billappramt");
            else if(sqlExp.getErrorCode()==20781)
            	this.setMessage("error.administration.sevicetax.revisiondate");
            else if(sqlExp.getErrorCode()==20782)
            	this.setMessage("error.weblogin.changepassword");
            else if(sqlExp.getErrorCode()==20783)
            	this.setMessage("error.weblogin.clearplannotavbl");
            else if(sqlExp.getErrorCode()==20784)
            	this.setMessage("error.weblogin.policyno");
            else if(sqlExp.getErrorCode()==20785)
            	this.setMessage("error.weblogin.enrollmentid");
            else if(sqlExp.getErrorCode()==20786)
            	this.setMessage("error.coding.PreAuthorization.codingworkflow");
            else if(sqlExp.getErrorCode()==20787)
        	    this.setMessage("error.weblogin.enrollment.adddependent");
            else if(sqlExp.getErrorCode()==20788)
        	    this.setMessage("error.preauth.icdpcs.primaryailment");
            else if(sqlExp.getErrorCode()==20789)
            	this.setMessage("error.preauth.medical.speciality");
            else if(sqlExp.getErrorCode()==20805)
            	this.setMessage("error.administration.usermanagement.ttkuser.inactivate");
            else if(sqlExp.getErrorCode()==20806)
            	this.setMessage("error.administration.usermanagement.ttkuser.activation");
            else if(sqlExp.getErrorCode()==20807)
            	this.setMessage("error.administration.usermanagement.ttkuser.activate");
            else if(sqlExp.getErrorCode()==20808)
            	this.setMessage("error.administration.usermanagement.ttkuser.inactivation");
            else if(sqlExp.getErrorCode()==20809)
            	this.setMessage("error.claims.settlement.approveamt");
            else if(sqlExp.getErrorCode()==20810)
            	this.setMessage("error.administration.usermanagement.ttkuser.dtofresgn");
            else if(sqlExp.getErrorCode()==20811)
            	this.setMessage("error.claims.settlement.taxamtpaid");
            else if(sqlExp.getErrorCode()==20832)
            	this.setMessage("error.weblogin.enrollment.optout");
            else if(sqlExp.getErrorCode()==20833)
            	this.setMessage("error.weblogin.enrollment.dateofmarriage");            
		    else if(sqlExp.getErrorCode()==20813)
                this.setMessage("error.preauth.claims.uncomplete.entry.remarks");
		    else if(sqlExp.getErrorCode()==20814)
                this.setMessage("error.preauth.claims.icdpcscoding.cancelled");
		    else if(sqlExp.getErrorCode()==20815)
		          this.setMessage("error.preauth.claims.max.copay.suminsured.restricted.amount");
		    else if(sqlExp.getErrorCode()==20816)
                this.setMessage("error.preauth.claims.insufficient.amount");
		    else if(sqlExp.getErrorCode()==20817)
                this.setMessage("error.preauth.claims.max.copayamount.exceeded");
			else if(sqlExp.getErrorCode()==20953)
                this.setMessage("error.preauth.claims.intimate.to.inscompany");//1274A
		    else if(sqlExp.getErrorCode()==20819)
		    	 this.setMessage("error.webservice.finance.accounttype.mismatch");
		    else if(sqlExp.getErrorCode()==20820)
		    	 this.setMessage("error.webservice.finance.employeenumber.isnull");
		    else if(sqlExp.getErrorCode()==20821)
		    	 this.setMessage("error.webservice.finance.invaliddetails.productname");
		    else if(sqlExp.getErrorCode()==20822)
		    	 this.setMessage("error.webservice.finance.enrollmentnoexist.required.productname");
		    else if(sqlExp.getErrorCode()==20823)
		    	 this.setMessage("error.webservice.finance.invaliddetails.ifsccode");
		    else if(sqlExp.getErrorCode()==20824)
		    	 this.setMessage("error.webservice.finance.invalid.policyinfo.isnull");
		    else if(sqlExp.getErrorCode()==20825)
		    	 this.setMessage("error.webservice.finance.invalid.hospitalinfo");
		    else if(sqlExp.getErrorCode()==20826)
		    	 this.setMessage("error.webservice.finance.invalid.hospitalinfo.isnull");
			else if(sqlExp.getErrorCode()==20827)
                          this.setMessage("enrollment.add.duplicate");
			else if(sqlExp.getErrorCode()==20829)// koc note change
                this.setMessage("enrollment.add.deppedent.common.gender");
			else if(sqlExp.getErrorCode()==20839)
                this.setMessage("enrollment.add.deppedent.opposite.gender");
			 else if(sqlExp.getErrorCode()==20835)
		    	 this.setMessage("error.weblogin.endorsementexist.actionnotallowed");
			 else if(sqlExp.getErrorCode()==20836)
		    	 this.setMessage("error.preauth.claim.intimated.tpa.chage.status.remarks");//1274A
		    else if(sqlExp.getErrorCode()==20950)
		    	 this.setMessage("error.usermanagement.login.useraccountlock");
		    else if(sqlExp.getErrorCode()==20951)
		    	 this.setMessage("error.usermanagement.login.useripaddress");
				else if(sqlExp.getErrorCode()==20850)
			    	 this.setMessage("error.enrollment.buffer.addeddate.lessthan.policyormemberdate");
				else if(sqlExp.getErrorCode()==20851)
			    	 this.setMessage("error.enrollment.configure.buffer.policylevel");
				else if(sqlExp.getErrorCode()==20852)
			    	 this.setMessage("error.enrollment.approvedamount.exceeds.familyorcorporate.bufferamount");
				else if(sqlExp.getErrorCode()==20853)
			    	 this.setMessage("error.enrollment.buffer.configure.shouldnot.lessthan.utilized");
				else if(sqlExp.getErrorCode()==20854)
			    	 this.setMessage("error.enrollment.active.preauthorclaims.exists");
				else if(sqlExp.getErrorCode()==20855)
			    	 this.setMessage("error.maintenace.enrollment.enrollment.member.buffer.amount");
	            //added as per KOC 1216b Change request IBM
				else if(sqlExp.getErrorCode()==20888)
			    	 this.setMessage("error.claims.settlement.save");
				else if(sqlExp.getErrorCode()==20997)
			    	 this.setMessage("error.lmpdate");
               //added as per KOC maternity Change rekha
   	        	//added for Plan Premium CR
			    else if(sqlExp.getErrorCode()==20856)
				this.setMessage("error.weblogin.enrollment.addmember.planpremium");
	           //'Enterd date should less STALE than check date or greater than System Date
			    else if(sqlExp.getErrorCode()==20857)
		    	 this.setMessage("error.finance.chequeinformation.date.greater.or.less.systemdate");
	            //added for Endorsement Age CR for IBM
			    else if(sqlExp.getErrorCode()==20858)
			    	this.setMessage("error.enrollment.addmember.dateofmarriage");
              //KOC 1286 for OPD
			    else if(sqlExp.getErrorCode()==20889)
			    	this.setMessage("error.claims.settlement.approved");
					
			    else if(sqlExp.getErrorCode()==20887)
			    	this.setMessage("error.enrollment.policydetails.confirm");
		       //KOC 1286 for OPD
               //as per KOC 1285 Change Request
			    else if(sqlExp.getErrorCode()==20862)
			    	this.setMessage("error.claims.doctor.certificate.notchecked");
				 else if(sqlExp.getErrorCode()==20863)
			    	 this.setMessage("error.preauth.claims.modification.not.allowed");
			    else if(sqlExp.getErrorCode()==20864)
			    	 this.setMessage("error.preauth.claims.modification.not.allowed.rejected");
			    else if(sqlExp.getErrorCode()==20865)
			    	 this.setMessage("error.preauth.claims.overide.inscompany.not.allowed");
	        	else if(sqlExp.getErrorCode()==20866)
			    	 this.setMessage("error.preauth.claims..require.inscompany.decision");
					  else if(sqlExp.getErrorCode()==20866)
			    	 this.setMessage("error.preauth.claims..require.inscompany.decision");
				else if(sqlExp.getErrorCode()==20867)
			    	 this.setMessage("error.preauth.claims.cannot.be.with.current.status");
				else if(sqlExp.getErrorCode()==20868)
			    	 this.setMessage("error.preauth.claims.cannot.be.changed");
	       //added for koc 1278
		    else if(sqlExp.getErrorCode()==20840)
		    	 this.setMessage("error.enrollment.members.personalwaitingperiod");
		    else if(sqlExp.getErrorCode()==20841)
		    	 this.setMessage("error.coding.general.promote.personalwaitingperiod");
		    else if(sqlExp.getErrorCode()==20845)
		    	 this.setMessage("error.enrollment.members.icdcodeunknown");
        	//added for koc 1278
        	//added for koc 1075 PED
		    else if(sqlExp.getErrorCode()==20846)
		    	 this.setMessage("error.preauth.medical.sincewhen");
        	//added for koc 1075 PED
     	// KOC 1270 for hospital cash benefit
			    else if(sqlExp.getErrorCode()==20878)
			    	 this.setMessage("error.administration.Policy.General.Configure");
			// KOC 1270 for hospital cash benefit
			else if(sqlExp.getErrorCode()==20879)
			    this.setMessage("error.save.decoupling");
			else if(sqlExp.getErrorCode()==20880)
		    	 this.setMessage("error.revert.decoupling");
			else if(sqlExp.getErrorCode()==20881)
		    	 this.setMessage("error.policyopt.checkbox.save");	            
			else if(sqlExp.getErrorCode()==20882)
				 this.setMessage("error.decoupling.claims.medical.diagnosissave"); 	            
			else if(sqlExp.getErrorCode()==20883)
				 this.setMessage("error.decoupling.claims.coding.codingsave"); 
			else if(sqlExp.getErrorCode()==20884)
				 this.setMessage("error.decoupling.claims.medical.diagnosissave.promote"); 
			else if(sqlExp.getErrorCode()==20885)
				 this.setMessage("error.decoupling.claims.coding.icd.save"); 
			else if(sqlExp.getErrorCode()==20886)
				 this.setMessage("error.coding.claims.diagSeqId"); 
			else if(sqlExp.getErrorCode()==20886)
				this.setMessage("error.DataEntryCoding.claims.diagSeqId");  
			else if(sqlExp.getErrorCode()==20897)
			    	this.setMessage("error.claim.settlement.member.admissiondate");
			//added for KOC-1273
			    else if(sqlExp.getErrorCode()==20890)
			    	this.setMessage("error.claim.settlement.criticalillness.benefit.age");

			    else if(sqlExp.getErrorCode()==20891)
			    	this.setMessage("error.claim.settlement.criticalillness.invalidgroup");

			    else if(sqlExp.getErrorCode()==20892)
			    	this.setMessage("error.claim.settlement.criticalillness.time.limit");

			    else if(sqlExp.getErrorCode()==20893)
			    	this.setMessage("error.claim.settlement.criticalillness.survival.period");
			    else if(sqlExp.getErrorCode() == 20894)
			    	this.setMessage("error.claim.general.criticalillness.employ.optfor.criticalbenefit");
			    else if(sqlExp.getErrorCode() == 20895)
			    	this.setMessage("error.claim.settlement.save.waitingperiod");
			    else if(sqlExp.getErrorCode()==20896)
	                this.setMessage("error.admin.insurance.Aprover.closed");
                  else if(sqlExp.getErrorCode()==20859)
		    	 this.setMessage("error.policies.clausedetails.shortfall.association.clausesubtype");
           	else if(sqlExp.getErrorCode()==20860)
		    	 this.setMessage("error.policies.clausedetails.shortfall.configured.cannot.deleted");
			else if(sqlExp.getErrorCode()==20861)
		    	 this.setMessage("error.policies.clausedetails.shortfall.clausesubtype.change.not.allowed");
			else if(sqlExp.getErrorCode()==20952)
                 this.setMessage("error.inscompany.already.responded");//1274A
			else if(sqlExp.getErrorCode()==20898)
                 this.setMessage("error.preauth.general.save.override");//1274A
            else if(sqlExp.getErrorCode()==20790) // KOC INVESTIGATION CHANGE
			     this.setMessage("error.claims.processing.general.pleasecheck.investigation");
	         //added for hyundai buffer
            else if(sqlExp.getErrorCode()==20989)
				this.setMessage("error.claim.Bufferlevel");      
            else if(sqlExp.getErrorCode()==20990)
				this.setMessage("error.administration.policies.Bufferlevel");      
            else if(sqlExp.getErrorCode()==20991)
				this.setMessage("error.administration.policies.Bufferconfiglevel");      
            else if(sqlExp.getErrorCode()==20992)
				this.setMessage("error.claim.Bufferdeletelevel");      
            else if(sqlExp.getErrorCode()==20993)
				this.setMessage("error.preauth.Bufferdeletelevel");  
            //hospital Login-Added by Kishor on 06/06/2014
            else if(sqlExp.getErrorCode()==20834) // KOC HOSPITAL LOGIN
			     this.setMessage("error.hospital.disempanel.login");
            else if(sqlExp.getErrorCode()==20998)
                 this.setMessage("error.mobile.app.login.failure");     
			else if(sqlExp.getErrorCode()==20899)
				 this.setMessage("error.dataentrycoding.medical.DODandDOC");
			else if(sqlExp.getErrorCode()==20373)
				 this.setMessage("error.services.performed.after.last");
			else if(sqlExp.getErrorCode()==20374)
				 this.setMessage("diagnosis.code.already.exists");
			else if(sqlExp.getErrorCode()==20370)
				 this.setMessage("patient.covered.member");
			else if(sqlExp.getErrorCode()==20371)
				 this.setMessage("services.performed.prior.effective");
			else if(sqlExp.getErrorCode()==20372)
				 this.setMessage("duplicate.preauth.request");
		   else if(sqlExp.getErrorCode()==20375)
					 this.setMessage("you.cannot.delete.approved.status.progress.rejected");
			else if(sqlExp.getErrorCode()==20377)
				  this.setMessage("you.cannot.delete.activity.claim.status.progress");
			else if(sqlExp.getErrorCode()==20378)
				  this.setMessage("you.cannot.delete.observation.claims.status.progress");
			else if(sqlExp.getErrorCode()==20379)
				  this.setMessage("primary.diagnosis.already.exists");
			else if(sqlExp.getErrorCode()==20380)
				  this.setMessage("add.atleast.one.diagnosis");
			else if(sqlExp.getErrorCode()==20376)
					  this.setMessage("you.cannot.delete.diagnosis.details");
			else if(sqlExp.getErrorCode()==20869)
				 this.setMessage("error.intx.Online.provider.contacts");  
			else if(sqlExp.getErrorCode()==20870)
				 this.setMessage("error.intx.Online.medical.code"); 
			else if(sqlExp.getErrorCode()==20286)
				 this.setMessage("error.selfFund.validateMobile"); 
			else if(sqlExp.getErrorCode()==20287)
				 this.setMessage("error.selfFund.InactiveMember"); 
			else if(sqlExp.getErrorCode()==20871)
				 this.setMessage("error.intx.PreEmpanelment.Credentials"); 
			else if(sqlExp.getErrorCode()==20872)
				 this.setMessage("error.intx.professionalID"); 
			else if(sqlExp.getErrorCode()==20873)
				 this.setMessage("error.intx.professionalID.Duplicate"); 
			else if(sqlExp.getErrorCode()==20877)//20876 error code using in backEnd
				 this.setMessage("error.intx.activityAlreadyExist"); 
			else if(sqlExp.getErrorCode()==20994)
				 this.setMessage("error.intx.tariffGrossDiscountAmount"); 
			else if(sqlExp.getErrorCode()==20995)
				 this.setMessage("error.intx.EmpanelFinanceReview"); 
            else if(sqlExp.getErrorCode()==20996)
				 this.setMessage("error.intx.provider.not.registered");
            else if(sqlExp.getErrorCode()==20874)
				 this.setMessage("error.intx.provider.registeredWithAnother");
			else if(sqlExp.getErrorCode()==20381)
						  this.setMessage("clinician.not.exist");
			else if(sqlExp.getErrorCode()==20382)
				  this.setMessage("clinician.already.exist");
			else if(sqlExp.getErrorCode()==20383)
				  this.setMessage("enhancement.date.not.lessthan.original.date");
	else if(sqlExp.getErrorCode()==20384)
		  this.setMessage("please.complete.parent.preauth.first");
			else if(sqlExp.getErrorCode()==20385)
				  this.setMessage("close.opened.shortfall.before.raise");
			else if(sqlExp.getErrorCode()==20386)
				  this.setMessage("you.cannot.delete.shortfall.details");
			else if(sqlExp.getErrorCode()==20387)
				  this.setMessage("close.opened.shortfall.before.complete");
			else if(sqlExp.getErrorCode()==20388)
				  this.setMessage("error.claims.total.amount.should.not.exceed");
			else if(sqlExp.getErrorCode()==20389)
				  this.setMessage("error.entered.Claims.count.shouldbe.equal");
			else if(sqlExp.getErrorCode()==20390)
				  this.setMessage("error.completed.claim.batch");
			else if(sqlExp.getErrorCode()==20391)
				  this.setMessage("error.dublication.invoice.no");
			else if(sqlExp.getErrorCode()==20396)
				  this.setMessage("error.usermanagement.login.empaneluserinvalid");
			else if(sqlExp.getErrorCode()==20272)
					  this.setMessage("error.dateofAdimissionnotbetween.policyperiod");
			else if(sqlExp.getErrorCode()==20392)
				  this.setMessage("error.check.auth.benefit.type.not.matching");
			else if(sqlExp.getErrorCode()==20393)
				  this.setMessage("error.check.auth.maternity.gpla.values.not.matching");
			  else if(sqlExp.getErrorCode()==20394)
				  this.setMessage("error.authorization.number.mandatory");
			  else if(sqlExp.getErrorCode()==20395)
				  this.setMessage("error.claim.presents.finance.cannot.override");
				  else if(sqlExp.getErrorCode()==20396)
				  this.setMessage("error.usermanagement.login.empaneluserinvalid");
			else if(sqlExp.getErrorCode()==20272)
					  this.setMessage("error.dateofAdimissionnotbetween.policyperiod");				
			else if(sqlExp.getErrorCode()==20398)
				  this.setMessage("error.duplicate.emirate.id");
			else if(sqlExp.getErrorCode()==20399)
				  this.setMessage("error.duplicate.globalnet.member.Id"); 
			else if(sqlExp.getErrorCode()==20400)
				  this.setMessage("error.duplicate.networkCodeOrOrder");  
			else if(sqlExp.getErrorCode()==20402)
				  this.setMessage("error.multiple.EmirateID");  
			else if(sqlExp.getErrorCode()==20363)
				  this.setMessage("error.married.child");  
			else if(sqlExp.getErrorCode()== 20323)
				  this.setMessage("error.ailment.benefitType");
            //qatar error codes start
			else if(sqlExp.getErrorCode()==20900)
				  this.setMessage("error.cannot.spouse.principal.married");  
			else if(sqlExp.getErrorCode()==20901)
				  this.setMessage("error.cannot.spouse.spouse.married");  
			else if(sqlExp.getErrorCode() == 20902)
				this.setMessage("error.qatarId.numeric");
			else if(sqlExp.getErrorCode() == 20903)
				this.setMessage("error.empanelment.partner.partnerSave");
	
			else if(sqlExp.getErrorCode()==20403)
				  this.setMessage("error.member.range"); 
			else if(sqlExp.getErrorCode()==20404)
				  this.setMessage("error.onlinePreauth.eitherActivityOrDrug");  
			else if(sqlExp.getErrorCode()==20406)
				  this.setMessage("error.activityCode.same");  

			else if(sqlExp.getErrorCode()==20425)
				  this.setMessage("error.check.preauth.SystemOfMedicine");
			else if(sqlExp.getErrorCode()==20426)
				  this.setMessage("error.userLogin.InvalidUserCategory");
			else if(sqlExp.getErrorCode()==20405)
				  this.setMessage("error.no.currencyRate"); 
			else if(sqlExp.getErrorCode()==20427)
				  this.setMessage("error.shortfall.dateError");
            //exception related to partner login cr
			else if(sqlExp.getErrorCode()==20903)
				  this.setMessage("error.empanelment.partner.partnerSave"); 
			else if(sqlExp.getErrorCode()==20432)
				  this.setMessage("error.empanelment.duplicateProviderLicenceID"); 
			else if(sqlExp.getErrorCode()==20904)
				  this.setMessage("error.swiftCode.Mandatory"); 
			else if(sqlExp.getErrorCode()==20905)
				  this.setMessage("error.ibanNo.Mandatory"); 
			else if(sqlExp.getErrorCode()==20906)
				  this.setMessage("error.partnerNotEmpnaled.validation"); 
			else if(sqlExp.getErrorCode()==20907)
				  this.setMessage("error.partner.accountsRequired"); 
			else if(sqlExp.getErrorCode()==20908)
				this.setMessage("error.intx.PartnerEmpanelFinanceReview");
			else if(sqlExp.getErrorCode()==20909)
				this.setMessage("error.partner.status.for");
			else if(sqlExp.getErrorCode()==20910)
				this.setMessage("error.partner.disempaneled");
			else if(sqlExp.getErrorCode()==20911)
				this.setMessage("error.partner.validationPeriod");
			else if(sqlExp.getErrorCode()==20913)
				this.setMessage("error.invalid.login");
			else if(sqlExp.getErrorCode()==20407)
				  this.setMessage("error.partner.address.required");  
		  else if(sqlExp.getErrorCode()==20273)
			this.setMessage("error.atleast.one.activity"); 
		  else if(sqlExp.getErrorCode()==20274)    
				this.setMessage("error.activity.date.expired");
		  else if(sqlExp.getErrorCode()==20942) 
			    this.setMessage("error.partner.empaneledDate.validation");
            

			else if(sqlExp.getErrorCode()==20278)
				this.setMessage("error.pricing.policy.notmatcing");  
			else if(sqlExp.getErrorCode()==20279)
				  this.setMessage("error.limitonDenOptMat.maxlimit"); 
			else if(sqlExp.getErrorCode()==20280)
				  this.setMessage("error.pricing.limitDentalOptical.maxlimit"); 
			else if(sqlExp.getErrorCode()==20281)
				  this.setMessage("error.pricing.limitDentalMaternity.maxlimit"); 
			else if(sqlExp.getErrorCode()==20282)
				  this.setMessage("error.pricing.limitonDental.maxlimit"); 
			else if(sqlExp.getErrorCode()==20283)
				  this.setMessage("error.pricing.limitonOpticalmaternity.maxlimit"); 
			else if(sqlExp.getErrorCode()==20284)
				  this.setMessage("error.pricing.limitonOptical.maxlimit"); 
			else if(sqlExp.getErrorCode()==20285)
				  this.setMessage("error.pricing.limitonMaternity.maxlimit"); 
			else if(sqlExp.getErrorCode()==20924){
		          this.setMessage("error.dynamic.massage");
		          this.setDynamicErrorDesc(sqlExp.getMessage()); 
			}
            
			else if(sqlExp.getErrorCode()==20288)
				  this.setMessage("error.preauth.preauthdetails.recalculateduetochangeinactivity"); 
			
            
			else if(sqlExp.getErrorCode()==20914)
				this.setMessage("error.event.duplicate");
			else if(sqlExp.getErrorCode()==20915)
				this.setMessage("error.event.invalid");
			else if(sqlExp.getErrorCode()==20916)
				this.setMessage("error.event.specialcharacter");
			else if(sqlExp.getErrorCode()==20917)
				this.setMessage("error.event.notmatch");
			else if(sqlExp.getErrorCode()==20918)
				this.setMessage("error.event.notavailable");
			else if(sqlExp.getErrorCode()== 20919)
				this.setMessage("error.event.notmatchlength");
			else if(sqlExp.getErrorCode()== 20922)
				this.setMessage("error.FinReport.CurserClosed");
			
			else if(sqlExp.getErrorCode()==20920)
				this.setMessage("error.pbmevent.notmatchlength");
			else if(sqlExp.getErrorCode()==20921)
				this.setMessage("error.pbmevent.notcompleted");
		  
			else if(sqlExp.getErrorCode()== 20944)
				this.setMessage("error.empanelment.partner");
			
			else if(sqlExp.getErrorCode()==20923)
				this.setMessage("error.ICD.requierd");

			else if(sqlExp.getErrorCode()== 20919)
				this.setMessage("error.event.notmatchlength");

         			else if(sqlExp.getErrorCode()==20925)
         				this.setMessage("error.hrLogin.DateOfInception.backDated");
         			else if(sqlExp.getErrorCode()==20926)
         				this.setMessage("error.hrLogin.DateOfInception.policyEndDate");
         			else if(sqlExp.getErrorCode()==20927)
         				this.setMessage("error.hrLogin.member.age");
         			else if(sqlExp.getErrorCode()==20928)
         				this.setMessage("error.hrLogin.cancelled.group");
         			else if(sqlExp.getErrorCode()==20929)
         				this.setMessage("error.hrLogin.cancelled.member");
         			else if(sqlExp.getErrorCode()==20930)
         				this.setMessage("error.hrLogin.dependent.inceptionDate");
         			else if(sqlExp.getErrorCode()==20931)
         				this.setMessage("error.hrLogin.dependentAddition.notPossible");
         			else if(sqlExp.getErrorCode()==20932)
         				this.setMessage("error.hrLogin.backDated.deletion");
         			else if(sqlExp.getErrorCode()==20933)
         				this.setMessage("error.hrLogin.dateOfExit.beforeDateOfInception");
         			else if(sqlExp.getErrorCode()==20934)
         				this.setMessage("error.hrLogin.dateOfExit.backDate");
         			else if(sqlExp.getErrorCode()==20935)
         				this.setMessage("error.hrLogin.qatarId.length");
         			else if(sqlExp.getErrorCode()==20936)
         				this.setMessage("error.hrLogin.benificiaryName");
         			else if(sqlExp.getErrorCode()==20937)
         				this.setMessage("error.hrLogin.dateOfBirth");
         			else if(sqlExp.getErrorCode()==20938)
         				this.setMessage("error.hrLogin.fatherMother.notSingle");
         			else if(sqlExp.getErrorCode()==20939)
         				this.setMessage("error.hrLogin.hospitalizationDate.cancelled");
         			else if(sqlExp.getErrorCode()==20940)
         				this.setMessage("error.hrLogin.principal.cancelled");
         			else if(sqlExp.getErrorCode()==20941)
         				this.setMessage("error.hrLogin.policy.endDate");
         			else if(sqlExp.getErrorCode()== 20945)
        				this.setMessage("error.hrLogin.memberDateOfExit");
         			else if(sqlExp.getErrorCode()== 20946)
        				this.setMessage("error.hrLogin.claimPreauthExist");
         			else if(sqlExp.getErrorCode()== 20947)
        				this.setMessage("error.hrLogin.principal.maritalStatus");
         			else if(sqlExp.getErrorCode()== 20948)
        				this.setMessage("error.hrLogin.claimPreauthExist.modifications");
         			else if(sqlExp.getErrorCode()== 20955)
        				this.setMessage("error.hrLogin.bankInformation");
         			else if(sqlExp.getErrorCode()== 20956)
        				this.setMessage("error.hrLogin.ibanAccNoExist");
         			else if(sqlExp.getErrorCode()== 20957)
        				this.setMessage("error.preauth.ShortFallToINP");
         			else if(sqlExp.getErrorCode()== 20958)
        				this.setMessage("error.preauth.INPToShortFall");
         			else if(sqlExp.getErrorCode()== 20012)
        				this.setMessage("error.usermanagement.login.partner.userinvalid");
			        else if(sqlExp.getErrorCode()==20289)
				        this.setMessage("error.claims.claimsdetails.review");
         			else if(sqlExp.getErrorCode()== 20949)
        				this.setMessage("error.enhance.claimPreauthExist");
         			else if(sqlExp.getErrorCode()== 20954)
        				this.setMessage("error.enhance.policyExpired");
         			else if(sqlExp.getErrorCode()== 20960)
        				this.setMessage("error.preauth.enhancement");
         			else if(sqlExp.getErrorCode()==20408)
        				this.setMessage("error.claims.activitymismatch");
           				
			else if(sqlExp.getErrorCode()== 20943)
				this.setMessage("error.event.quotation.generate");
			else if(sqlExp.getErrorCode()== 20409)
				this.setMessage("error.clmpre.exist.quotation.generate");
			else if(sqlExp.getErrorCode()== 20410)
				this.setMessage("error.event.quotation.modification.generate");
			else if(sqlExp.getErrorCode()== 20411)
				this.setMessage("error.gender.same.quotation.generate");
			else if(sqlExp.getErrorCode()== 20962)
				this.setMessage("error.inceptiondate.quotation");
			else if(sqlExp.getErrorCode()== 20965)
				this.setMessage("error.network.cheque");
			else if(sqlExp.getErrorCode()== 20412)
				this.setMessage("error.override.cfd.clearance");
			else if(sqlExp.getErrorCode()== 20413)
				this.setMessage("error.override.cfd.notapproved");
			else if(sqlExp.getErrorCode()== 20966)
				this.setMessage("error.provider.configuration");
			else if(sqlExp.getErrorCode()== 20837)
				this.setMessage("error.pricing.inputscree2.maternitymismatch");
			else if(sqlExp.getErrorCode()== 20838)
				this.setMessage("error.employee.login.incorrectdate");
			else if(sqlExp.getErrorCode()== 20418)
				this.setMessage("error.report.already.generated");
			else if(sqlExp.getErrorCode()== 20967)
			{	
				String msg = sqlExp.getMessage();
				if(msg.contains("Dental"))
					this.setMessage("error.no.benefit.Dental.limit.exhausted");
				else if(msg.contains("Optical"))
					this.setMessage("error.no.benefit.Optical.limit.exhausted");
				else if(msg.contains("IP"))
					this.setMessage("error.no.benefit.InPatient.limit.exhausted");
				else if(msg.contains("OP"))
					this.setMessage("error.no.benefit.OutPatient.limit.exhausted");
				else if(msg.contains("Health"))
					this.setMessage("error.no.benefit.Health.limit.exhausted");
				else if(msg.contains("Out-Patient"))
					this.setMessage("error.no.benefit.Out-Patient.limit.exhausted");
			}
			else if(sqlExp.getErrorCode()== 20414)
				this.setMessage("error.member.notcovered.corporate");
			else if(sqlExp.getErrorCode()== 20415)
				this.setMessage("error.member.notcovered.facility");
			else if(sqlExp.getErrorCode()== 20416)
				this.setMessage("error.check.eligibility.disabled");
			else if(sqlExp.getErrorCode()== 20417)
				this.setMessage("error.member.not.eligible.treatment");
			else if(sqlExp.getErrorCode()== 20847)
				{	
					String msg = sqlExp.getMessage();
					if(msg.contains("Dental"))
						this.setMessage("error.no.buffer.Dental.limit");
					else if(msg.contains("Optical"))
						this.setMessage("error.no.buffer.Optical.limit");
					else if(msg.contains("OP"))	// OP maternity
						this.setMessage("error.no.buffer.OutPatient.Maternity.limit");
					else if(msg.contains("Out-Patient"))	// Out-Patient
						this.setMessage("error.no.buffer.Out-Patient.limit");
				}
			else if(sqlExp.getErrorCode()== 20420)
				this.setMessage("error.member.benefit.type.materinity");
			else if(sqlExp.getErrorCode()== 20419)
				this.setMessage("error.member.not.match.principalicd");
			else if(sqlExp.getErrorCode()== 20428)
				this.setMessage("error.resubmission.submitted");
			else if(sqlExp.getErrorCode()== 20421)
				this.setMessage("error.Benefit.selected.not.match.with.activity.code");
			else if(sqlExp.getErrorCode()== 20422)
				this.setMessage("error.Benefit.selected.not.match.with.Principal.ICD");
			else if(sqlExp.getErrorCode()== 20345)
				this.setMessage("error.system"); 			// provider login exception cr (ranjan)
			else if(sqlExp.getErrorCode()== 20429)
				this.setMessage("error.invalid.icd.code"); // provider login exception cr (ranjan)
			else if(sqlExp.getErrorCode()== 20430)
				this.setMessage("error.Duplicate.BatchRefNo");
			else if(sqlExp.getErrorCode()==20431)
				this.setMessage("error.current.previousyear.policyview");
			else if(sqlExp.getErrorCode()==20433)
				this.setMessage("error.disabled.memberid");
            
			else if(sqlExp.getErrorCode()== 20435)
				this.setMessage("error.claim.dates.should.not.previous.date");
            
			else if(sqlExp.getErrorCode()== 20436)
				this.setMessage("error.claim.dates.should.not.exeed.current.date");
			else if(sqlExp.getErrorCode()== 20443)
				this.setMessage("error.current.tariff.exists.without.end.date");
            
           
            
            
            
			else if(sqlExp.getErrorCode()==20437)
				this.setMessage("error.premium.band.already.added");
            
			else if(sqlExp.getErrorCode()==20438)
				this.setMessage("error.member.not.part.any.premium.band");
            
            
            
			else if(sqlExp.getErrorCode()== 20439)
				this.setMessage("error.Paymant.Term.already.exist");
			else if(sqlExp.getErrorCode()== 20440)
				this.setMessage("error.previous.Start.date.validtion");
			else if(sqlExp.getErrorCode()== 20442)
				this.setMessage("error.future.date.validation");
			else if(sqlExp.getErrorCode()== 20441)
				this.setMessage("error.inactive.bankaccount");
			else if(sqlExp.getErrorCode()== 20445)
				this.setMessage("error.re.submission.re.evaluate.add");
			else if(sqlExp.getErrorCode()== 20446)
				this.setMessage("error.preauth.not.matching.parent.claim");
            
			else if(sqlExp.getErrorCode()== 20447)
				this.setMessage("error.empanelment.broker.add.duplicate");    
            
            
           else
                this.setMessage("error.database"); 
 }//end of if()     
        else if(rootCause instanceof NullPointerException)
        {
        	rootCause.printStackTrace();//to be displayed in a log file later...
        	this.setMessage("error.nullpointer");
        }//end of else if(rootCause instanceof NullPointerException)
        else if(rootCause instanceof NamingException)
        {
            this.setMessage("error.naming."+this.getIdentfier());
        }//end of else if(rootCause instanceof NamingException)
        else if(rootCause instanceof FileNotFoundException)
        {
            this.setMessage("error.file");
        }//end of else if(rootCause instanceof FileNotFoundException)
        else if(rootCause instanceof JMSException)
		{
        	rootCause.printStackTrace();//to be displayed in a log file later...
        	this.setMessage("error.jms");
		}//end of else if(rootCause instanceof JMSException)
		else if(rootCause instanceof MessagingException)
		{
			rootCause.printStackTrace();//to be displayed in a log file later...
			this.setMessage("error.messaging");
		}//end of else if(rootCause instanceof MessagingException)
		else if(rootCause instanceof SendFailedException)
		{
			rootCause.printStackTrace();//to be displayed in a log file later...
			this.setMessage("error.sendmailfailed");
		}//end of else if(rootCause instanceof SendFailedException)
        else if(rootCause instanceof UnknownHostException)
		{
        	rootCause.printStackTrace();//to be displayed in a log file later...
        	this.setMessage("error.unknownhost");
		}//end of else if(rootCause instanceof SendFailedException)
		else if(rootCause instanceof ServerResponseException)
		{
			rootCause.printStackTrace();//to be displayed in a log file later...
			this.setMessage("error.serverresponse");
		}//end of else if(rootCause instanceof ServerResponseException)
		else if(rootCause instanceof ConnectException)
		{
			rootCause.printStackTrace();//to be displayed in a log file later...
			this.setMessage("error.connect");
		}//end of else if(rootCause instanceof ServerResponseException)
		else if(rootCause instanceof MalformedURLException)
		{
			rootCause.printStackTrace();//to be displayed in a log file later...
			this.setMessage("error.hylafaxserver");
		}//end of else if(rootCause instanceof ServerResponseException)
		else if(rootCause instanceof JRException)
		{
			rootCause.printStackTrace();//to be displayed in a log file later...
			this.setMessage("error.jrexp");
		}//end of else if(rootCause instanceof JRException)
        else if(rootCause instanceof JXLException)
		{
			rootCause.printStackTrace();//to be displayed in a log file later...
			this.setMessage("error.jxlexp");
		}//end of else if(rootCause instanceof JXLException)
		else if(rootCause instanceof TTKException)
        {
			rootCause.printStackTrace();//to be displayed in a log file later...
			if(this.getIdentfier() == null || this.getIdentfier().equals(""))
                this.setMessage("error.general");//set the general message if no appropriate identifier is found
            else
                this.setMessage("error."+this.getIdentfier());
        }//end of else if(rootCause instanceof TTKException)
        else
            this.setMessage("error.general");
    }//end of TTKException(Throwable rootCause, String strIdentifier)

    public TTKException(String message) {
  		// TODO Auto-generated constructor stub
         	super(message);
            this.setMessage(message);
     }

    /**
     * Sets the message code based on the appropriate flow
     *
     * @param strMessage String an indicator to identify the appropriate message
     */
    public void setMessage(String strMessage)
    {
        this.strMessage = strMessage;
    }//end of setMessage(String strMessage)

    /**
     * Gets the message code based on the appropriate flow
     *
     * @return strMessage String the message code
     */
    public String getMessage()
    {
        return strMessage;
    }//end of getMessage()

    /**
     * Sets the identifier based on the appropriate flow
     *
     * @param strIdentfier String an indicator to identify the appropriate flow
     */
    public void setIdentfier(String strIdentfier)
    {
        this.strIdentfier = strIdentfier;
    }//end of setIdentfier(String strIdentfier)

    /**
     * Gets the flow identifier
     *
     * @return strIdentfier String the flow identifier
     */
    public String getIdentfier()
    {
        return strIdentfier;
    }//end of getIdentfier()

    /**
     * Gets the throwable object which contains the actual exception object
     *
     * @return rootCause Throwable the exception object
     */
    public Throwable getRootCause()
    {
        return rootCause;
    }//end of getRootCause()
    
    public void setDynamicErrorDesc(String dynamicErrorDesc) {
 		this.dynamicErrorDesc = dynamicErrorDesc;
 	}
     public String getDynamicErrorDesc() {
 		return dynamicErrorDesc;
 	}
}//end of class TTKException