$(document).ready(function() {
	
	setQueryRequiredFunctions["RadioButtonQueryInstance"] = makeRadioButtonQueryRequired;
	
});

function initRadioButtonQuery(queryID) {
	
	var $query = $("#query_" + queryID);
	
	if($query.hasClass("hasFreeTextAlternative")) {
		
		var $inputs = $query.find("input[type='radio']");
		
		$inputs.change(function(e, data) {
			
			var $this = $(this);
			
			if($this.hasClass("freeTextAlternative") && $this.is(":checked")) {
				$("#" + $this.attr("name") + "Value").removeAttr("disabled").parent().show();
			} else {
				$("#" + $this.attr("name") + "Value").attr("disabled", "disabled").parent().hide();
			}
			
			if(data == undefined || !data.manual) {
				
				if($query.hasClass("enableAjaxPosting")) {
					
					runRadioButtonEvaluators($this, queryID);
					
				};			
			}
			
		});
		
		$query.find("input[type='text']").change(function() {
			
			runRadioButtonEvaluators($(this), queryID);
			
		});
		
		$inputs.trigger("change", [{manual: true}]);
		
	} else {
		
		if($query.hasClass("enableAjaxPosting")) {
			
			$query.find("input[type='radio']").change(function() {
				
				runRadioButtonEvaluators($(this), queryID);
				
			});
		
		}
		
	}
	
}

function runRadioButtonEvaluators($this, queryID) {
	
	var parameters = {};
	
	var $inputWrapper = $this.parent().parent();
	
	$inputWrapper.find(".alternative input[type='radio']:checked").each(function () {
		
		var $radiobutton = $(this);
		
		parameters[$radiobutton.attr("name")] = $radiobutton.val();
		
		if($radiobutton.hasClass("freeTextAlternative")){
			
			parameters["q" + queryID + "_alternativeValue"] = $("#q" + queryID + "_alternativeValue").val();
		}
		
	});
	
	runQueryEvaluators(queryID, parameters);
	
}

function makeRadioButtonQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}