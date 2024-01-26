<%
/** @ (#) doctordashboard.jsp 5/01/2020
 * Project     : ProjectX
 * File        : doctordashboard.jsp
 * Author      : Gyanendra 
 * Company     : Vidal
 * Date Created: 5/05/2020
 *
 * @author 		 : Gyanendra
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>


<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>


<link rel="stylesheet" type="text/css" href="css/vendor/css/styles.css" />
<link rel="stylesheet" type="text/css" href="css/vendor/css/wrap-styles.css" />
	<script
		src="/ttk/scripts/vendor/jquery-3.2.1.min.js"></script>
	<script
		src="/ttk/scripts/vendor/moment.min.js"></script>
	<script
		src="/ttk/scripts/vendor/bootstrap-datetimepicker.js"></script>
	<script
		src="/ttk/scripts/vendor/jquery-ui.js"></script>
	<script
		src="/ttk/scripts/vendor/jquery.easing.1.3.js"></script>
	<script
		src="/ttk/scripts/vendor/modernizr-2.8.3.min.js"></script>
	<script
		src="/ttk/scripts/vendor/ladda.jquery.min.js"></script>
	<script
		src="/ttk/scripts/vendor/spin.min.js"></script>
	<script
		src="/ttk/scripts/vendor/ladda.min.js"></script>
	<!-- Bootstrap Core JavaScript -->
	<script
		src="/ttk/scripts/vendor/popper.min.js"></script>
	<script
		src="/ttk/scripts/vendor/bootstrap.min.js"></script>
	<script
		src="/ttk/scripts/vendor/toastr.min.js"></script>
	<script
		src="/ttk/scripts/vendor/bootstrap-typeahead.min.js"></script>
	<script
		src="/ttk/scripts/vendor/jquery.mCustomScrollbar.concat.min.js"></script>
	<script
		src="/ttk/scripts/vendor/jQuery.scrollSpeed.js"></script>
	<script type="text/javascript" src="/ttk/scripts/preauth/dashboard.js"></script>

<head>

	<style type="text/css">
		.vd-dash-action-counts .action-elements {
    display: inline-block;
    width:7.2%;
    text-align: center;
    background: #FFFFFF;
    box-shadow: 0 3px 3px 2px rgba(0, 0, 0, 0.05);
    border-radius: 2px;
    vertical-align: middle;
    padding: 1%;
    margin-right: 1%;
  /*    height: 135px;*/
    position: relative;
	}
	.vd-dash-action-counts{
    width: 100%;
	}
	
	select.form-control{
		height: 80px!important;
	}
	
	#spanhvp:HOVER{
	text-decoration: underline;
	}
	
	 
	/* @import url('https://fonts.googleapis.com/css?family=Lora:400,700i'); */
	

 
.circular-progress-bar {
    position: relative;
    margin: 0 auto;
}

.progress-percentage, .progress-text {
    position: absolute;
    width: 100%;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
    padding: 0px 60px;
}

.progress-percentage {
	font-size: 40px;
    transform: translate(-50%, -85%);
}

.progress-text {
    transform: translate(-50%, 0%);
    color: #585858;
    font-size: 21px;
}

#background{
    position: absolute;
    z-index: 0.8;
    /* background: white; */
    display: block;
    /* min-height: 50%; */
    min-width: 90%;
    color: yellow;
    top: 10%;
    left: 30%;
    opacity: 0.1;
}	
/* #bg-text
{
    color:lightgrey;
    font-size:120px;
    transform:rotate(300deg);
    -webkit-transform:rotate(300deg);
} */	
.vd-dash-action-counts .action-elements h5 {
    color: #5A5A5A;
    font-weight: bold;
    text-transform: capitalize;
    font-size: 12px;
    cursor: pointer;
    margin-bottom: 10px;
    height: 45%;
}	

.vd-dash-action-counts .action-elements h1 {
    font-weight: 300;
    cursor: pointer;
    margin-bottom: 0;
    font-size: 30px;
    text-transform: capitalize;
    -webkit-transition: all 0.5s ease;
    -moz-transition: all 0.5s ease;
    -o-transition: all 0.5s ease;
    transition: all 0.5s ease;
}
	</style>
	
</head>
<body>
<% boolean accessFlag = TTKCommon.isAuthorized(request, "ViewUserInfo"); %>
	<div class="vd-page-wrapper">
	<html:form action="/PreAuthDoctorDashboardDeaultAction.do" >
	<html:errors/>
	
		<div id="dashboadCollapse" data-children=".item" class="vd-dash-collapse ">
			<div class="item">
				<a data-toggle="collapse" data-parent="#dashboadCollapse"
					href="#dCollapseItem" aria-expanded="true"
					aria-controls="dashboadCollapse"> <img
					src="/ttk/images/minimize_window.svg"
					title="minimize_window" alt="minimize_window" class="vd-collapse-icon">
				</a>
				<div id="dCollapseItem" class="collapse show" role="tabpanel">
					<div class="">
						<div class="vd-dash-action-counts">
						
						<div class="action-elements clr-error">
								<h5 >For Decision</h5>
								<h1 style="z-index:999;position:relative;right:17px;text-decoration : none;font-size:25px; ">
								<span style="text-decoration: none;cursor: default;"><b><bean:write name="frmDashboardList" property="coding_cases"/></b>/
								<b></b><span id="spanhvp"  style="text-decoration: none;cursor: default;"></b>
								<bean:write name="frmDashboardList" property="highValuePA"/></span></span>
								</h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Inprogress cases"><img
										src="/ttk/images/info_icon.svg"
										 title="info_icon" alt="info_icon">
									</a>
								</div>
							</div>
					
							 <div class="action-elements clr-success">
								<h5>Fresh</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="fress_case"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Fress cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div> 
							
							 <div class="action-elements clr-success">
								<h5>Enhance</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="enhance_inp_cases"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Enhance cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div> 
							 <div class="action-elements clr-success">
								<h5>Appeal</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="appeal_inp_cases"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Appeal cases"><img
										src="/ttk/images/info_icon.svg"
										alt="info_icon">
									</a>
								</div>
							</div> 
							
							<div class="action-elements clr-info">
								<h5>Shortfall</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="shortfall_cases"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Shortfall cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div>
							<div class="action-elements clr-info">
								<h5>Shortfall Responded</h5> <%-- ${dashBoardCountDetails.mailDetailsVO.loadPercentage} --%>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="shortfall_respond_ases"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Shortfall Responded cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div>
							<div class="action-elements clr-success">
								<h5>Enhancement Shortfall Raised</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="enhanced_shortfall_cases"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Enhancement Shortfall Raised cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div>
							<div class="action-elements clr-success">
								<h5>Shortfall Received Through Enhancement</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="enhanced_shrtfal_res_cases"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Shortfall Received Through Enhancement cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div>
							
							<div class="action-elements clr-success">
								<h5>Logged In Doctors</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="doctor_logedin_case"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Logged In Doctors"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div>
							 <div class="action-elements clr-success">
								<h5>Approved</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="approved_cases"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Approved Cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div> 
						
							 <div class="action-elements clr-success">
								<h5>Rejected</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="rejected_cases"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays Rejected cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div>  
							
			
							
					 		<div class="action-elements clr-success">
								<h5>For Resolution</h5>
								<h1 style="text-decoration: none;cursor: default;"><bean:write name="frmDashboardList" property="forResolution"/></h1>
								<div class="info-icon">
									<a href="#" data-toggle="tooltip" data-placement="Bottom"
										title="Displays For Resolution cases"><img
										src="/ttk/images/info_icon.svg"
										title="info_icon" alt="info_icon">
									</a>
								</div>
							</div> 
						</div>
			
						
					</div>
				</div>
			</div>
		</div>
		 <input type="hidden"  id="docUserSeqirityFlag" value="<%= accessFlag %>">
		</html:form>
		<div class="vd-page-body-tile">User Information</div>
		
	
  		<div class="row">
  		
    	<div class="col-lg-4 col-md-4">
	    	<ttk:HtmlGrid1 name="tDtvUserInfo" />
	    	
	    </div>
	     <div class="col-lg-4 col-md-4">
	    	<ttk:HtmlGrid1 name="tDtvUserInfo2" />
	    </div>
	    <div class="col-lg-4 col-md-4">
	    	<ttk:HtmlGrid1 name="tDtvUserInfo3" />
	    </div>
	  
	    
	    <%-- <div class="col-lg-3">
	    	<ttk:HtmlGrid1 name="tDtvUserInfo4" />
	    </div>  --%>
	    </div>
		
	</div>
</body>
<script>
  var myVar = setInterval(myTimer, 20000); 
function myTimer() {

 var flag=document.getElementById("docUserSeqirityFlag").value;
    if(flag){
    document.forms[0].mode.value="doDisplaydoctordashboardPreDetails";
	 document.forms[0].action = "/PreDoctorDashboardPreauthDetails.do";
	 document.forms[0].submit();
    }else{
    	 document.forms[0].mode.value="doDisplayDoctorDashboardDefault";
    	 document.forms[0].action = "/PreAuthDoctorDashboardDeaultAction.do";
    	 document.forms[0].submit();
        
        }
}


</script>