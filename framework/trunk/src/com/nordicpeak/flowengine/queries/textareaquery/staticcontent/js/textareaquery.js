$(document).ready(function() {
	
	setQueryRequiredFunctions["TextAreaQueryInstance"] = makeTextAreaQueryRequired;
});

function initTextAreaQuery(queryID) {
	
	var query = $("#query_" + queryID);
	
	if (query.hasClass("showlettercount")) {
		
		var textarea = query.find("textarea");
		var counter = query.find(".lettercounter");
		
		var updateCounter = function() {
			var length = textarea.val().length;
			var max = counter.data("maxlength")
			counter.text(length + "/" + max);
			
			if (length > max) {
				counter.css('color', 'red');
			} else {
				counter.css('color', '');
			}
		};
		
		updateCounter();
		textarea.on("keyup change", updateCounter);
	}
}

function makeTextAreaQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
}