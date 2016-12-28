var ipCheckData = {
	"title" : "趋势图（IP）",
	"check0" : true,
	"check1" : true,
	"check2" : false
}

function checkIpClick(check) {
	ipCheckData[check.value] = check.checked;
	openIpTrendCharts();
}

$(document).ready(function() {
	var trendIpIndiTemplate = $.templates("#trendIpIndiTemplate");
	trendIpIndiTemplate.link("#trendIpIndiDiv", ipCheckData);

	if (minusDay < 2) {
		$("#trendIpCompareDiv").show();
	} else {
		$("#trendIpCompareDiv").hide();
	}

	openIpTrendCharts();
});

function openIpTrendCharts() {
	// 基于准备好的dom，初始化echarts实例
	var trendIpChart = echarts.init(document.getElementById('trendChartsIpDiv'));

	var i = 1;
	var check1 = "";
	if (ipCheckData["check1"] && minusDay < 2) {
		check1 = "1";
		i = i + 1;
	}
	var check2 = "";
	if (ipCheckData["check2"] && minusDay < 2) {
		check2 = "7";
		i = i + 1;
	}

	if (i == 1) {
		trendIpChart.setOption(optionOne);
	} else if (i == 2) {
		trendIpChart.setOption(optionTwo);
	} else if (i == 3) {
		trendIpChart.setOption(optionThree);
	}

	// 异步加载数据
	trendIpChart.showLoading({
		text : "图表数据正在努力加载..."
	});

	$.getJSON('getTrendChart?host=blog.csdn.net&indicator=ip&minusDay=' + minusDay + "&check1=" + check1 + "&check2=" + check2).done(function(data) {
		trendIpChart.hideLoading();
		trendIpChart.setOption({
			legend : {
				data : data[0].legend
			},
			xAxis : {
				data : data[0].categories
			},
			series : data
		});
	});
}