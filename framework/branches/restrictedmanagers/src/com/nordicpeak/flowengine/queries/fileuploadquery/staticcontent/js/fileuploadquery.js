$(document).ready(function() {
	
	setQueryRequiredFunctions["FileUploadQueryInstance"] = makeFileUploatQueryRequired;
});

function makeFileUploatQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
}

function initFileUploadQuery(queryID) {
	
	initFileUploader($("#query_" + queryID + "_fileuploader"));
}