var TextFieldQueryi18n = {
	colorChoose: "choose",
	colorCancel: "cancel",
}

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
	
	$query.find("input[type='text'].color").each(function(){
		
		var input = $(this);
		var hasErrors = input.hasClass("input-error");
		
		var colorFormat = "rgb";
		
		if (input.hasClass("color-hex")) {
			colorFormat = "hex8";
			
		} else if (input.hasClass("color-rgb")) {
			colorFormat = "rgb";
			
		} else if (input.hasClass("color-hsv")) {
			colorFormat = "hsv";
			
		} else if (input.hasClass("color-hsl")) {
			colorFormat = "hsl";
		}
		
		input.spectrum({
			textInput: input,
			flat: false,
			showSelectionPalette: false,
			chooseText: TextFieldQueryi18n.colorChoose,
		    cancelText: TextFieldQueryi18n.colorCancel,
			allowEmpty: input.parent().find("label").hasClass("required"),
			showAlpha: true,
			showPalette: true,
			showInitial: true,
			preferredFormat: colorFormat,
			palette: [
			          ["rgb(0, 0, 0)", "rgb(67, 67, 67)", "rgb(102, 102, 102)", "rgb(153, 153, 153)","rgb(183, 183, 183)",
			          "rgb(204, 204, 204)", "rgb(217, 217, 217)", "rgb(239, 239, 239)", "rgb(243, 243, 243)", "rgb(255, 255, 255)"],
			          ["rgb(152, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 153, 0)", "rgb(255, 255, 0)", "rgb(0, 255, 0)",
			          "rgb(0, 255, 255)", "rgb(74, 134, 232)", "rgb(0, 0, 255)", "rgb(153, 0, 255)", "rgb(255, 0, 255)"],
			          ["rgb(230, 184, 175)", "rgb(244, 204, 204)", "rgb(252, 229, 205)", "rgb(255, 242, 204)", "rgb(217, 234, 211)",
			          "rgb(208, 224, 227)", "rgb(201, 218, 248)", "rgb(207, 226, 243)", "rgb(217, 210, 233)", "rgb(234, 209, 220)",
			          "rgb(221, 126, 107)", "rgb(234, 153, 153)", "rgb(249, 203, 156)", "rgb(255, 229, 153)", "rgb(182, 215, 168)",
			          "rgb(162, 196, 201)", "rgb(164, 194, 244)", "rgb(159, 197, 232)", "rgb(180, 167, 214)", "rgb(213, 166, 189)",
			          "rgb(204, 65, 37)", "rgb(224, 102, 102)", "rgb(246, 178, 107)", "rgb(255, 217, 102)", "rgb(147, 196, 125)",
			          "rgb(118, 165, 175)", "rgb(109, 158, 235)", "rgb(111, 168, 220)", "rgb(142, 124, 195)", "rgb(194, 123, 160)",
			          "rgb(166, 28, 0)", "rgb(204, 0, 0)", "rgb(230, 145, 56)", "rgb(241, 194, 50)", "rgb(106, 168, 79)",
			          "rgb(69, 129, 142)", "rgb(60, 120, 216)", "rgb(61, 133, 198)", "rgb(103, 78, 167)", "rgb(166, 77, 121)",
			          "rgb(133, 32, 12)", "rgb(153, 0, 0)", "rgb(180, 95, 6)", "rgb(191, 144, 0)", "rgb(56, 118, 29)",
			          "rgb(19, 79, 92)", "rgb(17, 85, 204)", "rgb(11, 83, 148)", "rgb(53, 28, 117)", "rgb(116, 27, 71)",
			          "rgb(91, 15, 0)", "rgb(102, 0, 0)", "rgb(120, 63, 4)", "rgb(127, 96, 0)", "rgb(39, 78, 19)",
			          "rgb(12, 52, 61)", "rgb(28, 69, 135)", "rgb(7, 55, 99)", "rgb(32, 18, 77)", "rgb(76, 17, 48)"]
			      ]
		});
		
		input.keypress(function(event){
			if (event.which == 13) { // Enter
				event.preventDefault();
			}
		});
		
		input.show();
		
		if (hasErrors) {
			input.addClass("input-error");
		}
    });
}

function initQueryValidationErrors($query) {

// TODO re-enable when placement is correct
//	$query.find(".input-error input").tooltip({
//		position : {
//			my : "right top-38",
//			at : "right+3 top",
//			collision : "none"
//		},
//		track : false,
//		content : function() {
//			return $(this).next().attr("title") + "<span class=\"marker\"></span>";
//		},
//		show : {
//
//			effect : 'none'
//		},
//		hide : {
//			effect : 'none'
//		}
//	}).off("mouseover mouseout");
	
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
			
//			$textField.tooltip("destroy");
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
		
		var message = getValidationErrorMessage(error);
		
		$input.parent().append($("<i data-icon-after='!'' title='" + message + "' />"));
		
	});
	
	initQueryValidationErrors($query);
	
}
