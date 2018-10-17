var pudQueryLanguage = {
	"CANT_CONTACT_SEARCHSERVICE" : "Unable to contact the search service, contact administrator",
	"SERVICE_ERROR_MESSAGE" : "There is currently a problem with the search service. Contact the administrator.",
	"PUD_NOT_FOUND" : "Propert unit designation not found for selected address",
	"TO_MANY_PUD_FOUND" : "To many property unit designations found for address",
	"UNKOWN_ERROR_MESSAGE" : "An unexpected error occured. Contact the administrator."
};

$(document).ready(function() {
	
	setQueryRequiredFunctions["PUDQueryInstance"] = makePUDQueryRequired;
	
});

function initPUDQuery(queryID) {

	var init = true;
	var query = $("#query_" + queryID);
	var $select = query.find("select");
	var $input = $("#q" + queryID + "_searchInput");
	var pudField = $("#q" + queryID + "_propertyUnitDesignation");
	var addressField = $("#q" + queryID + "_address");
	var useAddressAsResult = $select.closest(".search-select").data("useaddressasresult");
	
	$select.change(function(e) {

		var $this = $(this);
		
		var $selectedOption = $this.find("option:selected");
		
		$input.attr("placeholder", $selectedOption.attr("label"));
		
		if (!init) {
			$input.val("");
		}
		
		var type = $selectedOption.val();
		
		if (type == "PUD") {
			
			$input.autocomplete({
				source : function(request, response) {
					return searchPUD(request, response, queryID, $input, $selectedOption);
				},
				minLength : 3,
				select: function( event, ui ) {
					
					addressField.val("");
					
					pudField.val(ui.item.label);
					pudField.parent().find("span").text(ui.item.label);
					pudField.parent().show();
					pudField.change();
				}
			});
			
		} else if (type == "ADDRESS") {
			
			$input.autocomplete({
				source : function(request, response) {
					return searchAddress(request, response, queryID, $input, $selectedOption);
				},
				minLength : 3,
				select: function( event, ui ) {

					addressField.val(ui.item.label);
					
					if (useAddressAsResult) {
						
						pudField.parent().find("span").text(ui.item.label);
						pudField.parent().show();
						pudField.change();
						
					} else {
						
						searchPUDFromFnr(ui.item.fnr, queryID, $input, $select, pudField, $selectedOption);
					}
				}
			});
		}
	});

	pudField.parent().find("i").click(function(e) {
		pudField.val("");
		addressField.val("");
		pudField.parent().find("span").text("");
		pudField.parent().hide();
		pudField.change();
	});
	
	$select.trigger("change");
	
	init = false;
	
	if (query.hasClass("enableAjaxPosting")) {
		
		pudField.change(function() {
			
			var parameters = {};
			parameters[pudField.attr("name")] = pudField.val();
			parameters[addressField.attr("name")] = addressField.val();
			
			runQueryEvaluators(queryID, parameters);
		});
	}
}

function searchPUD(request, response, queryID, $input, $selectedOption) {
	
	$.ajax({
		url : $selectedOption.closest(".search-select").find("div[data-search-service='" + $selectedOption.val() + "']").data("url"),
		dataType : "json",
		data : {
			q : request.term
		},
		success : function(data) {
			
			removePUDQueryError(queryID);
			
			if (data.features != undefined && data.features.length > 0) {
				data = data.features;
			}
			
			if (data.length > 0) {
				
				response($.map(data, function(item) {
					var pud = item.properties.name.replace("Enhetsomr�de 1", "");
					return {
						label : pud,
						value : pud,
						objid : item.properties.objid
					}
				}));
				
			}
					
			$input.removeClass("ui-autocomplete-loading");
		},
		error : function() {
			
			$input.removeClass("ui-autocomplete-loading");
			removePUDQueryError(queryID);
			showPUDQueryErrorMessage(queryID, pudQueryLanguage.UNKOWN_ERROR_MESSAGE);
		}
	});
}

function searchAddress(request, response, queryID, $input, $selectedOption) {
	
	$.ajax({
		url : $selectedOption.closest(".search-select").find("div[data-search-service='" + $selectedOption.val() + "']").data("url"),
		dataType : "json",
		data : {
			q : request.term
		},
		success : function(data) {
			
			removePUDQueryError(queryID);
			
			if (data.length > 0) {
				response($.map(data, function(item) {
					return {
						label : item[1],
						value : item[1],
						fnr : item[4]
					}
				}));							
			}
			
			$input.removeClass("ui-autocomplete-loading");
		},
		error : function() {
			
			$input.removeClass("ui-autocomplete-loading");
			removePUDQueryError(queryID);
			showPUDQueryErrorMessage(queryID, pudQueryLanguage.UNKOWN_ERROR_MESSAGE);
		}
	});
}

function searchPUDFromFnr(fnumber, queryID, $input, $select, pudField, $selectedOption) {
	
	$input.addClass("ui-autocomplete-loading");
	
	$.ajax({
		url : $selectedOption.closest(".search-select").find("div[data-search-service='PUD']").data("url"),
		dataType : "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			fnrsimple : fnumber
		},
		success : function(data) {
			
			removePUDQueryError(queryID);
			
			if (data.features == undefined) {
				
				showPUDQueryErrorMessage(queryID, pudQueryLanguage.PUD_NOT_FOUND);
			
			} else if (data.features.length == 1) {
				
				var pud = data.features[0].properties.name;
				
				pudField.val(pud);
				pudField.parent().find("span").text(pud);
				pudField.parent().show();
				pudField.change();
				
			} else {
				
				showPUDQueryErrorMessage(queryID, pudQueryLanguage.TO_MANY_PUD_FOUND);
			}
					
			$input.removeClass("ui-autocomplete-loading");
		},
		error : function() {
			
			$input.removeClass("ui-autocomplete-loading");
			removePUDQueryError(queryID);
			showPUDQueryErrorMessage(queryID, pudQueryLanguage.UNKOWN_ERROR_MESSAGE);
		}
	});	
}

function makePUDQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}

function showPUDQueryErrorMessage(queryID, message) {
	
	$("#query_" + queryID).find("article").addClass("error").addClass("jserror").before(
		'<div class="info-box first error jserror">' +
			'<span>' +
				'<strong data-icon-before="!" />' + message +
			'</span>' +
			'<div class="marker"></div>' +
		'</div>'
	);
	
}

function removePUDQueryError(queryID) {
	
	$("#query_" + queryID).find("article.jserror").removeClass("error").removeClass("jserror").prev(".jserror").remove();
	
}