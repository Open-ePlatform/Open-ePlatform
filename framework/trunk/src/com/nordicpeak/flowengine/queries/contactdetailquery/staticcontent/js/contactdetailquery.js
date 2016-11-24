$(document).ready(function() {
	
	setQueryRequiredFunctions["ContactDetailQueryInstance"] = makeContactDetailQueryRequired;
	
});

function initContactDetailQuery(queryID) {
	
	var shortQueryID = "#q" + queryID;
	
	
	$(shortQueryID + "_mobilephone").on("keyup blur change", function() {
		
		if($(this).val() != "") {
			$(shortQueryID + "_contactBySMS").removeAttr("disabled").next("label").removeClass("disabled");
		} else {
			$(shortQueryID + "_contactBySMS").removeAttr("checked").attr("disabled", "disabled").next("label").addClass("disabled");
		}
		
	});
	
	$(shortQueryID + "_email").on("keyup blur change", function() {
		
		if($(this).val() != "") {
			$(shortQueryID + "_contactByEmail").attr("checked", true);
		} else {
			$(shortQueryID + "_contactByEmail").removeAttr("checked");
		}
		
	});
	
	$(shortQueryID + "_mobilephone").trigger("change");
	$(shortQueryID + "_email").trigger("change");
	
	$("#query_" + queryID).find("input.input-error").parent().addClass("input-error");
	
}

function makeContactDetailQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}