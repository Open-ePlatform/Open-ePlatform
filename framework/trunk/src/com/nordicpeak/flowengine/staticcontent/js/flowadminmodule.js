var FlowAdmin;
var validationError = false;
var flowListDataTable;
var getExtraFlowListColumns;

$(function() {
	
	if (FlowAdmin != undefined) {
	
		var flowListTable = $(".flow-list-table");
		
		var columns = [
			{ name:"flowID", orderable: false, searchable: false, visible: false },
			{ name:"published", orderable: false, searchable: false, visible: false },
			{ name:"icon", orderable: false, searchable: false,
				render: function(data, type, row) {
			
					if (type == "display") {
						
						return "<img src='" + FlowAdmin.iconURL + data + "' width='25' alt='' />";
					}
					
					return null;
				}
			},
			{ name:"flowName",
				render: function(data, type, row) {
			
					if (type == "display") {
						
						return "<span " + (data.hasExternalVersions ? "data-icon-after='e'" : "") + ">" + $.fn.dataTable.render.text().display(data.flowName) + "</span>";
					}
					
					return data.flowName;
				}					
			},
			{ name:"flowType", searchable: false,
				render: function(data, type, row) {
			
					if (type == "display") {
						
						return "<a href='" + FlowAdmin.showFlowTypeURL + data.flowTypeID + "'>" + $.fn.dataTable.render.text().display(data.flowTypeName) + "</a>";
					}
					
					return data.flowTypeName;
				}
			},					
			{ name:"flowCategory", searchable: false, visible: FlowAdmin.useCategories },
			{ name:"versions", searchable: false },
			{ name:"submittedInstances", searchable: false },
			{ name:"notSubmittedInstances", searchable: false },
			{ name:"flowFamilyLastReviewed", type: "date" }
		]
		
		if (getExtraFlowListColumns) {
			
			columns = columns.concat(getExtraFlowListColumns());
		}
		
		columns.push(
			{ name:"delete", orderable: false, searchable: false,
				render: function(data, type, row) {
			
					if (type == "display") {
						
						if (data.hasInstances) {
							
							return "<a href='#' onclick='alert(\"" + FlowAdmin.i18n.deleteFlowFamilyDisabledHasInstances + "\"); return false;' title=\"" + FlowAdmin.i18n.deleteFlowFamilyDisabledHasInstances + "\"> \
										<img class='alignbottom' src='" + FlowAdmin.imgPath + "/delete_gray.png' alt='' /> \
									</a>";
									
						} else if (data.isPublished) {
							
							return "<a href='#' onclick='alert(\"" + FlowAdmin.i18n.deleteFlowFamilyDisabledIsPublished + "\"); return false;' title=\"" + FlowAdmin.i18n.deleteFlowFamilyDisabledIsPublished + "\"> \
										<img class='alignbottom' src='" + FlowAdmin.imgPath + "/delete_gray.png' alt='' /> \
									</a>";
									
						} else {
							
							return "<a href='" + FlowAdmin.deleteFlowFamilyURL + data.flowFamilyID + "' onclick='return confirmHyperlinkPost(this)' title='" + FlowAdmin.i18n.deleteFlowFamilyTitle + ": " + $.fn.dataTable.render.text().display(data.flowName) + "'> \
										<img class='alignbottom' src='" + FlowAdmin.imgPath + "/delete.png' alt='' /> \
									</a>";
						}
					}
					
					return null;
				}
		});
		
		flowListDataTable = flowListTable.DataTable({
			ajax: {
				url: flowListTable.data("url"),
				dataSrc: 'rows',
				beforeSend: function(jqXHR, settings) {
					jqXHR.setRequestHeader('NoLoginRedirect', 'true');
				}
			},		
			deferRender: true,
			columns: columns,
			order: [[ 3, "asc"]],
			dom: '<"toolbar-extension">frtipl',
			autoWidth: false,
			lengthMenu: [ 15, 25, 50, 100 ],
			language: {
				"decimal":        FlowAdmin.i18n.decimal,
				"emptyTable":     FlowAdmin.i18n.emptyTable,
				"info":           FlowAdmin.i18n.info,
				"infoEmpty":      FlowAdmin.i18n.infoEmpty,
				"infoFiltered":   FlowAdmin.i18n.infoFiltered,
				"infoPostFix":    FlowAdmin.i18n.infoPostFix,
				"thousands":      FlowAdmin.i18n.thousands,
				"lengthMenu":     FlowAdmin.i18n.lengthMenu,
				"loadingRecords": FlowAdmin.i18n.loadingRecords,
				"processing":     FlowAdmin.i18n.processing,
				"search":         FlowAdmin.i18n.search,
				"zeroRecords":    FlowAdmin.i18n.zeroRecords,
				paginate: {
					"first":      FlowAdmin.i18n.paginate.first,
					"last":       FlowAdmin.i18n.paginate.last,
					"next":       FlowAdmin.i18n.paginate.next,
					"previous":   FlowAdmin.i18n.paginate.previous
				},
				aria: {
					"sortAscending":  FlowAdmin.i18n.aria.sortAscending,
					"sortDescending": FlowAdmin.i18n.aria.sortDescending
				},
				select: {
					rows: {
						_: FlowAdmin.i18n.select.rows.many,
						0: FlowAdmin.i18n.select.rows.none,
						1: FlowAdmin.i18n.select.rows.one
					}
				},
			},
			preDrawCallback: function(settings) {
				
				// Remove callback
				settings.aoPreDrawCallback.splice(0, 1);
				
				settings.oClasses.sPageButton = "btn btn-inline btn-light";
				
				var filterDiv = $(settings.nTableWrapper).find(".dataTables_filter");
				var filterInput = filterDiv.find("input");
				var filterLabel = filterDiv.find("label"); 
				filterInput.attr("placeholder", filterLabel.text());
				filterDiv.append(filterInput);
				filterLabel.remove();
				
			},		
		});
		
		flowListTable.find("tbody").show();
		
		flowListDataTable.on('click', 'tbody td', function(event) {
		
			if (event.target.nodeName == "A" || (event.target.nodeName == "IMG" && event.target.parentElement.nodeName == "A")) {
				return;
			}

			var td = $(this);
			var tr = td.parent();
			
			if (tr.children("td").first().hasClass("dataTables_empty")) {
				return;
			}
			
			var flowID = flowListDataTable.cell(tr, "flowID:name").data();
			var url = FlowAdmin.showFlowURL + flowID;
			
			location.href = url;
		});
		
		$.fn.dataTable.ext.search.push(function(settings, data, dataIndex, rowData, counter) {
	
			var filterValue = $("#flow-status-filter").val();
			
			return filterValue == "all" || filterValue == rowData[flowListDataTable.column("published:name").index()];
		});
		
		$("#flow-status-filter").change(function() {
			
			flowListDataTable.draw();
			
		});
		
		$(".flow-list-filters").appendTo(".toolbar-extension").show();

	}
	
	$("table.coloredtable").each(function() {
		var $this = $(this);
		
		$this.on("change", function() {
			$this.find("tr:not(:first):visible:even").removeClass("odd").addClass("even");
			$this.find("tr:not(:first):visible:odd").removeClass("even").addClass("odd");
		});
		
		$this.trigger("change");
	});
	
	$(".sortable").sortable({
		cursor: 'move',
		update: function(event, ui) {
			
			if (validatePosition($(this), ui.item, ui.position)) {
				updateSortOrder($(this));
			}
			
		},
		stop: function(e, ui) {
			
			resetHighlightning();
			
			return validatePosition($(this), ui.item, ui.position);
			
		},
		start: function(e, ui) {
			
			highlightAffectedQueries(ui.item);
	}});
	
	$(".sortable").each(function() {
		updateSortOrder($(this));
	});
	
	if ($("#statusform").length > 0) { // Add/Update status
		
		$("#isAdminDeletable").on("change", function() {
			$("#isRestrictedAdminDeletable").parent().parent().toggle($(this).is(":checked"));
		}).change();
		
		$("#useAccessCheck").on("change", function() {
			$("#allowedManagers").toggle($(this).is(":checked"));
		}).change();
		
		var $addExternalMessage = $('#addExternalMessage');
		var $addInternalMessage = $('#addInternalMessage');
		var $newExternalMessagesDisallowed = $('#newExternalMessagesDisallowed');
		var $requireSigning = $('#requireSigning');

		$newExternalMessagesDisallowed.change(function(){
			
			$('#newExternalMessagesAllowedDays').prop('disabled', this.checked);

			toggleAddExternalMessage();
			
		}).change();
		
		$addExternalMessage.change(function(){
			
			$('#defaultExternalMessageTemplate').parent().toggle(this.checked);
			
			toggleRequireSigning();
			
		}).change();
		
		$addInternalMessage.change(function(){
			
			$('#defaultInternalMessageTemplate').parent().toggle(this.checked);
			
			toggleRequireSigning();
			
		}).change();
		
		$requireSigning.change(function(){
			
			toggleAddExternalMessage();
			toggleAddInternalMessage();
			
		}).change();
		
		function toggleAddExternalMessage() {
			
			toggleSetting($addExternalMessage, [$newExternalMessagesDisallowed, $requireSigning]);
		}

		function toggleAddInternalMessage() {
			
			toggleSetting($addInternalMessage, [$requireSigning]);
		}

		function toggleRequireSigning() {
			
			toggleSetting($requireSigning, [$addExternalMessage, $addInternalMessage]);
		}
		
		function toggleSetting($setting, disablingSettings) {
			
			var wasChecked = $setting.prop("checked");
			
			var disabled = disablingSettings.some(function($disablingSetting) {
				
				return $disablingSetting.prop("checked");
			});
			
			$setting.prop("disabled", disabled);
			
			if (disabled) {
				
				$setting.prop("checked", false);
			}
			
			if (wasChecked != $setting.prop("checked")) {
				
				$setting.change();
			}
		}
	}
	
	if ($("#flowForm").length > 0) { // Add/Update flow
	
		//Add flow
		
		$("#flowtype").change(function(e) {
			$(".flowTypeCategories").hide();
			$(".flowTypeCategories select").attr("disabled", "disabled");
			$("#flowTypeCategories_" + $(this).val() + " select").removeAttr("disabled");
			$("#flowTypeCategories_" + $(this).val()).show();
			
		}).trigger("change");
		
		$("#typeOfFlow").change(function(e) {
			
			var $flowForm = $("#flowForm");
			
			if ($(this).val() == "EXTERNAL") {
				
				$flowForm.find(".internal").hide();
				$flowForm.find(".external").show();
				$("#enabled").removeAttr("disabled");
				$("#externalLink").removeAttr("disabled").parent().parent().show();
				$("#addstandardstatuses").prop("checked", false);
				
			} else {
				
				$flowForm.find(".internal").show();
				$flowForm.find(".external").hide();
				$("#enabled").attr("disabled", "disabled");
				$("#externalLink").attr("disabled", "disabled").parent().parent().hide();
				$("#addstandardstatuses").trigger("change");
			}
			
		}).trigger("change");
		
		$("#addstandardstatuses").change(function(e) {
			var checked = this.checked;
			$("#statusGroupID").attr("disabled", !checked).parent().parent().toggle(checked);
			
		}).trigger("change");
		
		// Add and Update flow
		
		$("#requireAuthentication").change(function(e) {
			
			var checked = $(this).prop("checked");
			var allowForeignIDs = $("#allowForeignIDs");
			
			allowForeignIDs.parent().parent().toggle(checked);
			
		}).trigger("change");
		
		$("#skipPosterSigning").change(function(e) {
			
			var checkbox = $(this);
			var checked = checkbox.prop("checked") && checkbox.parent().parent().css('display') !== 'none';
			var fields = $("#allowPosterMultipartSigning");
			
			fields.parent().parent().toggle(checked);
		})
		
		$("#requireSigning").change(function(e) {
			
			var checked = $(this).prop("checked");
			var fields = $("#useSequentialSigning, #skipPosterSigning, #appendSigningSignatureToPDF, #showPreviousSignaturesToSigners");
			
			fields.parent().parent().toggle(checked);
			
			$("#skipPosterSigning").trigger("change");
			
		}).trigger("change");
		
		$('#hideExternalMessages').change(function() {

			$('#hideExternalMessageAttachments').parent().parent().toggle(!this.checked);
		
		}).trigger('change');
		
		$("#hideFromUser").change(function(e) {
			
			var checked = $(this).prop("checked");
			
			var fields = $("#hideExternalMessages");
			
			fields.prop("disabled", checked);
			
			if (checked) {
				fields.prop("checked", true).trigger("change");
			}
			
		}).trigger("change");
		
		$("#useAccessCheck").change(function(e) {
			
			var checked = $(useAccessCheck).prop("checked");
			
			$("#allowedManagers").toggle(checked).find("input").not(".usergroup-list input").prop("disabled", !checked);
			
		}).trigger("change");
	}
	
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
	
	$("select[name='queryTypeID']").change(function (e){
		
		var description = $("input[name = 'queryTypeDescription_" + $(this).val() + "']").val();
		
		if(description) {
			$("#queryTypeDescription").text(description);
		} else {
			$("#queryTypeDescription").text("Den valda frågetypen har ännu ingen beskrivning");
		}
			
	}).trigger("change");
	
	$(".flowtype-icon-preview a").click(function(e) {
		
		e.preventDefault();
		
		$(this).parent().parent().addClass("hidden");
		$(this).parent().find("input[type='hidden']").removeAttr("disabled").val("true");
	});
	
	$("input[type='text'].color-input").minicolors();
	
	var $readmoreText = $(".readmore-text");

	if ($readmoreText.length > 0 && $readmoreText.text().length <= 110) {	
		  $(".btn-readmore").remove();
	}
	
	var $flowMenu = $("#flowMenu");
	
	if ($flowMenu.length > 0) {
		
		$flowMenu.closest("aside").addClass("narrow");
		
		$(window).scroll(checkFlowMenuPosition);
		$(window).resize(checkFlowMenuPosition);
		
	}
	
	$("#show-inactive-providers").click(function(e) {
		
		e.preventDefault();
		
		$("#hide-inactive-providers").show();
		
		$(this).hide();
		$(".showflow-wrapper.extension.hidden").removeClass("hidden");
		
	});
	
	$("#hide-inactive-providers").click(function(e) {
		
		e.preventDefault();
		
		$("#show-inactive-providers").show();
		
		$(this).hide();
		$(".showflow-wrapper.extension.inactive").addClass("hidden");
		
	});
	
	$(document).on("click", ".showflow-moreinfo-footer a.show-more", function(e) {
		
		e.preventDefault();
		
		var $this = $(this);
		$this.hide();
		$this.parent().find("a.show-less").show();
		
		$("[data-rel = '" + $this.data("rel") + "']").removeClass("hidden");
	});
	
	$(document).on("click", ".showflow-moreinfo-footer a.show-less", function(e) {
		
		e.preventDefault();
		
		var $this = $(this);
		$this.hide();
		$this.parent().find("a.show-more").show();
		
		$("[data-rel = '" + $this.data("rel") + "']").addClass("hidden");
	});
	
	$(document).on("click", "ul.steps li.step > .title", function(e) {
		
		if($(e.target).closest("a").length) {
			return;
		}
		
		var $step = $(this).parent();
		var $queries = $step.find("ul.querydescriptors");
		
		if($queries.is(":visible")) {
			$queries.slideUp("fast");
			$.cookie("oep.flowSteps." + $step.data("cookiesuffix"), "false");
		} else {
			$queries.slideDown("fast");
			$.cookie("oep.flowSteps." + $step.data("cookiesuffix"), "true");
		}
		
	});
	
	var $steps = $("ul.steps");
	
	$(document).on("click", ".comments-btn", function(e) {
		
		e.preventDefault();
		
		var $this = $(this);
		
		if($this.hasClass("show-all-comments")) {
			$steps.addClass("show-comments");
			$(".hide-all-comments").show();
			$.cookie("oep.qeComments." + $steps.data("flowid"), "true");
		} else {
			$steps.removeClass("show-comments");
			$(".show-all-comments").show();
			$.cookie("oep.qeComments." + $steps.data("flowid"), "false");
		}

		$this.hide();
		
	});
	
	if($.cookie("oep.qeComments." + $steps.data("flowid")) == "true") {
		$steps.addClass("show-comments");
		$(".hide-all-comments").show();
		$(".show-all-comments").hide();
		
	}
	
	$steps.find("li.step").each(function() {
		
		var $this = $(this);
		
		var expanded = $.cookie("oep.flowSteps." + $this.data("cookiesuffix"));
		
		if(expanded == "true") {
			$this.find("ul.querydescriptors").show();
		} else {
			$this.find("ul.querydescriptors").hide();
		}
		
	});

	// Copied from flowinstancebrowser.js
	$(".section-inside div.description .use-expandable .readmore-text").expander({
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

	if ($("#updateManagementInfo").length && !Modernizr.inputtypes.date) {
		
		("#updateManagementInfo").find("input[type='date']").datepicker({
			changeMonth: true,
			changeYear: true,
			showWeek: true,
			dayNamesShort: true,
			onSelect: function() {
				$(this).trigger("blur");
			}
		});
	}
	
});

function checkFlowMenuPosition() {
	
	var win = $(this);
	
	var $flowMenu = $("#flowMenu");
	
	var headerHeight = ($("header").height() + 30);
	
	if (win.scrollTop() >= headerHeight) {
		
		var width = $flowMenu.width();
		$flowMenu.css("width", width);
		$flowMenu.addClass("fixed");
		
	} else if (win.scrollTop() < headerHeight) {
		
		$flowMenu.css("width", "");
		$flowMenu.removeClass("fixed");
		
	}
	
	if($(window).width() < 767) {
		$flowMenu.removeClass("fixed");
	}
	
}

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
	
	if (itemID === undefined || itemID.indexOf("step") == 0) {
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
	
	if (itemID === undefined || itemID.indexOf("step") == 0) {
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
	row.find(".allowUpdatingManagers").toggle(row.find("input[name='manager-allowUpdatingManagers" + userID + "']").val() == "true");
	
	var validFrom = row.find("input[name='manager-validFromDate" + userID + "']").val();
	var validTo = row.find("input[name='manager-validToDate" + userID + "']").val();
	
	row.find(".validfrom").toggle(validFrom != "").find("span").text(validFrom);
	row.find(".validto").toggle(validTo != "").find("span").text(validTo);
}

function updateManagerGroupShowHideRowExtra(row) {
	
	var groupID = row.find("input[name='manager-group']").val();
	row.find(".restricted").toggle(row.find("input[name='manager-group-restricted" + groupID + "']").val() == "true");
	row.find(".allowUpdatingManagers").toggle(row.find("input[name='manager-group-allowUpdatingManagers" + groupID + "']").val() == "true");
}

function updateManagerShowHideModalFields(content) {
	
	var restrictedField = content.find("input[name='restricted']");
	var allowUpdatingManagersField = content.find("input[name='allowUpdatingManagers']");
	
	restrictedField.change(function(){
		
		var restricted = restrictedField.prop("checked")
		allowUpdatingManagersField.parent().toggle(restricted);
		
		if (!restricted) {
			allowUpdatingManagersField.prop("checked", false);
		}
		
	}).change();
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
					var savedInput = row.find("input[name='manager-" + input.prop("name") + userID + "']");
					
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
				
				updateManagerShowHideModalFields(content);
				
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
					
					row.find("input[name='manager-" + input.prop("name") + userID + "']").val(val);
					
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
				
				modal.find("input, select, textarea").each(function(){
					
					var input = $(this);
					var savedInput = row.find("input[name='manager-group-" + input.prop("name") + groupID + "']");
					
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
				
				updateManagerShowHideModalFields(content);
				
				modal.detach();
			},
			
			beforeClose: function(){
				var feather = this;
				
				feather.$content.find("input, select, textarea").each(function(){
					
					var input = $(this);
					var val;
					
					if (input.attr("type") == "checkbox") {
						val = input.prop("checked");
						
					} else {
						val = input.val();
					}
					
					row.find("input[name='manager-group-" + input.prop("name") + groupID + "']").val(val);
					
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
	var ruleID = generateUUID();
	
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
							input.text(savedInput.val());
							
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
					content.find('#updateAutoManagerModalHeader').children().toggle();
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

function addAutoManagerAssignmentStatusRule(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var clone = $("#auto-manager-status-rule-template").clone();
	var ruleID = generateUUID();
	
	clone.attr("id", "");
	clone.find("input[name='auto-manager-status-rule']").val(ruleID);
	
	clone.find("input").prop("disabled", false).each(function() {
		
		var input = $(this);
		
		if (input.attr("name").lastIndexOf("auto-manager-status-rule-", 0) === 0) { // Starts with
			
			input.attr("name", input.attr("name") + "-" + ruleID);
		}
	});
	
	clone.insertBefore($("#auto-manager-status-rule-template"));
	clone.show();
	
	clone.find("a.open-auto-manager-status-modal").click();
}

function removeAutoManagerAssignmentStatusRule(button, event, text) {
	event.preventDefault();
	event.stopPropagation();
	
	if (confirm(text)) {
		$(button).closest(".auto-manager-status-rule").remove();
	}
}

function openAutoManagerAssignmentStatusRuleModal(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var modalContainer = $("#updateAutoManagerStatusModal"); 
	var modal = modalContainer.find(".auto-manager-status-modal");
	var row = $(button).closest("tr.auto-manager-status-rule");
	
	var ruleID = row.find("input[name='auto-manager-status-rule']").val();
	
	var config = {
			otherClose: '.close',
			
			beforeOpen: function() {
				
				modal.find("input.ui-autocomplete-input").autocomplete("destroy");
				
				modal.find("input, select, textarea").each(function() {
					
					var input = $(this);
					var savedInput = row.find("input[name^='auto-manager-status-rule-" + input.prop("name") + "-" + ruleID + "']");
					
					if (savedInput.length > 0) {
						
						if (input.attr("type") == "checkbox") {
							input.prop("checked", savedInput.val() == "true");
							
						} else if (input.attr("type") == "textarea") {
							input.text(savedInput.val());
							
						} else {
							input.val(savedInput.val());
						}
					}
				});
				
				// Move user-group-list data
				var userList = $("#status-template-user-list");
				var groupList = $("#status-template-group-list");
				userList.find(".status-template-user-list-entry").remove();
				groupList.find(".status-template-group-list-entry").remove();
				
				row.find(".auto-manager-users .status-template-user-list-entry").appendTo(userList);
				row.find(".auto-manager-groups .status-template-group-list-entry").appendTo(groupList);
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
				
				var $statusName = content.find("#statusName");
				
				if (row.hasClass("new_row")) {
					
					content.find('#updateAutoManagerStatusModalHeader').children().toggle();
				}
				
				var url = $statusName.parent().data('connectorUrl');
				
				$statusName.autocomplete({
					source: function(request, response) {
						return getStatuses(response, url, $statusName);
					},
					select: function( event, ui ) {
						
						$statusName.val(ui.item.value);
						return false;
					},
					focus: function(event, ui) {
						event.preventDefault();
					}
				});
				
				$statusName.on('focus', function(){
						
					$(this).autocomplete('search', ' ');
				});
				
				content.find('#addManagers').change(function(){
					
					content.find('#updateAutoManagerStatusManagerContainer').toggle(this.checked);
					
				}).change();
				
				content.find('#sendNotification').change(function(){
					
					content.find('#updateAutoManagerStatusNotificationContainer').toggle(this.checked);
					
				}).change();
			},
			
			beforeClose: function() {
				var feather = this;
				var content = feather.$content;
				var addManagers = content.find('#addManagers');
				
				// Destroy and remove listeners
				content.find("input.ui-autocomplete-input").autocomplete("destroy");
				content.find(".usergroup-list").off("change");
				
				var users = content.find(".status-template-user-list-entry"); 
				var groups = content.find(".status-template-group-list-entry");
				
				if (!users.length && !groups.length) {
					
					addManagers.prop('checked', false);
				}
				
				users.find("a.delete").off("click");
				groups.find("a.delete").off("click");
				
				// Copy values to row
				if (row.hasClass("new_row") && content.find("input[name='statusName']").val() == "") {
					
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
						
						row.find("input[name^='auto-manager-status-rule-" + input.prop("name") + "-" + ruleID + "']").val(val);
					});
					
					// Move user-group-list data
					var rowUserList = row.find(".auto-manager-users > div");
					var rowGroupList = row.find(".auto-manager-groups > div");
					rowUserList.find(".status-template-user-list-entry").remove();
					rowGroupList.find(".status-template-group-list-entry").remove();
					
					users.find("input").attr("disabled", true);
					groups.find("input").attr("disabled", true);
					
					var userIDs = "";
					var groupIDs = "";

					if (addManagers.is(':checked')) {
						
						users = users.appendTo(rowUserList);
						groups = groups.appendTo(rowGroupList);

						users.find("input[name='status-template-user']").each(function(index){
							var input = $(this);
							
							if (index > 0) {
								userIDs += ",";
							}
							
							userIDs += input.val();
						});
						
						groups.find("input[name='status-template-group']").each(function(index){
							var input = $(this);
							
							if (index > 0) {
								groupIDs += ",";
							}
							
							groupIDs += input.val();
						});
					}
					
					row.find("input[name='auto-manager-status-rule-users-" + ruleID + "']").val(userIDs);
					row.find("input[name='auto-manager-status-rule-groups-" + ruleID + "']").val(groupIDs);
					
					refreshAutoManagerAssignmentStatusRule(row, ruleID);
				}
				
				modalContainer.append(modal);
			},
	};
	
	$.featherlight(modal, config);
}

function refreshAutoManagerAssignmentStatusRule(row, ruleID) {
	
	var ruleStatusName = row.find("input[name='auto-manager-status-rule-statusName-" + ruleID + "']").val();
	var ruleAddManagers = row.find("input[name='auto-manager-status-rule-addManagers-" + ruleID + "']").val();
	var ruleUsers = row.find(".auto-manager-users .status-template-user-list-entry");
	var ruleGroups = row.find(".auto-manager-groups .status-template-group-list-entry");
	var ruleRemovePreviousManagers = row.find("input[name='auto-manager-status-rule-removePreviousManagers-" + ruleID + "']").val();
	var sendNotification = row.find("input[name='auto-manager-status-rule-sendNotification-" + ruleID + "']").val();
	
	row.find(".auto-manager-status-name").text(ruleStatusName);
	row.find(".auto-manager-add-managers > span").toggle(ruleAddManagers == "true");
	row.find(".auto-manager-users > span").text(ruleUsers.length);
	row.find(".auto-manager-groups > span").text(ruleGroups.length);
	row.find(".auto-manager-remove-previous-managers > span").toggle(ruleRemovePreviousManagers == "true");
	row.find(".auto-manager-send-notification > span").toggle(sendNotification == "true");
}

function generateUUID() {
	
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
}

function addOverviewAttribute(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var uuid = generateUUID();
	var container = $(button).closest(".overview-attributes-container");
	var alternatives = container.find(".overview-attributes");
	
	if (alternatives.children().length < 6) {
		
		var template = container.find(".overview-attribute-template").children().first().clone();
		
		template.find("input").prop("disabled", false).each(function() {
			
			var input = $(this);
			
			input.attr("id", input.attr("id") + uuid);
			
			if (input.attr("name") == "overviewAttributeID") {
				
				input.val(uuid);
				
			} else {
				
				input.attr("name", input.attr("name") + uuid);
			}
		});
		
		template.find("input.sortorder").val(alternatives.children().length);
		
		alternatives.append(template);
		
	} else {
		alert(container.find(".overview-attribute-template").data("maxtext"));
	}
}

function deleteOverviewAttribute(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	button = $(button);
	
	if (confirm(button.data("confirm"))) {
		
		var alternatives = button.closest(".overview-attributes");
		button.closest(".overview-attribute").remove();
		updateSortOrder(alternatives);
	}
}

function getStatuses(response, searchURL, searchInput) {
	
	searchInput.addClass("ui-autocomplete-loading");
	
	$.ajax({
		url : searchURL,
		dataType : "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		success : function(data) {
			
			if (data.hits != undefined && data.hits.length > 0) {
				
				response($.map(data.hits, function(item) {
					
					return {
						label : item,
						value : item,
					}
				}));
			} else {
				response(null);
			}
			
			searchInput.removeClass("ui-autocomplete-loading");
			
		},
		error : function() {
			
			searchInput.removeClass("ui-autocomplete-loading");
		}
	});
}
