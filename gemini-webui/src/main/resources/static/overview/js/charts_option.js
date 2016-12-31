// 指定图表的配置项和数据
optionOne = {
	grid : {
		top : '3%',
		left : '3%',
		right : '3%',
		bottom : '10%',
		containLabel : true
	},
	color : [ '#3d99d3', '#31b9ff', '#62b6eb', '#6ec6ff', '#b7e3ff', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD', '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0' ],
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
		formatter : '<div><div style="background: #f4f4f4;"><div style="display: inline; line-height: 35px; padding: 0 20px 0 20px">{b0}</div><div style="display: inline; padding: 0 20px 0 20px">数值</div></div><div><div><div style="display: inline; line-height: 35px; padding-left: 20px">{a0}</div><div style="display: inline; line-height: 35px; float: right; padding-right: 20px">{c0}</div></div></div></div>'
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

optionTwo = {
	grid : {
		top : '3%',
		left : '3%',
		right : '3%',
		bottom : '10%',
		containLabel : true
	},
	color : [ '#3d99d3', '#31b9ff', '#62b6eb', '#6ec6ff', '#b7e3ff', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD', '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0' ],
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
		formatter : '<div><div style="background: #f4f4f4;"><div style="display: inline; line-height: 35px; padding: 0 20px 0 20px">{b0}</div><div style="display: inline; padding: 0 20px 0 20px">数值</div></div><div><div><div style="display: inline; line-height: 35px; padding-left: 20px">{a0}</div><div style="display: inline; line-height: 35px; float: right; padding-right: 20px">{c0}</div></div><div><div style="display: inline; line-height: 35px; padding-left: 20px">{a1}</div><div style="display: inline; line-height: 35px; float: right; padding-right: 20px">{c1}</div></div></div></div>'
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

optionThree = {
	grid : {
		top : '3%',
		left : '3%',
		right : '3%',
		bottom : '10%',
		containLabel : true
	},
	color : [ '#3d99d3', '#31b9ff', '#62b6eb', '#6ec6ff', '#b7e3ff', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD', '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0' ],
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
		formatter : '<div><div style="background: #f4f4f4;"><div style="display: inline; line-height: 35px; padding: 0 20px 0 20px">{b0}</div><div style="display: inline; padding: 0 20px 0 20px">数值</div></div><div><div><div style="display: inline; line-height: 35px; padding-left: 20px">{a0}</div><div style="display: inline; line-height: 35px; float: right; padding-right: 20px">{c0}</div></div><div><div style="display: inline; line-height: 35px; padding-left: 20px">{a1}</div><div style="display: inline; line-height: 35px; float: right; padding-right: 20px">{c1}</div></div><div><div style="display: inline; line-height: 35px; padding-left: 20px">{a2}</div><div style="display: inline; line-height: 35px; float: right; padding-right: 20px">{c2}</div></div></div></div>'
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