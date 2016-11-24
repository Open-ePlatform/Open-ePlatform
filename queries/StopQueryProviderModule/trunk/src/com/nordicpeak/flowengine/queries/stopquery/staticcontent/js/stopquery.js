$(document).ready(function() {
	
	hideQueryFunctions.push(hideQueryListener);
	
	$(".navigator-buttons .next").hide();
	
});

function hideQueryListener(queryID) {
	
	if($(".stopquery").length == 1) {
		
		$(".navigator-buttons .next").show();
	}
	
}
