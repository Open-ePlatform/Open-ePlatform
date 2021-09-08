$(function() {
	
	setQueryRequiredFunctions["ManualMultiSignQueryInstance"] = makeManualMultiSignQueryRequired;
});

function initManualMultiSignQuery(queryID) {
	
	var $query = $("#query_" + queryID);

	$query.find("input.input-error").parent().addClass("input-error");
	
	if ($query.hasClass("enableAjaxPosting")) {
		
		$query.find("input[name$='socialSecurityNumber']").on("change", function() {
		
			runManualMultiSignEvaluators($(this), queryID);
		
		});
	}
}

function makeManualMultiSignQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}

function runManualMultiSignEvaluators($this, queryID) {
	
	var parameters = {};
	
	var $wrapper = $this.parent();
	
	if ($wrapper.hasClass("input-error")) {
		
		$wrapper.removeClass("invalid input-error").find("i[title]").remove();
	}
		
	parameters[$this.attr("name")] = $this.val();
	
	runQueryEvaluators(queryID, parameters);
}