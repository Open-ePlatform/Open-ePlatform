$(function() {
	
	setQueryRequiredFunctions["DropDownQueryInstance"] = makeDropDownQueryRequired;
	
});

function initDropDownQuery(queryID) {
	
	var $query = $("#query_" + queryID);

	if($query.hasClass("hasFreeTextAlternative")) {
		
		var $select = $query.find("select");
		
		$select.on("change", function(e, data) {
			
			var $this = $(this);
			
			if($this.val() == "freeTextAlternative") {
				$("#q" + queryID + "_dropdownalternativeValue").removeAttr("disabled").parent().show();
			} else {
				$("#q" + queryID + "_dropdownalternativeValue").attr("disabled", "disabled").parent().hide();
			}
			
			if(data == undefined || !data.manual) {
			
				if($query.hasClass("enableAjaxPosting")) {
					
					runDropDownEvaluators($this, queryID);
					
				};
			
			}
			
		});
		
		$query.find("input[type='text']").change(function() {
			
			runDropDownEvaluators($(this), queryID);
			
		});
		
		$select.trigger("change", [{manual: true}]);
		
	} else {
		
		if($query.hasClass("enableAjaxPosting")) {
			
			$query.find("select").on("change", function() {
				
				runDropDownEvaluators($(this), queryID);
				
			});
			
		}
		
	}
	
}

function runDropDownEvaluators($this, queryID) {
	
	var parameters = {};
	
	parameters[$this.attr("name")] = $this.val();
	
	if($this.val() == "freeTextAlternative"){
	
		parameters["q" + queryID + "_dropdownalternativeValue"] = $("#q" + queryID + "_dropdownalternativeValue").val();
		
	} else if($this.is("input:text")){

		parameters["q" + queryID + "_dropdownalternative"] = "freeTextAlternative";
	}
	
	runQueryEvaluators(queryID, parameters);
	
}

function makeDropDownQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}