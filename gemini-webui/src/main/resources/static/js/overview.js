$(document).ready(function() {
	$.getJSON('getTodayTraffic?host=10.19.7.66:17909', function(data) {
		var todayTrafficTemplate = $.templates("#todayTrafficTemplate");
		todayTrafficTemplate.link("#todayTrafficDiv", data);
	});
});