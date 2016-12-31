$.ajaxSetup ({ 
    cache: false //关闭AJAX相应的缓存 
}); 

$(document).ready(function() {
	console.log("探寻这里的秘密；");
	console.log("体验这里的挑战；");
	console.log("成为这里的主人；");
	console.log("加入亚信数件，加入企业级搜索，你，可以开启不一样的程序猿人生。");
	console.log("请联系亚信数件彭勇升，工号：24809，邮箱：pengys5@asiainfo.com");

	$("#mainpanel_div").load("/overview/overview.html");
});

function openMenu(path) {
	$("#mainpanel_div").load(path);
}