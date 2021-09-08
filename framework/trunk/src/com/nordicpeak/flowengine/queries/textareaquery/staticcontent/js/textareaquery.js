$(function() {
	
	setQueryRequiredFunctions["TextAreaQueryInstance"] = makeTextAreaQueryRequired;
});

function initTextAreaQuery(queryID, keepalive, keepalivePollFrequency) {
	
	var query = $("#query_" + queryID);
	var textarea = query.find("textarea");
	
	if (query.hasClass("showlettercount")) {
		
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
	
	if (keepalive) {

		var lastKeepalive = Date.now();

		textarea.on("keyup change", function() {

			var now = Date.now();

			if (lastKeepalive < (now - (keepalivePollFrequency * 1000))) {

				$.ajax({
					url : query.data('keepalive-url'),
					cache : false,
					success : function(result) {

						lastKeepalive = now;
					}
				});
			}
		});
	}
	
}

function makeTextAreaQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
}