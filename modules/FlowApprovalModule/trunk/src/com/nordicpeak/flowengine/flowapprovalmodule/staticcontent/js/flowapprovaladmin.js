$(document).ready(function() {
	
	if($("#activityGroupForm").length > 0) {
		
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
		
		 $("#useApproveDeny").change(function(){
			
			var checked = $(this).prop("checked");
			$("#denyStatus").attr("disabled", !checked).parent().parent().toggle(checked);
			
		}).change();
		
		$('.notification-text-toggle').click(function(){
			
			$(this).closest('.notification').find('.notification-text').toggle();
		});
		
		$('.notification-value-toggle').change(function(){
			
			$(this).closest('.notification')
				.find('.notification-value').toggle(this.checked)
				.find('input').prop('disabled', !this.checked);
			
		}).change();
	}
	
	if( $("#activityGroupsSortingForm").length > 0) {
		
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

function responsibleUserShowHideRowExtra(row) {
	
	var userID = row.find("input[name='responsible-user']").val();
	row.find(".fallback").toggle(row.find("input[name='responsible-user-fallback" + userID + "']").val() == "true");
}

function toggleResponsibleUserFallback(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var row = $(button).closest(".responsible-user-list-entry");
	var userID = row.find("input[name='responsible-user']").val();
	var fallback = row.find("input[name='responsible-user-fallback" + userID + "']");
	
	if (fallback.val() == "true") {
		
		fallback.val("false");
		
	} else {
		
		fallback.val("true");
	}
	
	responsibleUserShowHideRowExtra(row);
}
