<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile" errorPage="/ttk/common/error.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>

<logic:equal name="pbmRightClick" value="true">
<html oncontextmenu="return false;">
<!-- <html> -->
</logic:equal>
<logic:notEqual name="pbmRightClick" value="true">
<html>
</logic:notEqual>
<head>
<title>AlKoot Administrator</title>
<script type="text/javascript" src="/ttk/scripts/utils.js"></script>
<script type="text/javascript" src="/ttk/scripts/trackdatachanges.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/common/healthcarelayout.js"></SCRIPT>
<link href="/ttk/styles/OnlineDefault.css" media="screen" rel="stylesheet"></link>
<style type="text/css">
  
	#onlineMenus   {
            margin:0;
            margin-left:5px;
            margin-right: -5px;
            padding:0px;
            list-style:none;
             /* background-color: #91C85F;  */
            background-color:  rgb(91, 204, 128);
          /*   #BAD2F4;  */
            height:20px; 
            width:100%;
            border:solid 1px rgb(91, 204, 128);/*   #BAD2F4;  */
            border-width:1px 1px 1px 0px;
            font-family:Arial, Helvetica, sans-serif;
        }
	#onlineMenus li{
            display:inline; 
            position:relative; 
            float:left; 
            margin-right: -1px;
        }
	#onlineMenus li a { 
            display:block; 
            float:left;              
            line-height:25px; 
            padding:0 24px 0 24px; 
            text-decoration:none;           
            font-weight:normal; 
            font-size:13px; 
            color:black;
            border-left:solid 1px black; 
        }
	#onlineMenus li a:hover {
            color: blue; 
            background: white; 
        }
      
	#pbmCurrentTab{
	 color: blue; 
     background: white; 
     cursor: default;
	}
	
	#onlineMenus li ul { 
            margin:0;            
            padding:0 5px; 
            line-height:none; 
            position:absolute; 
            top:29px; left:0; 
           /*  border:solid 1px yellowgreen;  */
           border-width:0px 1px 1px 1px;  
            width:180px; display:none; 
            background:#FFFFFF;
        }
	#onlineMenus li:hover ul { display:block}
	
	#onlineMenus li ul li { 
            display:block;  
            border-bottom:solid 1px #dbdcd9; 
            width:98%;
            padding:0 0 0 10px;
            background:url(/ttk/images/arrow.gif) no-repeat 3px 12px; 
        }
	#onlineMenus li ul li:last-child { border-bottom:0px;}
	#onlineMenus li ul a { 
            border-width:0px; 
            color:#909090; 
            padding:0 5px 0 0; 
            background-color:transparent;
        }
	
	#onlineMenus li ul li a:hover { 
            color: blue;
            background-color: whitesmoke;
        }
        #onlineMenus li ul li:hover { 
        background-color: whitesmoke;
        border-right: solid 1px #91C85F;
        }
        
        
  #pbmContainerBody{
                border: 0;
                padding: 0;
                margin: 0;
                background-color: white;
            }  
            #pbmContainerTable{
                border: 1px solid greenyellow;
                border-collapse: collapse;
                width: 100%;
                height: 100%;
                margin: 0;
                padding: 0;
            }
            #pbm{
                border-bottom: 1px solid greenyellow;
                border-collapse: collapse;
                width: 100%;
                height: 55px;            
            }
            #pbmDropDownMenus{
                border-bottom: 1px solid greenyellow;
                border-collapse: collapse;
                width: 100%;
                height: 28px;
                text-align: left;
                color: aliceblue;
                font-size: 15px;
                background-color: #91C85F;
            }
           
            #pbmTabLinks{
                border-bottom: 1px solid greenyellow;
                border-collapse: collapse;
                width: 100%;
                height: 20px;
                text-align: left;
                font-size: 14px;
               /* background-color: #91C85F; */
                background-color:  rgb(91, 204, 128);
               /*  #BAD2F4; */
            }
            #pbmSubContainerTable{
                border-collapse: collapse;
                width: 100%;
                height: 100%;
                margin: 0;
                padding: 0;
            }
            #pbmLeftImg{
                border-collapse: collapse;
                min-height: 600px;
                vertical-align: top;
                background-color: azure;
                background-image: url("/ttk/images/Insurance/content.png");
            }
            #pbmRightImg{
                border-collapse: collapse;
                min-height: 600px;
                vertical-align: top;
                background-color: azure;
                background-image: url("/ttk/images/Insurance/content.png");
            }            
            #pbmMidBody{
                width: 90%;
                height: 600px;
                vertical-align: top;  
                /* font-family: Verdana, Arial, Helvetica, sans-serif; */             
            }
            #pbmFooter{
                border-top: 1px solid greenyellow;
                border-collapse: collapse;
                width: 100%;
                height: 20px;
                text-align: center;
                color: brown;
                font-size: 15px;
                background-color: cornsilk;
            }
            #pbmDivMidBody{
            padding:0;
            margin:0;
            border:0;
            width: 100%;
            height: 100%;
            font-family: Verdana, Arial, Helvetica, sans-serif;
            }    
            .pbmHelpTabs{ 
           font-size: 11px;           
            }
        .pbmHelpTabs:HOVER {
	     color: blue;
	     cursor: pointer;
	     background-color: white;
          }
</style>
<script type="text/javascript">
var bAction = true;
ClientReset = true;
var JS_SecondSubmit=false;
</script>
<script type="text/javascript">
function modifyOnlineTabs(leftLink1,subLink1,tab1){
	if(!TrackChanges()) return false;
		document.forms[0].leftlink.value = leftLink1;	
		document.forms[0].sublink.value = subLink1;	
		document.forms[0].tab.value = tab1;
	document.forms[0].action="/ttkAction.do?mode=doOnlineDefault";
	document.forms[0].submit();
}
function pbmLogout(){
	document.forms[0].action="/LogoutOnlineAccessAction.do?mode=doLogoff";
	document.forms[0].submit();
}
function disableCtrlModifer(evt)
{
	var disabled = {a:0, x:0,w:0, n:0, F4:0, j:0};
	var ctrlMod = (window.event)? window.event.ctrlKey : evt.ctrlKey;
	var key = (window.event)? window.event.keyCode : evt.which;
	key = String.fromCharCode(key).toLowerCase();
	return (ctrlMod && (key in disabled))? false : true;
	
}//end of disableCtrlModifer(evt)

function onProviderLogin() {
	
	  document.forms[1].mode.value="doProviderLogin";	  
	  document.forms[0].leftlink.value = "Hospital Information";	  
	  document.forms[0].sublink.value = "Home";	
	  document.forms[0].tab.value = "Home";		
	  document.forms[1].action="/PbmPharmacyGeneralAction.do";
	  
	  document.forms[1].submit();	     
}
function onPartnerLogin() {
	
	  document.forms[1].mode.value="doPartnerLogin";	  
	  document.forms[0].leftlink.value = "Partner Information";	  
	  document.forms[0].sublink.value = "Home";	
	  document.forms[0].tab.value = "Home";		
	  document.forms[1].action="/PbmPharmacyGeneralAction.do";
	  document.forms[1].submit();	     
}
function resizePBMDocument(){
	var offSetHeight = 90;
	var obj = document.getElementById("pbmContainerTable");
	var docSize = getDocumentSize();
	obj.style.height = docSize.height - offSetHeight + "px";
	var contentArea = document.getElementById("contentArea");
	if(contentArea){
		contentArea.style.height = docSize.height - 137 + "px";
	}
}
function onCommonICDCodes()
{
	var openPage = "/OnlineProvHelpAction.do?mode=doCommonICDCodes";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);

}
</script>
<logic:empty name="UserSecurityProfile" scope="session">
	<script type="text/javascript">	
	openFullScreenMode("/ttk/onlineaccess.jsp?sessionexpired=true&loginType=HOS");
	</script>
	</logic:empty>
</head>
<!-- Changes Added onunload in body for Password Policy CR KOC 1235 -->
<body id="pbmContainerBody" onunload="javascript:logoutwindow(event);" onLoad="javascript:resizePBMDocument();TrackFormData('webBoard');handleFocus();window.history.forward(1);" onkeypress="return disableCtrlModifer(event);" onkeydown="return disableCtrlModifer(event);">
<table id="pbmContainerTable">
            <tr>
                <td id="pbmHeader"><tiles:insert attribute="header" ignore="true"/></td>
            </tr>
            <tr>
                <td id="pbmTabLinks"><tiles:insert attribute="tabLinks" ignore="true"/></td>
            </tr>
            <tr>
                <td>
                    <table id="pbmSubContainerTable">
                        <tr>
                        
                            <!-- <td  id="pbmLeftImg">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> -->
                            <td  id="pbmMidBody">
                            
                            <tiles:insert attribute="bodyContent" ignore="true"/>
                           <%-- <tiles:insert attribute="content" ignore="true"/> --%>
							</td>
                            <!-- <td  id="pbmRightImg">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> -->
                        </tr>
                    </table>

                </td>
            </tr>
            <tr>
               <!--  <td id="pbmFooter">&COPY;Vidal&nbsp;2017</td> -->
            </tr>
        </table>
</body>
</html>