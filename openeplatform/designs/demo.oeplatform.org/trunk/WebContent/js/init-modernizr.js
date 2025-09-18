$(document).ready(function() {
	
	Modernizr.load();
	
	if (Modernizr.firefox) {
        $('html').addClass('moz');
    }
	
	if(Modernizr.touch) {
		$("#search").addClass("ipad");		
	}
	
	if($.browser.msie || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
		$("html").addClass("ie");
	}
	
	$(".contentitem:not(:has(section))").each(function() {
		$(this).addClass("no-sections");
	});
	
});