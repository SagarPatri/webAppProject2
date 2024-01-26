<%
/** @ (#) tablinks.jsp 20th Sep 2005
 * Project     : TTK Healthcare Services
 * File        : tablinks.jsp
 * Author      : Nagaraj D V
 * Company     : Span Systems Corporation
 * Date Created: 20th Sep 2005
 *
 * Modified by   : Arun K N
 * Modified date : 17th May 2007
 * Reason        : to show value of displayname attribute for tab links
 *
 */
 %>

<%@ page import="java.util.*,com.ttk.common.TTKCommon,com.ttk.common.security.*,com.ttk.dto.menu.Link" %>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,org.apache.struts.action.DynaActionForm" %>

      <style>
body { margin:10px; padding:0; font-family:Arial, Helvetica, sans-serif;}
	 #insLoginMenuId   { margin:0; padding:0px; list-style:none; background:url(/ttk/images/vertical_d_blue_classic_bg.png) repeat-x top; height:29px;  } 
	/* #insLoginMenuId   { margin:0; padding:0px; list-style:none; background:#BAD2F4 repeat-x top; width: 100%;} */
	#insLoginMenuId li{ display:inline; position:relative; float:left;  }
	#insLoginMenuId li a { display:block; float:left; height:29px; line-height:29px; padding:0 25px; text-decoration:none; color:#000000; font-weight:normal; font-size:12px; border-left:solid 1px #000000;  background:#BAD2F4;}
	#insLoginMenuId li a:hover {color:#a9401b; background:#FFFFFF; }
	
	#insLoginMenuId li ul { margin:0; padding:0 11px; line-height:none; position:absolute; top:29px; left:0; border:solid 1px #c93300; border-width:0px 1px 1px 1px; width:180px; display:none; background:#FFFFFF;}
	#insLoginMenuId li:hover ul { display:block}
	#insLoginMenuId li:hover a {color:#a9401b; background:#677BA8;} 
	#insLoginMenuId li ul li.li-no-hover:hover {background-color: transparent; background:#FFFFFF;}
	#insLoginMenuId li ul li { display:block;  border-bottom:solid 1px #dbdcd9; width:100%; background:url(/ttk/images/d_horizontal_red_classic_menu_arrow.gif) no-repeat 3px 12px;background-color:#FFFFFF; padding:0 0 0 10px; }
	#insLoginMenuId li ul li:last-child { border-bottom:0px; background:#FFFFFF;}
	#insLoginMenuId li ul a { border-width:0px; color:#909090; padding:0 5px 0 0; background-color:transparent;}
	#insLoginMenuId li:hover ul li a { color:#04B4AE}
	#insLoginMenuId li ul li a:hover { color:#4000FF}
	#insLoginMenuId li ul li a:hover { color:#4000FF; background:#FFFFFF;}
	#insLoginMenuId li ul li a {color:#a9401b; background:#FFFFFF;} 
	#insLoginMenuId li a:hover { color:#FFFFFF;}
	
	
	
	
</style>
<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)session.getAttribute("UserSecurityProfile");
	SecurityProfile spx=null;
	if(userSecurityProfile!=null && userSecurityProfile.getSecurityProfile()!=null)
	{
		spx = userSecurityProfile.getSecurityProfile();
	}
	ArrayList alTabs = new ArrayList();

	if(spx != null)
	   alTabs = spx.getTabs();

%>
<!-- <table width="100%"  border="0" cellspacing="0" cellpadding="0">

<tr> -->
<ul id="insLoginMenuId">
<%
	ArrayList alLinks = new ArrayList();
   	ArrayList alSubLinks = new ArrayList();
   	ArrayList alTabLinks = new ArrayList();
	if(spx != null)
	{
	   alLinks = spx.getLeftLinks();
	   alSubLinks = spx.getSubLinks();
	   alTabLinks =	spx.getTabs();
	}

	if(alLinks != null && alLinks.size() > 0) 	//check if any links exists
	{
		for(int i=0; i < alLinks.size(); i++)
		{
			Link leftLink= (Link)alLinks.get(i);
			if(spx.isActiveLink(leftLink.getName()))
			{  //links
				 if("Employee Home".equals(leftLink.getName()) || "Employee Pre-Approval".equals(leftLink.getName()) ||"Employee Claim Submission".equals(leftLink.getName())|| 
						"Employee Claims".equals(leftLink.getName()) || "Employee Change Password".equals(leftLink.getName()) 
						|| "Employee Network Providers".equals(leftLink.getName())|| "Employee Contact Us".equals(leftLink.getName())){ 
					/* if("Employee Home".equals(leftLink.getName()) || "Employee Pre-Approval".equals(leftLink.getName()) || 
							"Employee Claims".equals(leftLink.getName())){ */
			%>
				<!-- <td nowrap class="currentTab">  -->
				<%
				if(!"Employee Claims".equals(leftLink.getName())){
				%>
				<li style="CURSOR: default; HEIGHT: 29px; BORDER-RIGHT: #000000 1px solid; BORDER-BOTTOM: #000000 1px solid; FONT-WEIGHT: bold; COLOR: #ffffff; BACKGROUND-COLOR: #677BA8"> <a href="#" style="background-color: #677ba8;"><%=leftLink.getDisplayName()%></a>
			<%}else if("Employee Claims".equals(leftLink.getName())){%> 
				<li style="CURSOR: default; HEIGHT: 29px; BORDER-RIGHT: #000000 1px solid; BORDER-BOTTOM: #000000 1px solid; FONT-WEIGHT: bold; COLOR: #ffffff; BACKGROUND-COLOR: #677BA8"> <a href="#" style="background-color: #677ba8;">Claims History</a>
			<%}
			//check if any Sublinks exists for the current link
			
			
			
			%>
			
			</li><!-- </td> --><%
				}
			 } 
				else{//INACTIVE LINKS
					if("Employee Home".equals(leftLink.getName()) || "Employee Pre-Approval".equals(leftLink.getName()) || 
							"Employee Claim Submission".equals(leftLink.getName())||"Employee Claims".equals(leftLink.getName())|| "Employee Change Password".equals(leftLink.getName()) 
									|| "Employee Network Providers".equals(leftLink.getName())|| "Employee Contact Us".equals(leftLink.getName())){ 
						%>
						
							<%-- <!--  <td nowrap class="tab"> --><li><a href="#" onClick="javascript:modifyLinks('Link','<%=leftLink.getName()%>')">
				          		<%= leftLink.getDisplayName() %></a></li><!-- </td> --> --%>	          		
			<%if(!"Employee Claims".equals(leftLink.getName())){%>
				<li><a href="#" onClick="javascript:modifyLinks('Link','<%=leftLink.getName()%>')"><%= leftLink.getDisplayName() %></a></li>
			<%}else if("Employee Claims".equals(leftLink.getName())){%> 
			<li><a href="#" onClick="javascript:modifyLinks('Link','<%=leftLink.getName()%>')">Claims History</a></li>
			<%} 
				 } 
				}
			}
		}%>
		</ul>
			  <!-- </tr>			 
    </table> -->
    
<%--  <%
 System.out.println("Active Link:- "+spx.getActiveLink());
 System.out.println("Active SubLink:- "+spx.getActiveSubLink());
 System.out.println("Active Tab:- "+spx.getActiveTab());
 %> --%>

<%-- <!-- S T A R T : Tab Navigation -->
	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
      <%
    	if(alTabs!=null && alTabs.size()>0)
    	{
        	String strTab="";
        	String strSwitchType="";
        	String strActiveLink=spx.getActiveLink();
        	DynaActionForm generalForm=(DynaActionForm)session.getAttribute("frmPolicyList");

        	if(generalForm !=null)
        	{
        		strSwitchType=TTKCommon.checkNull((String)generalForm.get("switchType"));
	        	strSwitchType=strSwitchType.equals("")? "ENM":strSwitchType;
	        }
        	if(strActiveLink.equals("Enrollment"))
        	{
        		for(int i=0; i < alTabs.size(); i++)
	        	{
	        		Link tabLink= (Link)alTabs.get(i);
	        		strTab=tabLink.getName();
	        		if(spx.isActiveTab(tabLink.getName()))
	        		{
	    %>
 		       			<td nowrap class="currentTab"><%= tabLink.getDisplayName() %></td>
        <%
	        		}//end of if(spx.isActiveTab(tabLink.getName()))
	        		else if(!(strSwitchType.equals("ENM") && (strTab.equals("Endorsement")||strTab.equals("Alert"))))
        			{
	    %>
        				<td nowrap class="tab">
        				<a href="#" onClick="javascript:modifyOnlineLinks('Tab','<%=tabLink.getName()%>')" class="tabLink">
        					<%=tabLink.getDisplayName()%>
        				</a>
        				</td>
        <%
	        		}
	        	}//end of for(int i=0; i < alTabs.size(); i++)
        	}//end of if(strActiveLink.equals("Enrollment"))
        	else
        	{
	        	for(int i=0; i < alTabs.size(); i++)
	        	{
	        		Link tabLink= (Link)alTabs.get(i);
	        		if(spx.isActiveTab(tabLink.getName()))
	        		{
        %>
	        			<td nowrap class="currentTab"><%= tabLink.getDisplayName()%></td>
        <%
	        		}//end of if(spx.isActiveTab(tabLink.getName()))
	        		else
	        		{
        %>
        				<td nowrap class="tab">
    					<a href="#" onClick="javascript:modifyOnlineLinks('Tab','<%= tabLink.getName() %>')" class="tabLink">
    						<%= tabLink.getDisplayName() %>
    					</a>
        				</td>
        <%
		        	}
		        }//end of for(int i=0; i < alTabs.size(); i++)
        	}//end of else
        %>
        <%
        	if(strActiveLink.equals("Enrollment"))
        	{
        		if(strSwitchType.equals("ENM"))
        		{
        %>
 		       		<td width="100%" align="right" class="tab blankTab" nowrap>Enrollment View&nbsp;</td>
        <%
        		}//end of if(strSwitchType.equals("ENM"))
        		else if(strSwitchType.equals("END"))
        		{
        %>
 		       		<td width="100%" align="right" class="tab blankTab" nowrap>Endorsement View&nbsp;</td>
        <%
        		}//end of else if(strSwitchType.equals("END"))
        	}//end of if(strActiveLink.equals("Enrollment"))
        	else
        	{
        %>
	        	<td width="100%" class="tab blankTab" nowrap>&nbsp;</td>
        <%
	    	}//end of else
        }//end of if(alTabs!=null && alTabs.size()>0)
	 %>
      </tr>
    </table> --%>
	<!-- E N D : Tab Navigation -->
	
	
	<form name="insleftlinks" method="POST">
  <input type="hidden" name="mode">
  <INPUT TYPE="hidden" NAME="leftlink" VALUE="<%= spx.getActiveLink()%>">
  <INPUT TYPE="hidden" NAME="sublink" VALUE="<%= spx.getActiveSubLink()%>">
  <INPUT TYPE="hidden" NAME="tab" VALUE="<%=spx.getActiveTab()%>">
  <INPUT TYPE="hidden" NAME="loginType" VALUE="PBM">
  
  </form> 