$(document).ready(function() {
	
	 $(".raffleflow-list").each(function(j){
		 var list = $(this);
		 var prefix = "raffleflow";
		 var url = list.find("input[name='connectorURL']").val();
		 
		 var searchInput = $( "#" + prefix + "-search");
		 
		 searchInput.autocomplete({
		 	source: function(request, response) {
		 		return searchFlows(request, response, url, searchInput);
			},
			select: function( event, ui ) {
				
				addEntry(ui.item, list, prefix);
				
				$(this).val("");
				
				return false;
			},
			focus: function(event, ui) {
		        event.preventDefault();
		    }
		 });
		 
		 var template = getTemplate(prefix);
		 template.find("input").prop("disabled", true);
		 template.find("select").prop("disabled", true);
	 });
	 
	 addFlowIDCheckboxes();
	 
	 $("#overrideStatusChangedNotification").change(function (){
		 showHideOverrideNotifications();
	 });
	 showHideOverrideNotifications();
});

function showHideOverrideNotifications(){
	
	$("#statusChangedMessages").toggle($("#overrideStatusChangedNotification").prop('checked'));
}

function addFlowIDCheckboxes(){
	
	var addFlowIDs = $("input[name='addFlowID']");
	
	addFlowIDs.off("click", addFlowIDCheckboxesClicked);
	addFlowIDs.click(addFlowIDCheckboxesClicked);
}

function addFlowIDCheckboxesClicked(){
	
	var clickedCheckbox = this;
	
	$("input[name='addFlowID']").each(function(){
		var checkbox = this;
		
		if(clickedCheckbox !== checkbox){
			$(checkbox).prop('checked', false);
		}
	});
}

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
			
			if(data.hits != undefined && data.hits.length > 0) {
				
				response($.map(data.hits, function(item) {
					
					return {
						label : getLabel(item),
						value : item.ID,
						Name  : item.Name,
						Version  : item.Version,
						ID : item.ID,
						Statuses : item.Statuses
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

function getTemplate(prefix){
	return $("#" + prefix + "-template");
}

function addEntry(item, list, prefix){
	
	var exists = false;
	
	$("input[name='flowID']").each(function(){
		if($(this).val() == item.ID){
			exists = true;
		}
	});
	
	if(exists){
		return;
	}
	
	var clone = getTemplate(prefix).clone();
	
	clone.find("h3").text(item.label);
	clone.find("input").prop("disabled", false);
	clone.find("label[for='addFlowID-']").attr("for", "addFlowID-" + item.ID);
	clone.find("input[name='flowID']").val(item.ID);
	clone.find("input[name='addFlowID']").val(item.ID).attr("id", "addFlowID-" + item.ID);
	clone.find("input[name='flowName-']").val(item.Name).attr("name", "flowName-" + item.ID);
	clone.find("input[name='flowVersion-']").val(item.Version).attr("name", "flowVersion-" + item.ID);

	clone.find("select").each(function(j){
		 var select = $(this);
		 
		 select.prop("disabled", false);
		 select.attr("id", select.attr("id") + item.ID);
		 select.attr("name", select.attr("name") + item.ID);
		 
		 $.each(item.Statuses, function(index, status) {
			 select.append($('<option>', { value : status.ID }).text(status.Name));
		});
	});
	
	clone.removeAttr("id");
	list.append(clone);
	addFlowIDCheckboxes();
	clone.show();
}

function getLabel(item){
	
	return item.Name + " (v" + item.Version + ")";
}
