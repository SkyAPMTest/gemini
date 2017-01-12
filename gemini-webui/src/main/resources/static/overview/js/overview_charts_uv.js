var uvCheckData = {
	"title" : "趋势图（UV）",
	"check0" : true,
	"check1" : true,
	"check2" : false
}

function checkUvClick(check) {
	uvCheckData[check.value] = check.checked;
	openUvTrendCharts();
}

$(document).ready(function() {
	var trendUvIndiTemplate = $.templates("#trendUvIndiTemplate");
	trendUvIndiTemplate.link("#trendUvIndiDiv", uvCheckData);

	if (minusDay < 2) {
		$("#trendUvCompareDiv").show();
	} else {
		$("#trendUvCompareDiv").hide();
	}

	openUvTrendCharts();
});

function openUvTrendCharts() {
	// 基于准备好的dom，初始化echarts实例
	var trendUvChart = echarts.init(document.getElementById('trendChartsUvDiv'));

	var i = 1;
	var check1 = "";
	if (uvCheckData["check1"] && minusDay < 2) {
		check1 = "1";
		i = i + 1;
	}
	var check2 = "";
	if (uvCheckData["check2"] && minusDay < 2) {
		check2 = "7";
		i = i + 1;
	}

	if (i == 1) {
		trendUvChart.setOption(optionOne);
	} else if (i == 2) {
		trendUvChart.setOption(optionTwo);
	} else if (i == 3) {
		trendUvChart.setOption(optionThree);
	}

	// 异步加载数据
	trendUvChart.showLoading({
		text : "图表数据正在努力加载..."
	});

	$.getJSON('getTrendChart?host=work.asiainfo.com&indicator=uv&minusDay=' + minusDay + "&check1=" + check1 + "&check2=" + check2).done(function(data) {
		trendUvChart.hideLoading();
		trendUvChart.setOption({
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