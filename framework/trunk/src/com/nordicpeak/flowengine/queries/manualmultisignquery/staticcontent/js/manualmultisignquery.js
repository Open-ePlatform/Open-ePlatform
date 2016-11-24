$(document).ready(function() {
	
	setQueryRequiredFunctions["ManualMultiSignQueryInstance"] = makeManualMultiSignQueryRequired;
	
});

function initManualMultiSignQuery(queryID) {
	
	$("#query_" + queryID).find("input.input-error").parent().addClass("input-error");
	
}

function makeManualMultiSignQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}