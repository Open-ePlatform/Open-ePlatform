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