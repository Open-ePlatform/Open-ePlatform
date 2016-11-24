$(document).ready(function() {
	
	setQueryRequiredFunctions["TextAreaQueryInstance"] = makeTextAreaQueryRequired;
});

function makeTextAreaQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}