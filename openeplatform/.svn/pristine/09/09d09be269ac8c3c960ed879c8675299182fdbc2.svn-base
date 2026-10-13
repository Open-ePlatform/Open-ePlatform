$(function() {
	
	hideQueryFunctions.push(hideStopQueryListener);
});

function initStopQuery(queryID) {

	$(".navigator-buttons .next").hide();
}

function hideStopQueryListener($query) {
	
	var queryID = $query.attr('id');
	
	var otherStoppingQueriesSelector = ".stopflow:not(" + "#" + queryID + ")";
	
	if ($(otherStoppingQueriesSelector).length == 0) {
		
		$(".navigator-buttons .next").show();
	}
}
