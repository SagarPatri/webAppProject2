isIE = (document.all)? true:false

//This function gets X position of the event(Mouse Click)
function HmouseX(e) {
	if (!isIE) {var HmouseX=e.pageX; var HmouseY=e.pageY}
	if (isIE) {var HmouseX=event.x; var HmouseY=event.y}
	return(HmouseX);
}

//This function gets Y position of the event(Mouse Click)
function HmouseY(e) {
	if (!isIE) {var HmouseX=e.pageX; var HmouseY=e.pageY}
	if (isIE) {var HmouseX=event.x; var HmouseY=event.y}
	return(HmouseY);
}

//This function writes iFrame to the document
function writeHCalendarIFrame(){
	//document.write("<iframe id='calendar' name='calendar' src='../calendar.html' scrolling='no' frameborder='0' allowtransparency='true' style='position:absolute;top:200px;left:200px;width:250px;height:100px; z-index: 1000;'></iframe>");
	document.write("<iframe id='Hcalendar' name='Hcalendar' src='/ttk/Hcalendar.html' scrolling='no' frameborder='0'></iframe>");
}

writeHCalendarIFrame();


var weekend = [0,6];
var gNow;
var ggWinCal;
var HcalendarText = "";
var hideCal = 0;
var xOffset = -90;
var yOffset = 10;
var time;
//var timezoneOffset = gNow.getTimezoneOffset() * 6e4;

// Default Date Format
var gDefaultDateFormat = 'DD/MM/YYYY';

isNav = (navigator.appName.indexOf("Netscape") != -1) ? true : false;
isIE = (navigator.appName.indexOf("Microsoft") != -1) ? true : false;

HCalendar.Months = ["Muh", "Saf", "RAw", "RAk", "JAw", "JAk", "Raj", "Sha", "Ram", "Sya", "DhQ", "DhH"];

// Non-Leap year Month days..
HCalendar.DOMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
// Leap year Month days..
HCalendar.lDOMonth = [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
 

function InitializeHDate(){
	gNow = new HijriDate();
}


// Code modified to capture the date value ( p_day has been included)
function HCalendar(p_item, p_WinCal, p_day, p_month, p_year, p_format) {
	if ((p_month == null) && (p_year == null))	return;

	if (p_WinCal == null)
		this.gWinCal = ggWinCal;
	else
		this.gWinCal = p_WinCal;

	if (p_month == null) {
		this.gMonthName = null;
		this.gMonth = null;
		this.gYearly = true;
	} else {
		this.gMonthName = HCalendar.get_month(p_month);
		this.gMonth = new Number(p_month);
		this.gYearly = false;
	}

	// Code added to capture the date value
	this.gDay = p_day;
	this.gYear = p_year;
	this.gFormat = p_format;

	// Customize your Calendar here.....
	this.gTextColor = "#000000";
	this.gMainTableBgColor = "#dfebe6";
	this.gHeaderBgColor = "#03832c"; // The Year Display
	this.gHeaderFontColor = "#FFFFFF";
	this.gHeaderFontFace = "Verdana, Arial, Helvetica, sans-serif";
	this.gHeaderFontSize = "2";
	this.gLinkColor="#000000";

	// Display of Weekdays ( S M T W T F S )
	this.gDayFontFace = "Verdana, Arial, Helvetica, sans-serif";
	this.gDayFontSize = "2";

	// Display of Dates ( 1 - 31 )
	this.gDateFontFace = "Verdana, Arial, Helvetica, sans-serif";
 	this.gDateFontSize = "1";
	this.gDateBgColor = "#FFFFFF";
	this.gWeekendBgColor = "#EEEEEE";

	this.gSelectedDateFontColor = "#FF0000";

	// Images used in the Calendar
	this.gImgYearDcr = "/ttk/images/First.gif";
	this.gImgYearInc = "/ttk/images/Last.gif";
	this.gImgMonDcr = "/ttk/images/Prev.gif";
	this.gImgMonInc = "/ttk/images/Next.gif";


	this.gReturnItem = p_item;


}

HCalendar.get_month = HCalendar_get_month;
HCalendar.get_daysofmonth = HCalendar_get_daysofmonth;
HCalendar.calc_month_year = HCalendar_calc_month_year;
HCalendar.print = HCalendar_print;
HCalendar.getHDate = HCalendar_get_Hdate;

function HCalendar_get_Hdate(){
	return  HCalendar.hDate();
}

function HCalendar_get_month(monthNo) {
	return HCalendar.Months[monthNo];
}

function HCalendar_get_daysofmonth(monthNo, p_year) {
	return HijriDate.daysInMonth((p_year - 1) * 12 + monthNo);
}

function HCalendar_print() {
	ggWinCal.print();
}

function HCalendar_calc_month_year(p_Month, p_Year, incr) {
	/*
	Will return an 1-D array with 1st element being the calculated month
	and second being the calculated year
	after applying the month increment/decrement as specified by 'incr' parameter.
	'incr' will normally have 1/-1 to navigate thru the months.
	*/
	var ret_arr = new Array();

	if (incr == -1) {
		// B A C K W A R D
		if (p_Month == 0) {
			ret_arr[0] = 11;
			ret_arr[1] = parseInt(p_Year,10) - 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month,10) - 1;
			ret_arr[1] = parseInt(p_Year,10);
		}
	} else if (incr == 1) {
		// F O R W A R D
		if (p_Month == 11) {
			ret_arr[0] = 0;
			ret_arr[1] = parseInt(p_Year,10) + 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month,10) + 1;
			ret_arr[1] = parseInt(p_Year,10);
		}
	}

	return ret_arr;
}

// This is for compatibility with Navigator 3, we have to create and discard one object before the prototype object exists.
new HCalendar();

HCalendar.prototype.getMonthlyHCalendarCode = function() {
	var vCode = "";
	var vHeader_Code = "";
	var vData_Code = "";

	// Begin Table Drawing code here..
	vHeader_Code = this.cal_header();
	vData_Code = this.cal_data();
	vCode = vCode + vHeader_Code + vData_Code;

	return vCode;
}

HCalendar.prototype.show = function() {
	var vCode = "";

	// Show navigation buttons
	var prevMMYYYY = HCalendar.calc_month_year(this.gMonth, this.gYear, -1);
	var prevMM = prevMMYYYY[0];
	var prevYYYY = prevMMYYYY[1];

	var nextMMYYYY = HCalendar.calc_month_year(this.gMonth, this.gYear, 1);
	var nextMM = nextMMYYYY[0];
	var nextYYYY = nextMMYYYY[1];

	this.wwrite("<TABLE WIDTH='147' BORDER='0' CELLSPACING='1' CELLPADDING='0' BGCOLOR='"+ this.gMainTableBgColor +"'><TR><TD>");

	// Code modofied to capture the date value (gDay being included)
	var iframeDivObj = parent.frames["Hcalendar"].document.getElementById("HcalendarObj");

	this.wwrite("<TABLE WIDTH='147' BORDER=0 CELLSPACING=1 CELLPADDING=1 ALIGN='CENTER'><TR><TD WIDTH='10%' ALIGN='CENTER' BGCOLOR='"+ this.gHeaderBgColor +"'>");
	this.wwrite("<A HREF=\"" +
		"javascript:parent.HcalendarText = ''; parent.HBuild('" + this.gReturnItem + "', '" + this.gDay + "','" + this.gMonth + "', '" + (parseInt(this.gYear,10)-1) + "', '" + this.gFormat + "'); \"><IMG SRC='"+ this.gImgYearDcr +"' WIDTH='5' HEIGHT='8' BORDER='0'><\/A></TD><TD WIDTH='10%' ALIGN='CENTER' BGCOLOR='"+ this.gHeaderBgColor +"'>");
	this.wwrite("<A HREF=\"" +
		"javascript:parent.HcalendarText = ''; parent.HBuild(" + "'" + this.gReturnItem + "', '" + this.gDay + "','" + prevMM + "', '" + prevYYYY + "', '" + this.gFormat + "'" + ");" + " \"><IMG SRC='"+ this.gImgMonDcr +"' WIDTH='4' HEIGHT='8' BORDER='0'><\/A></TD><TD  WIDTH='60%' ALIGN='CENTER' BGCOLOR='"+ this.gHeaderBgColor +"'><FONT FACE='"+ this.gHeaderFontFace +"' SIZE='"+ this.gHeaderFontSize +"' COLOR='"+ this.gHeaderFontColor +"'>");
	this.wwriteA(this.gMonthName + " " + this.gYear);
	this.wwrite("</FONT></TD><TD WIDTH='10%' ALIGN=center BGCOLOR='"+ this.gHeaderBgColor +"'>");
	this.wwrite("<A HREF=\"" +
		"javascript:parent.HcalendarText = ''; parent.HBuild(" + "'" + this.gReturnItem + "', '" + this.gDay + "','" + nextMM + "', '" + nextYYYY + "', '" + this.gFormat + "'" +	");" + " \"><IMG SRC='"+ this.gImgMonInc +"' WIDTH='4' HEIGHT='8' BORDER='0'><\/A></TD><TD WIDTH='10%' ALIGN='CENTER' BGCOLOR='"+ this.gHeaderBgColor +"'>");
	this.wwrite("<A HREF=\"" +
		"javascript:parent.HcalendarText = ''; parent.HBuild(" + "'" + this.gReturnItem + "', '" + this.gDay + "','" + this.gMonth + "', '" + (parseInt(this.gYear,10)+1) + "', '" + this.gFormat + "'" +	");" + " \"><IMG SRC='"+ this.gImgYearInc +"' WIDTH='5' HEIGHT='8' BORDER='0'><\/A></TD></TR></TABLE>");

	// Get the complete calendar code for the month..
	vCode = this.getMonthlyHCalendarCode();
	this.wwrite(vCode);

	this.wwrite("</TABLE>");
}

HCalendar.prototype.wwrite = function(wtext) {
	//this.gWinCal.document.writeln(wtext);
	HcalendarText += wtext;
}

HCalendar.prototype.wwriteA = function(wtext) {
	//this.gWinCal.document.write(wtext);
	HcalendarText += wtext;
}

HCalendar.prototype.cal_header = function() {
	var vCode = "";
	vCode = vCode + "<TABLE WIDTH='147' BORDER='0' CELLSPACING='0' CELLPADDING='2'>";
	vCode = vCode + "<TR ALIGN='CENTER'>";
	vCode = vCode + "<TD><FONT FACE='"+ this.gDayFontFace +"' SIZE='"+ this.gDayFontSize +"'>A</FONT></TD>";
	vCode = vCode + "<TD><FONT FACE='"+ this.gDayFontFace +"' SIZE='"+ this.gDayFontSize +"'>I</FONT></TD>";
	vCode = vCode + "<TD><FONT FACE='"+ this.gDayFontFace +"' SIZE='"+ this.gDayFontSize +"'>T</FONT></TD>";
	vCode = vCode + "<TD><FONT FACE='"+ this.gDayFontFace +"' SIZE='"+ this.gDayFontSize +"'>A</FONT></TD>";
	vCode = vCode + "<TD><FONT FACE='"+ this.gDayFontFace +"' SIZE='"+ this.gDayFontSize +"'>K</FONT></TD>";
	vCode = vCode + "<TD><FONT FACE='"+ this.gDayFontFace +"' SIZE='"+ this.gDayFontSize +"'>J</FONT></TD>";
	vCode = vCode + "<TD><FONT FACE='"+ this.gDayFontFace +"' SIZE='"+ this.gDayFontSize +"'>S</FONT></TD>";
	vCode = vCode + "</TR>";
	vCode = vCode + "</TABLE>";

	return vCode;
}

HCalendar.prototype.cal_data = function() {
	var vDate = new HijriDate();
	vDate.setDate(1);
	vDate.setMonth(this.gMonth);
	vDate.setFullYear(this.gYear);

	var vFirstDay=vDate.getDay();
	var vDay=1;
	var vLastDay=HCalendar.get_daysofmonth(this.gMonth, this.gYear);
	var vOnLastDay=0;
	var vCode = "";

	/*
	Get day for the 1st of the requested month/year..
	Place as many blank cells before the 1st day of the month as necessary.
	*/
	vCode = vCode + "<TABLE WIDTH='147' BORDER='0' CELLSPACING='1' CELLPADDING='0'>";
	vCode = vCode + "<TR>";
	for (i=0; i<vFirstDay; i++) {
		vCode = vCode + "<TD ALIGN='CENTER' WIDTH='21' HEIGHT='21'" + this.write_weekend_string(i) + "><FONT FACE='"+ this.gDateFontFace +"' SIZE='"+ this.gDateFontSize +"'><B>&nbsp;</B></FONT></TD>";
	}


	// Write rest of the 1st week
	// changeFlag has been included to trigger the onChange event in the concerned screens
	for (j=vFirstDay; j<7; j++) {
		vCode = vCode + "<TD ALIGN='CENTER' WIDTH='21' HEIGHT='21'" + this.write_weekend_string(j) + "><FONT FACE='"+ this.gDateFontFace +"' SIZE='"+ this.gDateFontSize +"'><B>" +
			"<A HREF='#' " +
				"onClick=\"self.parent.document." + this.gReturnItem + ".value='" +
				this.format_data(vDay) +
				"'; self.parent.document." + this.gReturnItem + ".focus(); if(self.parent.changeFlag) self.parent.changeFlag='true'; parent.document.getElementById('Hcalendar').style.display = 'none'; self.parent.HcalendarText = ''; self.parent.hideCal = 0; \">" +
				this.format_day(vDay) +
			"</A>" +
			"</B></FONT></TD>";
		vDay=vDay + 1;
	}
	vCode = vCode + "</TR>";

	// Write the rest of the weeks
	// changeFlag has been included to trigger the onChange event in the concerned screens
	for (k=2; k<7; k++) {
		vCode = vCode + "<TR>";

		for (j=0; j<7; j++) {
if(vDay!="&nbsp;"){
			vCode = vCode + "<TD ALIGN='CENTER' WIDTH='21' HEIGHT='21'" + this.write_weekend_string(j) + "><FONT FACE='"+ this.gDateFontFace +"' SIZE='"+ this.gDateFontSize +"'><B>" +
				"<A HREF='#' " +
					"onClick=\"self.parent.document." + this.gReturnItem + ".value='" +
					this.format_data(vDay) +
					"'; self.parent.document." + this.gReturnItem + ".focus();if(self.parent.changeFlag) self.parent.changeFlag='true'; parent.document.getElementById('Hcalendar').style.display = 'none'; self.parent.HcalendarText = ''; self.parent.hideCal = 0; \">" +
				this.format_day(vDay) +
				"</A>" +
				"</B></FONT></TD>";
}else{
			vCode = vCode + "<TD ALIGN='CENTER' WIDTH='21' HEIGHT='21'" + this.write_weekend_string(j) + "><FONT FACE='"+ this.gDateFontFace +"' SIZE='"+ this.gDateFontSize +"' color='#aaaaaa'>" +
				this.format_day(vDay) +
				"</FONT></TD>";
}

			if(vDay != "&nbsp;")
				vDay=vDay + 1;

			if (vDay > vLastDay) {
				vDay = "&nbsp;"
				/*vOnLastDay = 1;
				break;*/
			}
		}

		/*if (j == 6)
			vCode = vCode + "</TR>";
		if (vOnLastDay == 1)
			break;*/
	}

	/*// Fill up the rest of last week with proper blanks, so that we get proper square blocks
	for (m=1; m<(7-j); m++) {
		if (this.gYearly)
			vCode = vCode + "<TD ALIGN='CENTER' WIDTH='21' HEIGHT='21'" + this.write_weekend_string(j+m) +
			"><FONT FACE='"+ this.gDateFontFace +"' SIZE='"+ this.gDateFontSize +"'><B>&nbsp;</B></FONT></TD>";
		else
			vCode = vCode + "<TD ALIGN='CENTER' WIDTH='21' HEIGHT='21'" + this.write_weekend_string(j+m) +
			"><FONT FACE='"+ this.gDateFontFace +"' SIZE='"+ this.gDateFontSize +"'><B>&nbsp;</B></FONT></TD>";
	}*/
	vCode = vCode + "</TABLE>";
	return vCode;
}

HCalendar.prototype.format_day = function(vday) {
	// Code modified to capture the date value (the date in the text field is shown highlighted)
	if (vday == this.gDay)
		return ("<FONT COLOR='"+ this.gSelectedDateFontColor +"'>" + vday + "</FONT>");
	else
		return (vday);
}

HCalendar.prototype.write_weekend_string = function(vday) {
	var i;

	// Return special formatting for the weekend day.
	for (i=0; i<weekend.length; i++) {
		if (vday == weekend[i])
			return (" BGCOLOR=\"" + this.gWeekendBgColor + "\"");
	}

	return (" BGCOLOR=\"" + this.gDateBgColor + "\"");
}

HCalendar.prototype.format_data = function(p_day) {
	var vData;
	var vMonth = 1 + this.gMonth;
	vMonth = (vMonth.toString().length < 2) ? "0" + vMonth : vMonth;
	var vMon = HCalendar.get_month(this.gMonth).substr(0,3).toUpperCase();
	var vFMon = HCalendar.get_month(this.gMonth).toUpperCase();
	var vY4 = new String(this.gYear);
	var vY2 = new String(this.gYear.substr(2,2));
	var vDD = (p_day.toString().length < 2) ? "0" + p_day : p_day;

	switch (this.gFormat) {
		case "MM\/DD\/YYYY" :
			vData = vMonth + "\/" + vDD + "\/" + vY4;
			break;
		case "MM\/DD\/YY" :
			vData = vMonth + "\/" + vDD + "\/" + vY2;
			break;
		case "MM-DD-YYYY" :
			vData = vMonth + "-" + vDD + "-" + vY4;
			break;
		case "MM-DD-YY" :
			vData = vMonth + "-" + vDD + "-" + vY2;
			break;

		case "DD\/MON\/YYYY" :
			vData = vDD + "\/" + vMon + "\/" + vY4;
			break;
		case "DD\/MON\/YY" :
			vData = vDD + "\/" + vMon + "\/" + vY2;
			break;
		case "DD-MON-YYYY" :
			vData = vDD + "-" + vMon + "-" + vY4;
			break;
		case "DD-MON-YY" :
			vData = vDD + "-" + vMon + "-" + vY2;
			break;

		case "DD\/MONTH\/YYYY" :
			vData = vDD + "\/" + vFMon + "\/" + vY4;
			break;
		case "DD\/MONTH\/YY" :
			vData = vDD + "\/" + vFMon + "\/" + vY2;
			break;
		case "DD-MONTH-YYYY" :
			vData = vDD + "-" + vFMon + "-" + vY4;
			break;
		case "DD-MONTH-YY" :
			vData = vDD + "-" + vFMon + "-" + vY2;
			break;

		case "DD\/MM\/YYYY" :
			vData = vDD + "\/" + vMonth + "\/" + vY4;
			break;
		case "DD\/MM\/YY" :
			vData = vDD + "\/" + vMonth + "\/" + vY2;
			break;
		case "DD-MM-YYYY" :
			vData = vDD + "-" + vMonth + "-" + vY4;
			break;
		case "DD-MM-YY" :
			vData = vDD + "-" + vMonth + "-" + vY2;
			break;

		default :
			vData = vMonth + "\/" + vDD + "\/" + vY4;
	}

	return vData;
}

// Code modified to capture the date value (p_day has been included)
function HBuild(p_item,  p_day,  p_month, p_year, p_format) {
	var p_WinCal = ggWinCal;

	// Code modified to capture the date value (p_day has been included)
	gCal = new HCalendar(p_item, p_WinCal, p_day,  p_month, p_year, p_format);

	// Show function
	gCal.show();

	window.frames["Hcalendar"].document.getElementById("HcalendarObj").innerHTML = HcalendarText;
}

function show_Hcalendar() {
	HcalendarText = "";
	/*
		p_month : 0-11 for Jan-Dec; 12 for All Months.
		p_year	: 4-digit year
		p_format: Date format (mm/dd/yyyy, dd/mm/yy, ...)
		p_item	: Return Item.
	*/
	p_item = arguments[1];
	InitializeHDate();
	
	// Added this piece of code to fix the error that is caused when the element name contains period, and the user
	// tries to navigate to other months or years.
	// The application must send the control as formname.elements['elementname'] to the calendar function in such cases.
	// The Calendar's Build function accepts the first paramenter as a string and hence the problem occurs when this code is not added.
	if((p = p_item.indexOf('[\'')) != -1 && (q = p_item.indexOf('\']')) != -1){
		var p_form = eval("document.forms['"+p_item.substring(0,p_item.indexOf("."))+"']");
		var p_controlname = p_item.substring(p+2,q);
		for(var i=0;i<p_form.elements.length;i++){
			if(p_form.elements[i].name==p_controlname) break;
		}
		// formname.elements['elementname'] is transformed to formname.elements[n]; where n is the index of the concerned element.
		p_item = p_form.name+".elements["+i+"]";
	}
	//--

	// Code added to capture the date value
	if (arguments[2] == "") 	{
		p_day = gNow.getDate();
		p_month = new String(gNow.getHMonth());
		p_year = new String(gNow.getHFullYear().toString());
	}
	else {
		var dateValue = arguments[2];
		var dateArray = dateValue.split( (dateValue.indexOf("/") != -1) ? "/" : "-");
		p_day = new Number(dateArray[0]);
		p_month = new Number(dateArray[1])-1;
		p_year = new String(dateArray[2]);

		if( isNaN(p_day) || p_day < 1 || p_day > 31 || isNaN(p_month) || p_month == -1 || p_month<0 || p_month > 11 || p_year.length != 4 || isNaN(p_year) ) {
			p_day = gNow.getDate();
			p_month = new String(gNow.getHMonth());
			p_year = new String(gNow.getHFullYear().toString());
		}

	}



	// ----------------------------------------------------------------------------

	if (arguments[3] == null || arguments[3] == "")
		p_format = gDefaultDateFormat;
	else
		p_format = arguments[3];


	var mouseXPos = HmouseX(arguments[4]);
	var mouseYPos = HmouseY(arguments[4]);

	var docSize = getDocumentSize();
	HcalendarPos = mouseYPos+arguments[6];
	if(HcalendarPos>docSize.height){
		mouseYPos = mouseYPos - arguments[6] - 20;
	}
	if(mouseYPos<0){
		mouseYPos = mouseYPos + arguments[6] + 20;
	}

	HBuild(p_item, p_day,  p_month, p_year, p_format);

	var HcalendarObj = document.getElementById("Hcalendar");
	var iframeDivObj = window.frames["Hcalendar"].document.getElementById("HcalendarObj");
	HcalendarObj.style.height = iframeDivObj.offsetHeight + "px";
	HcalendarObj.style.top = mouseYPos + yOffset + "px";
	HcalendarObj.style.left = mouseXPos + xOffset + "px";
	HcalendarObj.style.width = arguments[5] + "px";
	HcalendarObj.style.height = arguments[6] - 3 + "px";
	HcalendarObj.style.display = "block";

	//self.parent.document.onclick=showHideCalendar;
	self.document.onclick=showHideHCalendar;

}

//Hide iFrame Calendar
function showHideHCalendar(){
	var HcalendarObj = document.getElementById("Hcalendar");
	if(hideCal==1 && HcalendarObj.style.display == "block"){
		HcalendarObj.style.display = "none"
		hideCal = 0;
	}
	if(HcalendarObj.style.display == "block")
		hideCal = 1;
}

function scrollTest(){
	if(document.getElementById('contentArea'))
		document.getElementById('contentArea').onscroll = showHideHCalendar;
}

window.setInterval("scrollTest()",10);



	// ------ E N D ----------------------------------------------------------------------


