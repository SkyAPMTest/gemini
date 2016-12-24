$(document).ready(function() {
	$.getJSON('getTodayTraffic?host=redmine.asiainfo.com', function(data) {
		var todayTrafficTemplate = $.templates("#todayTrafficTemplate");
		todayTrafficTemplate.link("#todayTrafficDiv", data);
	});
});