$(window).resize(function () {
	var pageWidth = $(window).width();
	adjustListSize(pageWidth);
});



$(document).ready(function(){
	 
	 $(".flowfamily-list").each(function(){
		 initFlowFamilyList($(this));
	 });
	 
	 adjustListSize($(window).width());
});

function adjustListSize(pageWidth)
{
	if (pageWidth<650)
	{
		$('#flowfamily-list').prop("style","width:"+(pageWidth-50)+"px");
	}
	else
	{
		$('#flowfamily-list').prop("style","width:600px");
	}
}

function initFlowFamilyList(list) {
	
	var prefix = getFlowFamilyPrefix(list);
	
	var searchInput = $( "#" + prefix + "-search");
	
	if (searchInput.data('ui-autocomplete') != undefined) {
		return;
	}
	
	 
	searchInput.autocomplete({
		source: function(request, response) {
			return searchFlowFamilies(request, response, list.find("input[name='connectorURL']").val(), searchInput);
		},
		
		select: function( event, ui ) {
			addFlowFamilyEntry(ui.item, list, prefix);
			$(this).val("");
			
			return false;
		},
		
		focus: function(event, ui) {
			event.preventDefault();
		}
	});
	
	searchInput.keypress(function(event){
		if (event.which == 13) { // Enter
			event.preventDefault();
		}
	});
	
	var entries = list.find("li");
	
	entries.each(function() {
		var entry = $(this);
		initFlowFamilyDeleteButton(entry, list);
	});
	
	list.on("change", function() {
		list.find("li").removeClass("lightbackground");
		list.find("li:odd").addClass("lightbackground");
	});
	
	list.trigger("change");
}

function getFlowFamilyPrefix($list) {
	return $list.find("input[name='prefix']").val();
}

function searchFlowFamilies(request, response, searchURL, searchInput) {
	
	searchInput.addClass("ui-autocomplete-loading");
	
	$.ajax({
		url : searchURL,
		cache: false,
		dataType : "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			q : encodeURIComponent(request.term)
		},
		success : function(data) {
			
			if (data.hits != undefined && data.hits.length > 0) {
				
				response($.map(data.hits, function(item) {
					console.log(item);
					return {
						label : item.Name,
						value : item.FlowFamilyID,
						Enabled : item.Enabled,
						Name  : item.Name,
						ID : item.FlowFamilyID
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

function getFlowFamilyTemplate(prefix) {
	return $("#" + prefix + "-template");
}

function addFlowFamilyEntry(item, list, prefix) {
	
	if (list.find("#" + prefix + "_" + item.value).length > 0) {
		return;
	}
	
	var clone = getFlowFamilyTemplate(prefix).clone();
	
	var label = item.Name;
	
	clone.find("span.text").text(label);
	clone.find("input[name='" + prefix +"ID-template']").val(item.ID);
	
	clone.find("input").prop("disabled", false);
	
	clone.attr("id", prefix + "_" + item.value);
	clone.attr("class", prefix + "-list-entry");
	clone.find("input[name='" + prefix +"ID-template']").attr("name", prefix+"ID");
	
	var deleteButton = initFlowFamilyDeleteButton(clone, list);
	deleteButton.attr("title", deleteButton.attr("title") + " " + label);
	
	list.find("li:last").before(clone);
	clone.show();
	
	list.trigger("change");
	
	return clone;
}

function initFlowFamilyDeleteButton(entry, list) {
	
	var deleteButton = entry.find("a.delete");
	
	deleteButton.click(function(e) {
		e.preventDefault();
		
		entry.remove();
		
		list.trigger("change");
	});
	
	return deleteButton;
}
