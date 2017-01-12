var minusDay = 0;

var minusDayMap = {
	'todayBtn' : 0,
	'yesterdayBtn' : 1,
	'last7daysBtn' : 7,
	'last30daysBtn' : 30
};

$(document).ready(function() {
	$.getJSON('getTraffic?host=work.asiainfo.com', function(data) {
		var trafficTemplate = $.templates("#trafficTemplate");
		trafficTemplate.link("#trafficDiv", data);
	});

	$("#chartsDiv").load("/overview/overview_charts.html");

	$("#timeSlotDiv").find('button').click(function() {
		$(this).siblings().attr('class', 'btn btn-white');
		$(this).attr('class', 'btn btn-success active');
		minusDay = minusDayMap[$(this).attr("id")];
		$("#chartsDiv").load("/overview/overview_charts.html");
	});
});
