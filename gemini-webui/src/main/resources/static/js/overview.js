$(document).ready(function() {
	$.getJSON('getTraffic?host=upload.jianshu.io', function(data) {
		var trafficTemplate = $.templates("#trafficTemplate");
		trafficTemplate.link("#trafficDiv", data);
	});
});