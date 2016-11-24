$(document).ready(function() {
	
	setQueryRequiredFunctions["CheckboxPaymentQueryInstance"] = makeCheckBoxPaymentQueryRequired;
	
});

function initCheckBoxPaymentQuery(queryID) {
	
	var $query = $("#query_" + queryID);
	
	var $freeTextAlternative = $query.find("input[type='checkbox'].freeTextAlternative")
	
	if($freeTextAlternative.length > 0) { 
	
		$freeTextAlternative.change(function(e, data) {
			
			var $this = $(this);
			
			if($this.is(":checked")) {
				$("#" + $this.attr("name") + "Value").removeAttr("disabled").parent().show();
			} else {
				$("#" + $this.attr("name") + "Value").attr("disabled", "disabled").parent().hide();
			}
			
			if(data == undefined || !data.manual) {
			
				if($query.hasClass("enableAjaxPosting")) {
					
					runCheckBoxPaymentEvaluators($this, queryID);
					
				}
			
			}
			
		});
		
		$freeTextAlternative.trigger("change", [{manual: true}]);
		
		if($query.hasClass("enableAjaxPosting")) {
			
			bindCheckBoxChangeEvent($query.find("input[type='checkbox']:not(.freeTextAlternative)"), queryID);
			
		}
		
	} else {
		
		if($query.hasClass("enableAjaxPosting")) {
			
			bindCheckBoxPaymentChangeEvent($query.find("input[type='checkbox']"), queryID);
			
		}
		
	}
	
}

function bindCheckBoxPaymentChangeEvent($checkboxes, queryID) {
	
	$checkboxes.change(function() {
		
		runCheckBoxPaymentEvaluators($(this), queryID);
		
	});
	
}

function runCheckBoxPaymentEvaluators($this, queryID) {
	
	var parameters = {};
	
	var $inputWrapper = $this.parent().parent();
	
	$inputWrapper.find(".alternative input[type='checkbox']:checked").each(function () {
		
		var $checkbox = $(this);
		
		parameters[$checkbox.attr("name")] = $checkbox.val();
		
	});
	
	runQueryEvaluators(queryID, parameters);
	
}

function makeCheckBoxPaymentQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}