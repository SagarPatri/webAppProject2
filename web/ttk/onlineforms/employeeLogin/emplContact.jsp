<%
/**
 * @ (#) hospsearch.jsp 12th Sep 2005
 * Project      : TTK HealthCare Services
 * File         : hospsearch.jsp
 * Author       : Srikanth H M
 * Company      : Span Systems Corporation
 * Date Created : 12th Sep 2005
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/EmployeeLogin/hospsearch.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
bAction = false;
var TC_Disabled = true;
</SCRIPT>

<style type="text/css">
.searchList_class{
    width: 80%;
    padding-left: 10%;
}
.searchData_class{
	width: 70%;
    padding-left: 15%;
}

.contact_table_class{
    width: 50%;
    padding-left: 2%;
    font-size: 15px;
    font-weight: bold;
    color: #fff;
    font-variant: normal;
}

.searchContainer{
    background-color: #F0F0F0;
    font-size: 17px;
    font-weight: bold;
    color: #000000;
    font-variant: normal;   
    border: 0px;  
}

.button_design_class {
	float: left;
    width: 16%;
    margin-left: 17%;
    height: 35px;
}


.query_div_class{
    width: 15%;
    padding-left: 19%;
}
.tableHeader_class{
color:red;
}
.tableDataClass td{
font-size: 14px;
}
</style>

<%

%>
<!-- S T A R T : Content/Form Area -->
<div class="contentArea" id="contentArea">
<h4 class="sub_heading" style="width:13%;">Contact Details</h4>

<br/><html:errors/><br/>
<html:form action="/HospitalSearchAction.do" >

	
	<div class="contact_table_class">
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
  		<tr>
	    	<td valign="bottom">Qatar: 800 2000<br></td>
	    	</tr><tr>
        	<td valign="bottom">Rest of the Word: (+974) 4040 2000<br></td>	
        	</tr>
        	<tr>	
        		<td valign="bottom">Email ID: customercare@alkoot-medical.com<br></td>
   			</tr>
   			<tr><td>Our Global Partners:</td></tr>
   			<tr>
   			<td>
	<table align="center" class="searchContainer tableDataClass" border="0" cellspacing="0" cellpadding="0">
  		<tr>
	    	<th align="left" class="tableHeader_class">Partner</th>
       		<th align="left" class="tableHeader_class">Region</th>
       		<th align="left" class="tableHeader_class">Contact No</th>
        </tr>
        <tr>
        <td>Premier Health Care</td>
        <td>Europe</td>
        <td>+49 40537976625</td>
        </tr>
        <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
        <tr>
        <td>Vidal Health</td>
        <td>India</td>
        <td>+91 80 49166777</td>
        </tr>
        <tr>
        <td></td>
        <td>UAE</td>
        <td>800 84325</td>
        </tr>
        
        <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
		<tr>
        <td>Globe Med</td>
        <td>KSA</td>
        <td>+966 92 00 20 252</td>
        </tr>
        <tr>
        <td></td>
        <td>Syria</td>
        <td>+963 11 218 1610</td>
        </tr>
        <tr>
        <td></td>
        <td>Kuwait</td>
        <td>+965 22 910 910</td>
        </tr>
        <tr>
        <td></td>
        <td>Bahrain</td>
        <td>+973 1753 07 02</td>
        </tr>
        <tr>
        <td></td>
        <td>Palestine</td>
        <td>+970 229 464 64</td>
        </tr>
        <tr>
        <td></td>
        <td>Egypt</td>
        <td>+202 330 86 252</td>
        </tr>
        <tr>
        <td></td>
        <td>Jordan</td>
        <td>+962 6 5806600</td>
        </tr>
        
        <tr>
        <td></td>
        <td>Lebanon</td>
        <td>+961 1 518 100 </td>
        </tr>
        
        <tr>
        <td></td>
        <td>Iraq</td>
        <td>+964 78 2777 2777</td>
        </tr>	
		 <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
        <tr>
        <td>Indus Health Plus</td>
        <td>Nepal</td>
        <td>+91 904 902 2222</td>
        </tr>
        <tr>
        <td></td>
        <td>Sri Lanka</td>
        <td></td>
        </tr>	
          <tr>
        <td></td>
        <td>Singapore</td>
        <td></td>
        </tr>
        <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
        <tr>
        <td>Premier Health for<br/>Medical Services</td>
        <td>Sudan</td>
        <td>+249 912 733 066</td>
        </tr>
        <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
		
        <tr>
        <td>MSO</td>
        <td>Africa</td>
        <td>(+27) 11 259 5403</td>
        </tr>
        <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
		
        <tr>
        <td>Tawasul Healthcare</td>
        <td>Pakistan</td>
        <td>(+92) 0302 28434941</td>
        </tr>
        <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
		
        <tr>
        <td>Medigo</td>
        <td>South America</td>
        <td>(+49) 30 255 585079</td>
        </tr>
        <tr>
        <td></td>
        <td>South Korea</td>
        <td></td>
        </tr>
        <tr>
        <td></td>
        <td>Japan</td>
        <td></td>
        </tr>
        <tr>
        <td></td>
        <td>Asia</td>
        <td></td>
        </tr>
        <tr>
        <td></td>
        <td>Hong Kong</td>
        <td></td>
        </tr>
        <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
		 <tr>
        <td>Healix</td>
        <td>UK</td>
        <td>(+44) 20 8608 4231</td>
        </tr>
        <tr>
        <td></td>
        <td>China</td>
        <td></td>
        </tr>
        <tr>
        <td colspan="3"><hr style="background: black; border: currentColor; border-image: none; height: 1px;" /></td>
		</tr>
		
        <tr>
        <td>Global Exel</td>
        <td>USA</td>
        <td>(+1) 877 345 8766</td>
        </tr>
</table>
</td>
</tr>
<tr>
        <td colspan="3"><br/></td>
        
</tr>

<tr style="">
<td>Office Address:</td>
</tr>
<tr>
<td>Al Koot Insurance & ReInsurance Company<br/>
PO Box: 24563, Doha - Qatar
</td>
</tr>
</table>
	</div>
	<br/><br/>
	<!-- <div class="query_div_class"><a href="#" onclick="javascript:onQuery();" class="button_design_class">HAVE A QUERY?</a></div>
	 -->
	
	
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	     <button type="button" name="Button" accesskey="h" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onQuery();"><u>H</u>AVE A QUERY?</button>&nbsp;
	    </td>
	  </tr>
	</table>

	
	
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
</div>
<!-- E N D : Main Container Table -->