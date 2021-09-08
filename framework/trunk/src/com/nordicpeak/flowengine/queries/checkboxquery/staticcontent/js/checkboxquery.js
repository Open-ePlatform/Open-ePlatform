$(function() {
	
	setQueryRequiredFunctions["CheckboxQueryInstance"] = makeCheckBoxQueryRequired;
});

function initCheckBoxQuery(queryID) {
	
	var $query = $("#query_" + queryID);
	
	var $freeTextAlternative = $query.find("input[type='checkbox'].freeTextAlternative")
	
	var $checkboxes = $query.find("input[type='checkbox'][name!='checkAllBoxes']");
	
	if($freeTextAlternative.length > 0) { 
	
		$freeTextAlternative.on("change", function(e, data) {
			
			var $this = $(this);
			
			if($this.is(":checked")) {
				$("#" + $this.attr("name") + "Value").removeAttr("disabled").parent().show();
			} else {
				$("#" + $this.attr("name") + "Value").attr("disabled", "disabled").parent().hide();
			}
			
		});
		
		$freeTextAlternative.trigger("change", [{manual: true}]);
		
		if($query.hasClass("enableAjaxPosting")) {
			
			bindCheckBoxChangeEvent($checkboxes, queryID);
			bindCheckBoxChangeEvent($query.find("input[type='text']"), queryID);
		}
		
	} else {
		
		if($query.hasClass("enableAjaxPosting")) {
			
			bindCheckBoxChangeEvent($checkboxes, queryID);
		}
		
	}
	
	var maxChecked = $query.data("maxchecked");
	
	if (maxChecked != undefined) {
		
		runCheckBoxMaxAlternatives($checkboxes, maxChecked);
		
		$checkboxes.on("change", function() {
			runCheckBoxMaxAlternatives($checkboxes, maxChecked);
		});
	}

	$("#query_" + queryID + "checkAllBoxes").on("click", function(){
    	
    	var $checkAll = $(this);
    	
   		$checkboxes.each(function() {
    	
    		$(this).prop('checked', $checkAll.is(':checked'));
    	});
    	
    	$checkboxes.last().trigger("change");
	});	

	if($freeTextAlternative.length > 0) {
	
		$freeTextAlternative.trigger("change");
	}
}

function runCheckBoxMaxAlternatives($checkboxes, maxChecked) {
	
	var checked = 0;
	
	$checkboxes.each(function(index) {
		
		if ($(this).prop("checked")) {
			checked += 1;
		}
	});
	
	if (checked >= maxChecked) {
		
		$checkboxes.each(function(index) {
			
			var checkbox = $(this); 
			
			if (!checkbox.prop("checked")) {
				checkbox.prop("disabled", true);
				checkbox.siblings("label").addClass("disabled");
			}
		});
		
	} else {
		
		$checkboxes.prop("disabled", false);
		$checkboxes.siblings("label").removeClass("disabled");
	}
}

function bindCheckBoxChangeEvent($checkboxes, queryID) {
	
	$checkboxes.on("change", function() {
	   
		var $checkAll = $("#query_" + queryID + "checkAllBoxes");
		var $currentCheckbox = $(this);
		
		if ($currentCheckbox.is(":checked")) {
			
			if ($("#query_" + queryID + " input[type = 'checkbox'][name != 'checkAllBoxes']:not(':checked')").length === 0) {
			
				$checkAll.prop("checked", true);
			}
		}
		else {
			
			$checkAll.prop("checked", false);
		}
		
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