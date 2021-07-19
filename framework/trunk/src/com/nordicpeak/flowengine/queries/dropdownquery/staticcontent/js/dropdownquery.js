$(document).ready(function() {
	
	setQueryRequiredFunctions["DropDownQueryInstance"] = makeDropDownQueryRequired;
	
});

function initDropDownQuery(queryID) {
	
	var $query = $("#query_" + queryID);
	
	if($query.hasClass("hasFreeTextAlternative")) {
		
		var $select = $query.find("select");
		
		$select.change(function(e, data) {
			
			var $this = $(this);
			
			if($this.val() == "freeTextAlternative") {
				$("#" + $this.attr("name") + "Value").removeAttr("disabled").parent().show();
			} else {
				$("#" + $this.attr("name") + "Value").attr("disabled", "disabled").parent().hide();
			}
			
			if(data == undefined || !data.manual) {
			
				if($query.hasClass("enableAjaxPosting")) {
					
					runDropDownEvaluators($this, queryID);
					
				};
			
			}
			
		});
		
		$query.find("input[type='text']").keyup(delay(function() {
			
			runDropDownEvaluators($(this), queryID);
			
		}, 500));
		
		$select.trigger("change", [{manual: true}]);
		
	} else {
		
		if($query.hasClass("enableAjaxPosting")) {
			
			$query.find("select").change(function() {
				
				runDropDownEvaluators($(this), queryID);
				
			});
			
		}
		
	}
	
}

function runDropDownEvaluators($this, queryID) {
	
	var parameters = {};
	
	parameters[$this.attr("name")] = $this.val();
	
	if($this.val() == "freeTextAlternative"){
	
		parameters["q" + queryID + "_alternativeValue"] = $("#q" + queryID + "_alternativeValue").val();
		
	} else if($this.is("input:text")){

		parameters["q" + queryID + "_alternative"] = "freeTextAlternative";
	}
	
	runQueryEvaluators(queryID, parameters);
	
}

function makeDropDownQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}

function delay(callback, ms) {
	
	var timer = 0;
	
	return function() {
		
		var context = this;
		args = arguments;
		
		clearTimeout(timer);
		
		timer = setTimeout(function () {
		
			callback.apply(context, args);
		
    	}, ms || 0);
	};
}