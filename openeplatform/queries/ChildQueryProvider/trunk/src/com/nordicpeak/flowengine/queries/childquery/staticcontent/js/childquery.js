$(function() {

	setQueryRequiredFunctions["ChildQueryInstance"] = makeChildQueryRequired;
});

function initChildQuery(queryID) {

	var $query = $("#query_" + queryID);

	$query.find("input[type='radio']").on("change", function() {
		showHideGuardians($query);
	});

	showHideGuardians($query);
	
	$(".alternative").on("click", function(event) {
		
		if(event.target === this){
			$(this).find("input").trigger("click");
		}
	});
	
	if($query.hasClass("enableAjaxPosting")) {
		
		$query.find("input[type='radio']").on("change", function() {
			
			runChildQueryEvaluators($query, queryID);
		});
		
			
		$query.find("input[type='text']").on("blur", function(e) {
			
			if(!$(e.relatedTarget).hasClass("force-submit")){
				
				runChildQueryEvaluators($query, queryID);
			}
	
		});
	}
	
	$query.find('input.disablepaste').on('paste', function (e) {
		e.preventDefault();
	});
}

function runChildQueryEvaluators($query, queryID) {
	
	var parameters = {};
	
	$query.find("input[type='radio']:checked").each(function() {
		
		var $radio = $(this);
		
		parameters[$radio.attr("name")] = $radio.val();
		
	});
	
	$query.find("input[type='text']").each(function () {
		
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

function showHideGuardians(query) {

	var guardians = query.find(".guardian");
	var otherguardians = query.find(".otherguardians");

	otherguardians.hide();
	guardians.each(function() {
		$(this).hide().find("input").prop("disabled", true);
	});

	var selectedRadio = query.find("input[type='radio']:checked");

	if (selectedRadio.length > 0) {

		var relatedGuardians = selectedRadio.closest(".alternative").find(".guardians div");
		
		if (relatedGuardians.length > 0) {
			
			otherguardians.show();
			
			relatedGuardians.each(function() {

				var citizenID = $(this).text();

				guardians.filter(".guardian[data-citizenid='" + citizenID + "']").show().find("input").prop("disabled", false);
			});
		}
	}
}

function makeChildQueryRequired(queryID) {

	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
}