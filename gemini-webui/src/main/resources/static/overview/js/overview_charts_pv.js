var pvCheckData = {
	"title" : "趋势图（PV）",
	"check0" : true,
	"check1" : true,
	"check2" : false
}

function checkPvClick(check) {
	pvCheckData[check.value] = check.checked;
	openPvTrendCharts();
}

$(document).ready(function() {
	var trendPvIndiTemplate = $.templates("#trendPvIndiTemplate");
	trendPvIndiTemplate.link("#trendPvIndiDiv", pvCheckData);

	if (minusDay < 2) {
		$("#trendPvCompareDiv").show();
	} else {
		$("#trendPvCompareDiv").hide();
	}

	openPvTrendCharts();
});

function openPvTrendCharts() {
	// 基于准备好的dom，初始化echarts实例
	var trendPvChart = echarts.init(document.getElementById('trendChartsPvDiv'));

	var i = 1;
	var check1 = "";
	if (pvCheckData["check1"] && minusDay < 2) {
		check1 = "1";
		i = i + 1;
	}
	var check2 = "";
	if (pvCheckData["check2"] && minusDay < 2) {
		check2 = "7";
		i = i + 1;
	}

	if (i == 1) {
		trendPvChart.setOption(optionOne);
	} else if (i == 2) {
		trendPvChart.setOption(optionTwo);
	} else if (i == 3) {
		trendPvChart.setOption(optionThree);
	}

	// 异步加载数据
	trendPvChart.showLoading({
		text : "图表数据正在努力加载..."
	});

	$.getJSON('getTrendChart?host=blog.csdn.net&indicator=pv&minusDay=' + minusDay + "&check1=" + check1 + "&check2=" + check2).done(function(data) {
		trendPvChart.hideLoading();
		trendPvChart.setOption({
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