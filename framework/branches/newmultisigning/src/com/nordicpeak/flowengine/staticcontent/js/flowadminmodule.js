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
	
	$("#useAccessCheck").change(function(e) {
		
		var checked = $(useAccessCheck).prop("checked");
		
		$("#allowedManagers").toggle(checked).find("input").not(".usergroup-list input").prop("disabled", !checked);
	}).trigger("change");
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
	
	if (itemID.indexOf("step") == 0) {
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
	
	if (itemID.indexOf("step") == 0) {
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

function updateManagerShowHideRowExtra(row) {
	
	var userID = row.find("input[name='manager']").val();
	row.find(".restricted").toggle(row.find("input[name='manager-restricted" + userID + "']").val() == "true");
	
	var validFrom = row.find("input[name='manager-validFromDate" + userID + "']").val();
	var validTo = row.find("input[name='manager-validToDate" + userID + "']").val();
	
	row.find(".validfrom").toggle(validFrom != "").find("span").text(validFrom);
	row.find(".validto").toggle(validTo != "").find("span").text(validTo);
}

function updateManagerGroupShowHideRowExtra(row) {
	
	var groupID = row.find("input[name='manager-group']").val();
	row.find(".restricted").toggle(row.find("input[name='manager-group-restricted" + groupID + "']").val() == "true");
}

function openUpdateManagerModal(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var modalContainer = $("#updateManagerModal");
	var modal = modalContainer.find(".manager-modal");
	var row = $(button).closest(".manager-list-entry");
	var userID = row.find("input[name='manager']").val();
	var userName = row.find("input[name='manager-name" + userID + "']").val();
	
	var config = {
			otherClose: '.close',
			
			beforeOpen: function() {
				
				var h1 = modal.find(".modal-header").find("h1");
				h1.text(h1.data("title") + " " + userName);
				
				modal.find("input, select").each(function() {
					
					var input = $(this);
					var savedInput = row.find("input[name^='manager-" + input.prop("name") + userID + "']");
					
					if (savedInput.length > 0) {
						
//						console.info("load " + input.prop("name") + " " + savedInput.val()); 
						
						if (input.attr("type") == "checkbox") {
							input.prop("checked", savedInput.val() == "true");
							
						} else {
							input.val(savedInput.val());
						}
					}
				});
			},
	
			afterContent: function() {
				var feather = this;
				var content = feather.$content; 
				content.removeClass("no-sections");
				
				if (!Modernizr.inputtypes.date) {
					
					content.closest(".featherlight").css("z-index", 1000).css("background", "rgba(0,0,0,.8");
					
					content.find("input[type='date']").datepicker({
			            changeMonth: true,
			            changeYear: true,
			            showWeek: true,
					});
					
				}
				
				modal.detach();
			},
			
			beforeClose: function() {
				var feather = this;
				
				feather.$content.find("input, select").each(function() {
					
					var input = $(this);
					var val;
					
					if (input.attr("type") == "checkbox") {
						val = input.prop("checked");
						
					} else {
						val = input.val();
					}
					
					row.find("input[name^='manager-" + input.prop("name") + userID + "']").val(val);
					
//					console.info("store " + input.prop("name") + " " + val);
				});
				
				updateManagerShowHideRowExtra(row);
				modalContainer.append(modal);
			},
	};
	
	$.featherlight(modal, config);
}

function openUpdateManagerGroupModal(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var modalContainer = $("#updateManagerGroupModal"); 
	var modal = modalContainer.find(".manager-modal");
	var row = $(button).closest(".manager-group-list-entry");
	var groupID = row.find("input[name='manager-group']").val();
	var groupName = row.find("input[name='manager-group-name" + groupID + "']").val();
	
	var config = {
			otherClose: '.close',
			
			beforeOpen: function(){
				
				var h1 = modal.find(".modal-header").find("h1");
				h1.text(h1.data("title") + " " + groupName);
				
				modal.find("input, select").each(function(){
					
					var input = $(this);
					var savedInput = row.find("input[name^='manager-group-" + input.prop("name") + groupID + "']");
					
					if (savedInput.length > 0) {
						
//						console.info("load " + input.prop("name") + " " + savedInput.val()); 
						
						if (input.attr("type") == "checkbox") {
							input.prop("checked", savedInput.val() == "true");
							
						} else {
							input.val(savedInput.val());
						}
					}
				});
			},
	
			afterContent: function(){
				var feather = this;
				var content = feather.$content; 
				content.removeClass("no-sections");
				
				if (!Modernizr.inputtypes.date) {
					
					content.closest(".featherlight").css("z-index", 1000).css("background", "rgba(0,0,0,.8");
					
					content.find("input[type='date']").datepicker({
			            changeMonth: true,
			            changeYear: true,
			            showWeek: true,
					});
					
				}
				
				modal.detach();
			},
			
			beforeClose: function(){
				var feather = this;
				
				feather.$content.find("input, select").each(function(){
					
					var input = $(this);
					var val;
					
					if (input.attr("type") == "checkbox") {
						val = input.prop("checked");
						
					} else {
						val = input.val();
					}
					
					row.find("input[name^='manager-group-" + input.prop("name") + groupID + "']").val(val);
					
//					console.info("store " + input.prop("name") + " " + val);
				});
				
				updateManagerGroupShowHideRowExtra(row);
				modalContainer.append(modal);
			},
	};
	
	$.featherlight(modal, config);
}

function addAutoManagerAssignmentRule(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var clone = $("#auto-manager-rule-template").clone();
	var ruleID = (function() {
		var chars = '0123456789abcdef'.split('');
		var uuid = [], rnd = Math.random;
		var r;
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		uuid[14] = '4';
		for (var i = 0; i < 36; i++) {
			if (!uuid[i]) {
				r = 0 | rnd()*16;
				uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
			}
		}
		return uuid.join('');
	})();
	
	clone.attr("id", "");
	clone.find("input[name='auto-manager-rule']").val(ruleID);
	
	clone.find("input").prop("disabled", false).each(function() {
		
		var input = $(this);
		
		if (input.attr("name").lastIndexOf("auto-manager-rule-", 0) === 0) { // Starts with
			
			input.attr("name", input.attr("name") + "-" + ruleID);
		}
	});
	
	clone.insertBefore($("#auto-manager-rule-template"));
	clone.show();
	
	clone.find("a.open-auto-manager-modal").click();
}

function removeAutoManagerAssignmentRule(button, event, text) {
	event.preventDefault();
	event.stopPropagation();
	
	if (confirm(text)) {
		$(button).closest(".auto-manager-rule").remove();
	}
}

function openAutoManagerAssignmentRuleModal(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var modalContainer = $("#updateAutoManagerModal"); 
	var modal = modalContainer.find(".auto-manager-modal");
	var row = $(button).closest("tr.auto-manager-rule");
	
	var ruleID = row.find("input[name='auto-manager-rule']").val();
	
	var config = {
			otherClose: '.close',
			
			beforeOpen: function() {
				
				modal.find("input.ui-autocomplete-input").autocomplete("destroy");
				
				modal.find("input, select, textarea").each(function() {
					
					var input = $(this);
					var savedInput = row.find("input[name^='auto-manager-rule-" + input.prop("name") + "-" + ruleID + "']");
					
					if (savedInput.length > 0) {
						
//						console.info("load " + input.prop("name") + " " + savedInput.val()); 
						
						if (input.attr("type") == "checkbox") {
							input.prop("checked", savedInput.val() == "true");
							
						} else if (input.attr("type") == "textarea") {
							input.text(avedInput.val());
							
						} else {
							input.val(savedInput.val());
						}
					}
				});
				
				// Move user-group-list data
				var userList = $("#template-user-list");
				var groupList = $("#template-group-list");
				userList.find(".template-user-list-entry").remove();
				groupList.find(".template-group-list-entry").remove();
				
				row.find(".auto-manager-users .template-user-list-entry").appendTo(userList);
				row.find(".auto-manager-groups .template-group-list-entry").appendTo(groupList);
			},
	
			afterContent: function() {
				var feather = this;
				var content = feather.$content; 
				content.removeClass("no-sections");
				feather.$instance.addClass("ui-front");
				
				modal.detach();
				
				content.find(".usergroup-list").each(function() {
					initUserGroupList($(this));
				});
				
				if (row.hasClass("new_row")) {
					content.find("input[name='attribute']").focus();
				}
			},
			
			beforeClose: function() {
				var feather = this;
				var content = feather.$content;
				
				// Destroy and remove listeners
				content.find("input.ui-autocomplete-input").autocomplete("destroy");
				content.find(".usergroup-list").off("change");
				
				var users = content.find(".template-user-list-entry"); 
				var groups = content.find(".template-group-list-entry");
				users.find("a.delete").off("click");
				groups.find("a.delete").off("click");
				
				// Copy values to row
				if (row.hasClass("new_row") && content.find("input[name='attribute']").val() == "") {
					
					row.remove();
					
				} else {
					
					row.removeClass("new_row");
				
					content.find("input, select, textarea").each(function() {
						
						var input = $(this);
						var val;
						
						if (input.attr("type") == "checkbox") {
							val = input.prop("checked");
							
						} else if (input.attr("type") == "textarea") {
							val = input.text();
							
						} else {
							val = input.val();
						}
						
						row.find("input[name^='auto-manager-rule-" + input.prop("name") + "-" + ruleID + "']").val(val);
						
//						console.info("store " + input.prop("name") + " " + val);
					});
					
					// Move user-group-list data
					var rowUserList = row.find(".auto-manager-users > div");
					var rowGroupList = row.find(".auto-manager-groups > div");
					rowUserList.find(".template-user-list-entry").remove();
					rowGroupList.find(".template-group-list-entry").remove();
					
					users.find("input").attr("disabled", true);
					groups.find("input").attr("disabled", true);
					
					users = users.appendTo(rowUserList);
					groups = groups.appendTo(rowGroupList);
					
					// Store user and group IDs
					var userIDs = "";
					users.find("input[name='template-user']").each(function(index){
						var input = $(this);
						
						if (index > 0) {
							userIDs += ",";
						}
						
						userIDs += input.val();
					});
					
					var groupIDs = "";
					groups.find("input[name='template-group']").each(function(index){
						var input = $(this);
						
						if (index > 0) {
							groupIDs += ",";
						}
						
						groupIDs += input.val();
					});
					
					row.find("input[name='auto-manager-rule-users-" + ruleID + "']").val(userIDs);
					row.find("input[name='auto-manager-rule-groups-" + ruleID + "']").val(groupIDs);
					
					refreshAutoManagerAssignmentRule(row, ruleID);
				}
				
				modalContainer.append(modal);
			},
	};
	
	$.featherlight(modal, config);
}

function refreshAutoManagerAssignmentRule(row, ruleID) {
	
	var ruleAttribute = row.find("input[name='auto-manager-rule-attribute-" + ruleID + "']").val();
	var ruleValues = row.find("input[name='auto-manager-rule-values-" + ruleID + "']").val();
	var ruleInvert = row.find("input[name='auto-manager-rule-invert-" + ruleID + "']").val();
	var ruleUsers = row.find(".auto-manager-users .template-user-list-entry");
	var ruleGroups = row.find(".auto-manager-groups .template-group-list-entry");
	
	row.find(".auto-manager-attribute").text(ruleAttribute);
	row.find(".auto-manager-values").text(ruleValues.replace(/\n/g, ", "));
	row.find(".auto-manager-invert > span").toggle(ruleInvert == "true");
	row.find(".auto-manager-users > span").text(ruleUsers.length);
	row.find(".auto-manager-groups > span").text(ruleGroups.length);	
}