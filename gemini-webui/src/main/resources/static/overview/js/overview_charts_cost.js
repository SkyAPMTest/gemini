var costCheckData = {
	"title" : "趋势图（COST）",
	"check0" : true,
	"check1" : true,
	"check2" : false
}

function checkCostClick(check) {
	costCheckData[check.value] = check.checked;
	openCostTrendCharts();
}

$(document).ready(function() {
	var trendCostIndiTemplate = $.templates("#trendCostIndiTemplate");
	trendCostIndiTemplate.link("#trendCostIndiDiv", costCheckData);

	if (minusDay < 2) {
		$("#trendCostCompareDiv").show();
	} else {
		$("#trendCostCompareDiv").hide();
	}

	openCostTrendCharts();
});

function openCostTrendCharts() {
	// 基于准备好的dom，初始化echarts实例
	var trendCostChart = echarts.init(document.getElementById('trendChartsCostDiv'));

	var i = 1;
	var check1 = "";
	if (costCheckData["check1"] && minusDay < 2) {
		check1 = "1";
		i = i + 1;
	}
	var check2 = "";
	if (costCheckData["check2"] && minusDay < 2) {
		check2 = "7";
		i = i + 1;
	}

	if (i == 1) {
		trendCostChart.setOption(optionOne);
	} else if (i == 2) {
		trendCostChart.setOption(optionTwo);
	} else if (i == 3) {
		trendCostChart.setOption(optionThree);
	}

	// 异步加载数据
	trendCostChart.showLoading({
		text : "图表数据正在努力加载..."
	});

	$.getJSON('getTrendChart?host=blog.csdn.net&indicator=cost&minusDay=' + minusDay + "&check1=" + check1 + "&check2=" + check2).done(function(data) {
		trendCostChart.hideLoading();
		trendCostChart.setOption({
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