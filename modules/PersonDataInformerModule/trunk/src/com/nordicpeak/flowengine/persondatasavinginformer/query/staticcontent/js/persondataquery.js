$(document).ready(function() {
	
	setQueryRequiredFunctions["PersonDataInformerQueryInstance"] = makePersonDataInformerQueryRequired;
});

function initPersonDataInformerQuery(queryID) {
	
	var $query = $("#query_" + queryID);
			
	if($query.hasClass("enableAjaxPosting")) {
		
		bindPersonDataInformerChangeEvent($query.find("input[type='checkbox']"), queryID);
	}
}

function bindPersonDataInformerChangeEvent($checkboxes, queryID) {
	
	$checkboxes.change(function() {
		
		runQueueEvaluators($(this), queryID);
	});
}

function runQueueEvaluators($this, queryID) {
	
	var parameters = {};
	
	var $inputWrapper = $this.parent().parent();
	
	$inputWrapper.find("input[type='checkbox']:checked").each(function () {
		
		var $checkbox = $(this);
		
		parameters[$checkbox.attr("name")] = $checkbox.val();
		
	});
	
	runQueryEvaluators(queryID, parameters);
}

function makePersonDataInformerQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
}

