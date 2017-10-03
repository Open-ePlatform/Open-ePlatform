$(document).ready(function() {
	
	setQueryRequiredFunctions["ContactDetailQueryInstance"] = makeContactDetailQueryRequired;
	
});

function initContactDetailQuery(queryID) {
	
	var shortQueryID = "#q" + queryID;
	
	
	$(shortQueryID + "_mobilephone").on("keyup blur change", function() {
		
		var smsCheckbox = $(shortQueryID + "_contactBySMS");
		
		if ($(this).val() != "") {
			
			if (smsCheckbox.hasClass('forced')) {
				smsCheckbox.prop("checked", true);
				
			} else {
				smsCheckbox.removeAttr("disabled").next("label").removeClass("disabled");
			}
			
		} else {
			smsCheckbox.prop("checked", false).attr("disabled", "disabled").next("label").addClass("disabled");
		}
		
	});
	
	$(shortQueryID + "_email").on("keyup blur change", function() {
		
		var emailCheckbox = $(shortQueryID + "_contactByEmail");
		
		if ($(this).val() != "") {
			emailCheckbox.prop("checked", true);
			
		} else {
			emailCheckbox.prop("checked", false);
		}
		
	});
	
	$(shortQueryID + "_mobilephone").trigger("change");
	$(shortQueryID + "_email").trigger("change");
	
	$("#query_" + queryID).find("input.input-error").parent().addClass("input-error");
	
}

function makeContactDetailQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}