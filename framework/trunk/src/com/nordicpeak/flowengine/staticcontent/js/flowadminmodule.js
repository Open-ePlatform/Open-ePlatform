var validationError = false;

$(document).ready(function() {
	
	$("table.coloredtable").each(function() {
		var $this = $(this);
		
		$this.bind("change", function() {
			$this.find("tr:not(:first):visible:even").removeClass("odd").addClass("even");
			$this.find("tr:not(:first):visible:odd").removeClass("even").addClass("odd");
		});
		
		$this.trigger("change");
	});
	
	$(".sortable").sortable({
		cursor: 'move',
		update: function(event, ui) {
			
			if(validatePosition($(this), ui.item, ui.position)) {
				updateSortOrder($(this));
			}
			
		},
		stop: function(e, ui) {
			
			resetHighlightning();
			
			return validatePosition($(this), ui.item, ui.position);
			
		},
		start: function(e, ui) {
			
			highlightAffectedQueries(ui.item);
			
		}}).children().each(function(i) {
			if(validationError) {
				var item = $(this);
				var itemSortOrder = item.find('input[type="hidden"].sortorder').val();
				if(i != itemSortOrder) {
					item.parent().children().each(function(i) {
						if(itemSortOrder == i) {l
							$(this).before(item.detach());
							return;
						}
					});
				}
			}
	});
	
	$(".sortable").each(function() {
		updateSortOrder($(this));
	});
	
	$("#flowtype").change(function(e) {
		$(".flowTypeCategories").hide();
		$(".flowTypeCategories select").attr("disabled", "disabled");
		$("#flowTypeCategories_" + $(this).val() + " select").removeAttr("disabled");
		$("#flowTypeCategories_" + $(this).val()).show();
	});
	
	$("#flowtype").trigger("change");
	
	$("#typeOfFlow").change(function(e) {
		
		var $flowForm = $("#flowForm");
		
		if($(this).val() == "EXTERNAL") {
			$flowForm.find(".internal").hide();
			$("#enabled").removeAttr("disabled");
			$("#externalLink").removeAttr("disabled").parent().parent().show();
		} else {
			$flowForm.find(".internal").show();
			$("#enabled").attr("disabled", "disabled");
			$("#externalLink").attr("disabled", "disabled").parent().parent().hide();
		}
		
	});
	
	$("#typeOfFlow").trigger("change");
	
	
	$("#checkall").click(function() {
		
		if($(this).attr("checked")) {
			$("input[type='checkbox'][name='queryType']").attr("checked", "checked");
		} else {
			$("input[type='checkbox'][name='queryType']").removeAttr("checked");
		}
		
	});
	
	$("input[type='checkbox'][name='queryType']").click(function(){
		
		checkCheckboxState();
	});

	var checkCheckboxState = function() {
		
		var checkedQueryTypes = $("input[type='checkbox'][name='queryType']:checked").length;
		var totalQueryTypes = $("input[type='checkbox'][name='queryType']").length;
		
		if(checkedQueryTypes == totalQueryTypes) {
			$("#checkall").attr("checked","checked");
		} else {
			$("#checkall").removeAttr("checked");
		}
		
	};
	
	checkCheckboxState();	
	
	$(".flowtype-icon-preview a").click(function(e) {
		
		e.preventDefault();
		
		$(this).parent().parent().addClass("hidden");
		$(this).parent().find("input[type='hidden']").removeAttr("disabled").val("true");
	});
	
	$("#flow-status-filter").change(function() {
		
		var $this = $(this);
		
		$("#flowlist tbody tr").removeClass("status-filtered").hide();
		
		if($this.val() == "published") {
			
			$("#flowlist tbody tr.published").addClass("status-filtered").show();
			
		} else if($this.val() == "unpublished") {
			
			$("#flowlist tbody tr:not(.published)").addClass("status-filtered").show();
			
		} else {
			
			$("#flowlist tbody tr").addClass("status-filtered").show();
		}
		
		$("input[type='text'].flow-filter-input").trigger("keyup");
		
	});
	
	$("input[type='text'].flow-filter-input").keyup(function() {
		
		var $this = $(this);
		var val = $.trim($this.val()).replace(/ +/g, ' ').toLowerCase();
	    
		$("#flowlist").find(".status-filtered").show().filter(function() {
	        var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        return !~text.indexOf(val);
	    }).hide();
	
		$("#flowlist").trigger("change");
		
	});
	
	$("#flow-status-filter").trigger("change");
	
	$("input[type='text'].color-input").minicolors();
	
	// Copied from flowinstancebrowser.js
	$(".section-inside div.description").expander({
		slicePoint : 110,
		expandText : "",
		userCollapseText : "",
		expandEffect: "show",
		collapseEffect: "hide",
		expandSpeed: 0,
		collapseSpeed: 0,
		onSlice: function(options){
			
			var div = $(this);
			var noExpandElements = div.find(".noexpander");
			
			if (noExpandElements.length > 0) {
			
				var belowSummary = $('<div class="after-description-summary only-mobile description"/>');
				noExpandElements.clone().appendTo(belowSummary);
				belowSummary.insertAfter(div);
			}
		}
	});
	
	// Copied from flowinstancebrowser.js
	$(document).on("click", ".btn-readmore", function(e) {
        e.preventDefault();
        e.returnValue = false;
        
        var descriptionDiv = $(this).parents(".description");
        descriptionDiv.addClass("active");
        
		var belowSummaryDiv = descriptionDiv.siblings(".after-description-summary");
		
		if (belowSummaryDiv.length > 0) {
			
			belowSummaryDiv.remove();
		}
    });
	
});

function updateSortOrder(obj) {
	obj.children().each(function(i) {
		$(this).find("input[type='hidden'].sortorder").val(i);
	});
}

function highlightAffectedQueries($item) {
	
	var $targetQueryIDs = $item.find("input.targetQueryIDs");
	
	if ($targetQueryIDs.length > 0) {
		
		$targetQueryIDs.each(function() { $("#query_" + $(this).val()).addClass("affectedQuery");  });
	}
	
	var itemID = $item.attr("id");
	
	if (itemID.startsWith("step")) {
		return;
	}
	
	var queryID = itemID.split("_")[1];
	
	var $relatedQueryIDs = $("input[value='" + queryID + "'].targetQueryIDs");
	
	if ($relatedQueryIDs.length > 0) {
		
		$relatedQueryIDs.each(function() {
			$("#" + $(this).parent().attr("id")).addClass("affectedQuery");
		});
	}
}

function resetHighlightning() {
	
	$(".query").removeClass("affectedQuery");
}

function validatePosition($sortable, $item, newItemPosition) {
	
	if ($($sortable.children(":first")).hasClass("query")) {
		return false;
	}
	
	var itemID = $item.attr("id");
	
	if (itemID.startsWith("step")) {
		return true;
	}
	
	var queryID = itemID.split("_")[1];
	
	var isValidPosition = true;
	
	var $targetQueryIDs = $("input[name='targetQueryIDs_" + queryID + "']");
	
	if ($targetQueryIDs.length > 0) {
		
		$targetQueryIDs.each(function() {
			
			var $targetQuery = $("#query_" + $(this).val());
			
			if (newItemPosition.top >= $targetQuery.position().top) {
				isValidPosition = false;
				return;
			}
			
		});
		
		if (!isValidPosition) {
			return false;
		}
		
	}

	var $relatedQueryIDs = $("input[value='" + queryID + "'].targetQueryIDs");
	
	if ($relatedQueryIDs.length > 0) {
		
		$relatedQueryIDs.each(function() {
			
			var $relatedQuery = $("#" + $(this).parent().attr("id"));
			
			if (newItemPosition.top <= $relatedQuery.position().top) {
				isValidPosition = false;
				return;
			}
			
		});
		
	}
	
	return isValidPosition;
}