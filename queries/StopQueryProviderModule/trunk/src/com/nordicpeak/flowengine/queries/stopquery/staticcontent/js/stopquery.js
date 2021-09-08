$(function() {
	
	hideQueryFunctions.push(hideStopQueryListener);
});

function initStopQuery(queryID) {

	$(".navigator-buttons .next").hide();
}

function hideStopQueryListener($query) {
	
	if ($query.find(".stopquery").length > 0 && $(".stopquery").length == 1) {
		
		$(".navigator-buttons .next").show();
		
	}
	
}
