// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));

// 指定图表的配置项和数据
option = {
	title : {
		text : '趋势图（PV）',
		textStyle : {
			fontSize : 14,
			fontWeight : 'bold',
			fontFamily : 'Microsoft Yahei, Helvetica, Arial, sans-serif'
		},
		left : 15,
		top : 10
	},
	toolbox : {
		feature : {
			saveAsImage : {}
		},
		right : 15,
		top : 10
	},
	grid : {
		left : '3%',
		right : '3%',
		bottom : '10%',
		containLabel : true
	},
	color : [ '#0099FF', '#99CCFF', '#FCCE10', '#E87C25', '#27727B', '#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD', '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0' ],
	backgroundColor : '#ffffff',
	tooltip : {
		trigger : 'axis',
		backgroundColor : '#ffffff',
		borderColor : '#f3f3f4',
		borderWidth : 3,
		alwaysShowContent : false,
		padding : 0,
		textStyle : {
			color : '#262626',
			fontSize : 12,
			fontFamily : 'Microsoft Yahei, Helvetica, Arial, sans-serif'
		},
		formatter : '<div><div style="background: #f4f4f4;"><div style="display: inline; line-height: 35px; padding: 0 20px 0 20px">{b0}</div><div style="display: inline; padding: 0 20px 0 20px">浏览量(PV)</div></div><div><div><div style="display: inline; line-height: 35px; padding-left: 20px">{a0}</div><div style="display: inline; line-height: 35px; float: right; padding-right: 20px">{c0}</div></div><div><div style="display: inline; line-height: 35px; padding-left: 20px">{a1}</div><div style="display: inline; line-height: 35px; float: right; padding-right: 20px">{c1}</div></div></div></div>'
	},
	legend : {
		data : [],
		bottom : 10
	},
	xAxis : {
		type : 'category',
		boundaryGap : false,
		data : []
	},
	yAxis : {
		type : 'value'
	},
	series : [ {
		name : '今天',
		type : 'line',
		data : [],
		areaStyle : {
			normal : {}
		},
		label : {
			normal : {
				show : true,
				position : 'top'
			}
		}
	}, {
		name : '昨天',
		type : 'line',
		data : [],
		areaStyle : {
			normal : {}
		},
		label : {
			normal : {
				show : true,
				position : 'top'
			}
		}
	} ]
};

myChart.setOption(option);

$(document).ready(function() {
	$.getJSON('getTraffic?host=blog.csdn.net', function(data) {
		var trafficTemplate = $.templates("#trafficTemplate");
		trafficTemplate.link("#trafficDiv", data);
	});

	// 异步加载数据
	myChart.showLoading({
		text : "图表数据正在努力加载..."
	});
	$.getJSON('getTrendChart?host=blog.csdn.net&indicator=pv').done(function(data) {
		console.log(data)
		myChart.hideLoading();
		myChart.setOption({
			legend : {
				data : data.legend
			},
			xAxis : {
				data : data.today.categories
			},
			series : [ {
				name : data.today.legend,
				data : data.today.data
			}, {
				name : data.yesterday.legend,
				data : data.yesterday.data
			} ]
		});
	});
});