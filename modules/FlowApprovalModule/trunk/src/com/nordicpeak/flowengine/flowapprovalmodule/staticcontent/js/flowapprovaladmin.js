$(function() {
	
	if ($("#activityGroupForm").length > 0) {
		
		$("#startStatus, #completeStatus, #denyStatus").each(function(j) {
			var searchInput = $(this);
			var url = $("input[name='statusURL']").val();
			
			searchInput.autocomplete({
				source: function(request, response) {
					return searchFlows(request, response, url, searchInput);
				},
				select: function( event, ui ) {
					
					searchInput.val(ui.item.value);
					return false;
				},
				focus: function(event, ui) {
					event.preventDefault();
				}
			});
		});
		
		$("#useCustomApprovedText").on("change", function(){
			
			var checked = $(this).prop("checked");
			$("#approvedText").attr("disabled", !checked).parent().parent().toggle(checked);
			
			var useDeny = $("#useApproveDeny").prop("checked");
			$("#deniedText").attr("disabled", !(checked && useDeny)).parent().parent().toggle(checked && useDeny);
		})
		
		$("#useApproveDeny").on("change", function(){
			
			var checked = $(this).prop("checked");
			$("#denyStatus").attr("disabled", !checked).parent().parent().toggle(checked);
			
			$("#useCustomApprovedText").trigger("change");
			
		}).trigger("change");
		
		$("#allowRestarts").on("change", function(){
			
			var checked = $(this).prop("checked");
			$("#onlyRestartIfActivityChanges").attr("disabled", !checked).parent().parent().toggle(checked);
			
		}).trigger("change");
		
		$('.notification-text-toggle').on("click", function(){
			
			$(this).closest('.notification').find('.notification-text').toggle();
		});
		
		$('.notification-value-toggle').on("change", function(){
			
			$(this).closest('.notification')
				.find('.notification-value').toggle(this.checked)
				.find('input').prop('disabled', !this.checked);
			
		}).trigger("change");
		
		$("#sendActivityGroupCompletedEmail").on("change", function() {
			
			var $inputs = $("#activityGroupCompletedEmailAttachPDF, #activityGroupCompletedEmailAttachFlowInstancePDF");
			
			$inputs.parent().parent().toggle(this.checked);
			$inputs.prop('disabled', !this.checked);
			
		}).trigger("change");
		
	}
	
	if ($("#activityForm").length > 0) {
		
		$('#allowManagersToAssignOwner').on("change", function(){
			
			$(".allowManagersToAssignOwner").toggle(this.checked && !this.disabled);
			
		});
		
		var $searchResponsibleUser = $('#useResponsibleUserAttributeName');
		var $searchResponsibleGroup = $('#useResponsibleGroupAttributeName');
		
		$searchResponsibleUser.on("change", function(){
			
			$(".useResponsibleUserAttributeName").toggle(this.checked);
			$("#responsibleUserAttributeNames").prop('disabled', !this.checked);
			
			var visible = $searchResponsibleUser.is(":checked") || $searchResponsibleGroup.is(":checked");
			$("#allowManagersToAssignOwner").prop('disabled', !visible).trigger("change").parent().parent().toggle(visible);
			$("#responsibleUserFallbackDiv").toggle(visible);
			
		}).trigger("change");
		
		$searchResponsibleGroup.on("change", function(){
			
			$(".useResponsibleGroupAttributeName").toggle(this.checked);
			$("#responsibleGroupAttributeNames").prop('disabled', !this.checked);
			
			var visible = $searchResponsibleUser.is(":checked") || $searchResponsibleGroup.is(":checked");
			$("#allowManagersToAssignOwner").prop('disabled', !visible).trigger("change").parent().parent().toggle(visible);
			$("#responsibleUserFallbackDiv").toggle(visible);
	
		}).trigger("change");
		
		$('#useAttributeFilter').on("change", function(){
			
			$(".useAttributeFilter").toggle(this.checked).find("input,textarea").prop('disabled', !this.checked);
			
		}).trigger("change");
	}
	
	if ($("#activityGroupsSortingForm").length > 0) {
		
		var updateSortOrder = function(obj) {
			obj.children().each(function(i) {
				$(this).find("input[type='hidden'].sortorder").val(i);
			});
		};
		
		$(".sortable").sortable({
			cursor: 'move',
			update: function(event, ui) {
				updateSortOrder($(this));
			},
		});
		
		$(".sortable").each(function() {
			updateSortOrder($(this));
		});
	}
	
	$("#showFlowInstance").on("change", function(e) {
			var checked = $(this).prop("checked");
			$("#pdfDownloadActivation").attr("disabled", !checked).parent().parent().toggle(checked);
			
		}).trigger("change");
		
	$("#requireComment").on("change", function(e) {
			var checked = $(this).prop("checked");
			if(checked) {
				$("#whenToComment").show();
			}
			else {
				$("#whenToComment").hide();
			}
			
		}).trigger("change");
});

function searchFlows(request, response, searchURL, searchInput) {
	
	searchInput.addClass("ui-autocomplete-loading");
	
	$.ajax({
		url : searchURL,
		dataType : "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			q : encodeURIComponent(request.term)
		},
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
