$(document).ready(function() {
	
	setQueryRequiredFunctions["CheckboxQueryInstance"] = makeCheckBoxQueryRequired;
	
});

function initCheckBoxQuery(queryID) {
	
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
			
//			if(data == undefined || !data.manual) {
//			
//				if($query.hasClass("enableAjaxPosting")) {
//					
//					runCheckBoxEvaluators($this, queryID);
//					
//				}
//			
//			}
			
		});
		
		$freeTextAlternative.trigger("change", [{manual: true}]);
		
		if($query.hasClass("enableAjaxPosting")) {
			
			bindCheckBoxChangeEvent($query.find("input[type='checkbox']"), queryID);
			
		}
		
	} else {
		
		if($query.hasClass("enableAjaxPosting")) {
			
			bindCheckBoxChangeEvent($query.find("input[type='checkbox']"), queryID);
			
		}
		
	}
	
	if($query.data("maxchecked") != "") {
		
		bindCheckBoxMaxAlternatives($query.find("input[type='checkbox']"), $query.data("maxchecked"));
	}
	
}

function bindCheckBoxMaxAlternatives($checkboxes, maxChecked) {
	
	$checkboxes.change(function() {
		
		var checked = 0;
		
		$checkboxes.each(function(index){
			
			if($(this).prop("checked")){
				checked += 1;
			}
		});
		
		if(checked >= maxChecked){
			
			$checkboxes.each(function(index){
				
				var checkbox = $(this); 
				
				if(!checkbox.prop("checked")){
					checkbox.prop("disabled", true);
					checkbox.siblings("label").addClass("disabled");
				}
			});
			
		} else {
			
			$checkboxes.prop("disabled", false);
			$checkboxes.siblings("label").removeClass("disabled");
		}
	});
}

function bindCheckBoxChangeEvent($checkboxes, queryID) {
	
	$checkboxes.change(function() {
		
		runCheckBoxEvaluators($(this), queryID);
		
	});
	
}

function runCheckBoxEvaluators($this, queryID) {
	
	var parameters = {};
	
	var $inputWrapper = $this.parent().parent();
	
	$inputWrapper.find(".alternative input[type='checkbox']:checked").each(function () {
		
		var $checkbox = $(this);
		
		parameters[$checkbox.attr("name")] = $checkbox.val();
		
		if($checkbox.hasClass("freeTextAlternative")){
			
			parameters["q" + queryID + "_freeTextAlternativeValue"] = $("#q" + queryID + "_freeTextAlternativeValue").val();
		}
		
	});
	
	runQueryEvaluators(queryID, parameters);
	
}

function makeCheckBoxQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}