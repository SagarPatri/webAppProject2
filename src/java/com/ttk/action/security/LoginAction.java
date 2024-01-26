/**
 * @ (#) LoginAction.java 3rd Aug 2005
 * Project      : TTK HealthCare Services
 * File         : LoginAction.java
 * Author       : Nagaraj D V
 * Company      : Span Systems Corporation
 * Date Created : 3rd Aug 2005
 *
 * @author       : Nagaraj D V
 * Modified by   : Arun K N
 * Modified date : Mar 25, 2006
 * Reason        : for updating usermanagement developed part and remving the content,
 *                 which were read from the template before.
 */

package com.ttk.action.security;

//import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.servlet.http.Cookie;
//import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.business.common.SecurityManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.IconObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.usermanagement.UserSecurityProfile;
import com.ttk.dto.usermanagement.UserVO;

//Changes Added for Password Policy CR KOC 1235
import java.net.InetAddress;

import com.ttk.business.usermanagement.UserManager;
//End changes for Password Policy CR KOC 1235

/**
 * This class checks for the authorized User and puts the UserSecurityProfile to the session,
 * It invalidates the session when User Logs out.
 */
public class LoginAction extends TTKAction
{
    private static Logger log = Logger.getLogger( LoginAction.class );
    
    /**
     * This method is used check for the authorized user.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doLogin(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	try{
    		//ServletContext sc=servlet.getServletContext();
    		UserSecurityProfile userSecurityProfile = null; //Contains the User Information
    		//ArrayList alActiveUsers=null;   //This will contain the List of Users Logged in to application
            String strLink = "";
            String strFirstLogYN="";
			String strPwdExpiryMsg="";		//Changes Added for Password Policy CR KOC 1235
            String strPwdValidYN="";		//Changes Added for Password Policy CR KOC 1235
            UserVO userVO = new UserVO();
            Cookie cookie1=new Cookie("typeId","VINGS");
			response.addCookie(cookie1);
			response.setHeader("P3P", "CP='INT NAV UNI'");
            DynaActionForm frmLogin = (DynaActionForm)form;
            userVO.setUSER_ID((String)frmLogin.get("userid"));
            userVO.setPassword((String)frmLogin.get("password"));
			String ipAddress=request.getRemoteHost();			//Changes Added for Password Policy CR KOC 1235
			userVO.setHostIPAddress(ipAddress);					//Changes Added for Password Policy CR KOC 1235
            //get the user security profile object if he is valid User
            userSecurityProfile = (UserSecurityProfile)this.getSecurityManagerObject().login(userVO);
            strFirstLogYN = userSecurityProfile.getFirstLoginYN();
			strPwdExpiryMsg = userSecurityProfile.getPswdExpiryYN();		//Changes Added for Password Policy CR KOC 1235
            strPwdValidYN = userSecurityProfile.getPwdValidYN();			//Changes Added for Password Policy CR KOC 1235
            frmLogin.set("firstLoginYN", strFirstLogYN);
			frmLogin.set("pwdExpiryMsg", strPwdExpiryMsg);			//Changes Added for Password Policy CR KOC 1235
			frmLogin.set("pwdExpiryYN", strPwdValidYN);				//Changes Added for Password Policy CR KOC 1235
            if(userSecurityProfile != null && userSecurityProfile.getSecurityProfile()!=null)
            {
                /*if(userSecurityProfile.getUserTypeId().equals("COR"))
                {
                    userSecurityProfile.setLoginType("H");
                }//end of if(userSecurityProfile.getUserTypeId().equals("COR"))

                if(sc.getAttribute("ActiveUsers")!=null)
                    alActiveUsers=(ArrayList)sc.getAttribute("ActiveUsers");*/

                //set the User security profile object to session
                request.getSession().setAttribute("UserSecurityProfile",userSecurityProfile);
                request.getSession().setAttribute("frmLogin", frmLogin);
                setLinks(request);      //call the setlinks to set default links when user logins
                strLink = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getSecurityProfile().getActiveLink()+"."+((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getSecurityProfile().getActiveSubLink()+"."+((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getSecurityProfile().getActiveTab();
                //load the tool bar to session
                this.loadToolBar(request, strLink);
                if(TTKCommon.checkNull(request.getParameter("open")).equals("browser")){
                    return mapping.findForward(strLink);    //to open in same window for DMC user
                }//end of if(TTKCommon.checkNull(request.getParameter("open")).equals("browser"))
                if(mapping.findForward(strLink)!=null && mapping.findForward(strLink).getPath()!=null)
                 request.setAttribute("OpenPage",mapping.findForward(strLink).getPath());
                //return to the appropriate screen
                request.getSession().setAttribute("ipAddress", ipAddress);
                return (mapping.findForward("relogin"));
            }//end if(this.getSecurityManagerObject().login(userVO) != null)
            else
            {
                log.info("Returning to failure !!!!!!");
                return (mapping.findForward("relogin"));
            }//end of else

    	}//end of try
    	catch(TTKException expTTK)
		{
    		ActionMessages actionMessages = new ActionMessages();
            ActionMessage actionMessage = new ActionMessage(expTTK.getMessage());
            if("error.usermanagement.login.useripaddress".equals(expTTK.getMessage()))request.setAttribute("UALO","User already logged on");
            //getMessage will return like "user.create.error"
            log.info("Error Key: "+expTTK.getMessage());
            actionMessages.add("global.error",actionMessage);
            saveErrors(request,actionMessages);
            //return this.processExceptions(request, mapping, expTTK);
            return mapping.findForward("relogin");
            
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"login"));
		}//end of catch(Exception exp)
    }//end of doLogin(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to logoff from the application.
     * Finally it forwards to login page.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doLogoff(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	try{
			UserVO userVO = new UserVO();		//Changes Added for Password Policy CR KOC 1235
			//If the session is active remove the User from the Active Users List and invalidate the session
            if(request.getSession().getAttribute("UserSecurityProfile")!=null)
            {
                //UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
                //String strLoginType=userSecurityProfile.getLoginType();
				SecurityManager securityManagerObject = this.getSecurityManagerObject();			//Changes Added for Password Policy CR KOC 1235
				
    			int iResult =securityManagerObject.userLoginIPAddress(TTKCommon.getUserID(request),(String)request.getSession().getAttribute("ipAddress"));	//Changes Added for Password Policy CR KOC 1235

                if(request.getSession(false)!=null)//invalidate the session
                {
                	request.getSession().setAttribute("UserSecurityProfile",null);
                	request.getSession().invalidate();
                }//end of if(request.getSession(false)!=null) 

                /*if(strLoginType.equals("I")||strLoginType.equals("E"))      //redirect to online access login page for external users
                    return (mapping.findForward("onlineaccesslogin"));
                else
                    return (mapping.findForward("relogin"));*/
                
                
            }//end of if(request.getSession().getAttribute("UserSecurityProfile")!=null)
            	return (mapping.findForward("relogin"));
    	}//end of try
    	catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"login"));
		}//end of catch(Exception exp)
    }//end of doLogoff(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    /**
     * This method is used to doUserLogout from the application.
     * Finally it forwards to login page.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doUserLogout(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	try{
			    UserVO userVO = new UserVO();
				SecurityManager securityManagerObject = this.getSecurityManagerObject();
				userVO.setUSER_ID(request.getParameter("userid"));
				userVO.setPassword(request.getParameter("password"));
				userVO.setHostIPAddress(request.getRemoteHost());
				
    			String result =securityManagerObject.userLogout(userVO);	
    			
        if("Y".equalsIgnoreCase(result)){
        	request.setAttribute("updated","You Are Successfully Logged out!! Now You Can Login");        	
        }else{
        	request.setAttribute("UALO","User already logged on");
        	request.setAttribute("errorMsg","You have entered incorrect credentials");
        }
    			
    			return (mapping.findForward("relogin"));
    	}//end of try
    	catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"login"));
		}//end of catch(Exception exp)
    }//end of doUserLogout(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to logoff from the application.
     * Finally it forwards to login page.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doOnlineLogoff(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	try{
			UserVO userVO = new UserVO();		//Changes Added for Password Policy CR KOC 1235
			//If the session is active remove the User from the Active Users List and invalidate the session
            if(request.getSession().getAttribute("UserSecurityProfile")!=null)
            {
                //UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
                //String strLoginType=userSecurityProfile.getLoginType();
				SecurityManager securityManagerObject = this.getSecurityManagerObject();			//Changes Added for Password Policy CR KOC 1235
    			int iResult =securityManagerObject.userLoginIPAddress(TTKCommon.getUserID(request),"");	//Changes Added for Password Policy CR KOC 1235

                if(request.getSession(false)!=null)  //invalidate the session
                {
                	request.getSession().setAttribute("UserSecurityProfile",null);
                	request.getSession().invalidate();
                }//end of if(request.getSession(false)!=null) 

                /*if(strLoginType.equals("I")||strLoginType.equals("E"))      //redirect to online access login page for external users
                    return (mapping.findForward("onlineaccesslogin"));
                else
                    return (mapping.findForward("relogin"));*/
            }//end of if(request.getSession().getAttribute("UserSecurityProfile")!=null)
            request.setAttribute("logoutSuccess", "Successfully Logged out");
            if("OBR".equals(request.getParameter("loginType"))){
            	return (mapping.findForward("brokerLogin"));
            }
            else return (mapping.findForward("relogin"));
    	}//end of try
    	catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"login"));
		}//end of catch(Exception exp)
    }//end of doOnlineLogoff(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


    /**
     * Returns the SecurityManager session object for invoking methods on it.
     * @return SecurityManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private SecurityManager getSecurityManagerObject() throws TTKException
    {
        SecurityManager securityManager = null;
        try
        {
            if(securityManager == null)
            {
                InitialContext ctx = new InitialContext();
                securityManager = (SecurityManager) ctx.lookup("java:global/TTKServices/business.ejb3/SecurityManagerBean!com.ttk.business.common.SecurityManager");	//changed for jboss upgradation
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "security");
        }//end of catch(Exception exp)
        return securityManager;
    }//end getSecurityManagerObject()

    /**
     * This method will load the tool bar to the session
     * @param request HttpServletRequest current request object
     * @param strLink
     */
    private void loadToolBar(HttpServletRequest request, String strLink)
    {
        Toolbar toolBar = null;
        IconObject conflictIcon = new IconObject();
        conflictIcon.setImageURL("ErrorIcon");
        IconObject docViewIcon = new IconObject();
        docViewIcon.setImageURL("DocViewIcon");

        toolBar = new Toolbar(conflictIcon, docViewIcon, strLink);
        request.getSession().setAttribute("toolbar", toolBar); //set the tool bar to the session
    }//end of loadToolBar(HttpServletRequest request)
}//end of class LoginAction

