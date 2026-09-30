$(document).ready(function() {
	var police_saut = 2;
	
	if (!($.cookie("font-size")))
		$.cookie("font-size", $('html').css('font-size').length > 2 ? $('html').css('font-size').substr(0,2) : $('html').css('font-size'), { expires: 365, path: '/' });
	
	$('html').css('font-size', $.cookie("font-size") + 'px');
	
	$("a#augmenter").click(function(){
		var currentFontSize = $.cookie("font-size");
		var currentFontSizeNum = parseFloat(currentFontSize, 16);
		
		var newFontSize = currentFontSizeNum + police_saut;
		if (newFontSize <= 20) {
			$('html').css('font-size', newFontSize);
			$.cookie("font-size", newFontSize);
		}
		return false;
	});
	
	$("a#diminuer").click(function(){
		var currentFontSize = $.cookie("font-size");
		var currentFontSizeNum = parseFloat(currentFontSize, 16);
		
		var newFontSize = currentFontSizeNum - police_saut;
		if (newFontSize >= 16) {
			$('html').css('font-size', newFontSize);
			$.cookie("font-size", newFontSize);
		}
		return false;
	});
});