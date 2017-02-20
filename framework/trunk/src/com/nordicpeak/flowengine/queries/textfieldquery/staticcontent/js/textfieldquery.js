$(document).ready(function() {
	
	setQueryRequiredFunctions["TextFieldQueryInstance"] = makeTextFieldQueryRequired;
	
	setQueryValidationErrorFunctions["TextFieldQueryInstance"] = setTextFieldValidationError;
	
});

function initTextFieldQuery(queryID) {
	
	var $query = $("#query_" + queryID);
	
	initQueryValidationErrors($query);
	
	if($query.hasClass("enableAjaxPosting")) {
		
		bindTextFieldChangeEvent($query.find("input[type='text']"), queryID);
		
	}
	
	if (!Modernizr.inputtypes.date) {
	
		$query.find("input[type='date']").datepicker({
	        changeMonth: true,
	        changeYear: true,
	        showWeek: true,
	        dayNamesShort: true,
	        onSelect: function() {
	            $(this).trigger("blur");
	        }
	    });
	}
}

function initQueryValidationErrors($query) {
	
	$query.find(".input-error input").tooltip({
		position : {
			my : "right top-38",
			at : "right+3 top",
			collision : "none"
		},
		track : false,
		content : function() {
			return $(this).next().attr("title") + "<span class=\"marker\"></span>";
		},
		show : {

			effect : 'none'
		},
		hide : {
			effect : 'none'
		}
	}).off("mouseover mouseout");
	
} 

function bindTextFieldChangeEvent($textFields, queryID) {
	
	$textFields.change(function() {
		
		runTextFieldEvaluators($(this), queryID);
		
	});
	
}

function runTextFieldEvaluators($this, queryID) {
	
	var parameters = {};
	
	var $inputWrapper = $this.parent().parent();
	
	$inputWrapper.find("input[type='text']").each(function () {
		
		var $textField = $(this);
		
		var $wrapper = $textField.parent();
		
		if($wrapper.hasClass("input-error")) {
			
			$textField.tooltip("destroy");
			$wrapper.removeClass("invalid input-error").find("i[title]").remove();
		}
		
		parameters[$textField.attr("name")] = $textField.val();
		
	});
	
	runQueryEvaluators(queryID, parameters);
	
}

function makeTextFieldQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}

function setTextFieldValidationError(queryID, validationErrors) {
	
	var $query = $("#query_" + queryID);
	
	$.each(validationErrors, function(i, error) {
		
		var $input = $query.find("input[name='q" + queryID + "_field" + error.field + "']");
		
		$input.parent().addClass("invalid input-error");
		
		var message;
		
		//TODO handle normal error types
		
		if(error.displayName) {
		
			message = error.displayName;
		
		} else if(error.messageKey) {
		
			message = error.messageKey;
	
		} else {
			
			message = "Unkown validation error";
			
		}
		
		$input.parent().append($("<i data-icon-after='!'' title='" + message + "' />"));
		
	});
	
	initQueryValidationErrors($query);
	
}
