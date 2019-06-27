var Alternativesi18n = {
	deleteConfirm: "Are you sure you want to remove the alternative"
};

$(document).ready(function() {
	
	var alternativesContainers = $(".sortable").sortable({ cursor: 'move', update: function(event, ui) {
		
		updateSortOrder($(this));
		
	}}).each(function(){
		
		var container = $(this);
		
		sortAlternativesAfterSortOrder(container);
		
		container.each(function(){
			var row = $(this);
			attachRowChangeSortingListener(row);
		});
	});
	
	$("#useFreeTextAlternative").change(function() {
		var $this = $(this);
		
		if ($this.is(":checked")) {
			
			$("#freeTextAlternative").removeAttr("disabled");
			
		} else {
			$("#freeTextAlternative").attr("disabled", "disabled");
		}
	});
	
	$("#useFreeTextAlternative").trigger("change");
	
	$(".alternatives-container .sorting").change(function() {
		
		sortAlternativesAfterSortingSelect($(this));
		
	}).each(function(){
		
		sortAlternativesAfterSortingSelect($(this));
	});
	
});

function updateSortOrder(alternativesContainer) {
	
	alternativesContainer.children().each(function(i) {
		var alternative = $(this);
		alternative.find("input.sortorder").val(i);
	});
}

function sortAlternativesAfterSortOrder(alternativesContainer){
	
	alternativesContainer.children().sort(function(a, b){
		
		return parseInt($(a).find('input[type="hidden"].sortorder').val()) - parseInt($(b).find('input[type="hidden"].sortorder').val());
		
	}).appendTo(alternativesContainer);
}

function sortAlternativesAfterSortingSelect(select){
	
	var sortOrder = select.val();
	
	if (sortOrder != "") {
	
		var alternativesContainer = select.closest(".alternatives-container").find(".alternatives");
		var alternatives = alternativesContainer.children();
		
		if(sortOrder == "d"){ // A-Z
		
			alternativesContainer.children().sort(function(a, b){
				
				var valA = $(a).find('input[type="text"]').val().toLowerCase();
				var valB = $(b).find('input[type="text"]').val().toLowerCase();
				
				// Always place empty string at the bottom
				if (valA == "" && valB == "") {
				
					return 0;
					
				} else if (valA == "") {
					
					return 1;
					
				} else if (valB == "") {
					
					return -1;
				}
				
				return valA.localeCompare(valB);
				
			}).appendTo(alternativesContainer);
			
		} else { // Z-A
			
			alternativesContainer.children().sort(function(a, b){
				
				var valA = $(a).find('input[type="text"]').val().toLowerCase();
				var valB = $(b).find('input[type="text"]').val().toLowerCase();
				
				return -valA.localeCompare(valB);
				
			}).appendTo(alternativesContainer);
		}
		

		updateSortOrder(alternativesContainer);
	}
}

function attachRowChangeSortingListener(row){
	
	row.find("input[type='text']").on("blur", function(){
		
		sortAlternativesAfterSortingSelect(row.closest(".alternatives-container").find(".sorting"));
	});
}

function addAlternative(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var uuid = generateUUID();
	var container = $(button).closest(".alternatives-container");
	var alternatives = container.find(".alternatives");
	var template = container.find(".alternative-template").children().first().clone();
	
	template.find("input").prop("disabled", false).each(function() {
		
		var input = $(this);
		
		input.attr("id", input.attr("id") + uuid);
		
		if (input.attr("name") == "alternativeID") {
			
			input.val(uuid);
			
		} else {
			
			input.attr("name", input.attr("name") + uuid);
		}
	});
	
	template.find("input.sortorder").val(alternatives.children().length);
	
	alternatives.append(template);
	
	attachRowChangeSortingListener(template);
}

function deleteAlternative(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	if(confirm(Alternativesi18n.deleteConfirm + "?")) {
		
		var $button = $(button);
		
		var alternatives = $button.closest(".alternatives");
		$button.closest(".alternative").remove();
		updateSortOrder(alternatives);
	}
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

function openAlternativeModal(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var alternativesContainer = $(button).closest(".alternatives-container");
	var modal = alternativesContainer.find(".alternatives-modal");
	var modalParent = modal.parent();
	var alternative = $(button).closest(".alternative");
	
	var config = {
			otherClose: '.close',
			
			beforeOpen: function(){
				modal.find(".modal-header").find("h1, h2").text(Alternativesi18n.title + " " + alternative.find("input.name").val());
				modal.find("input, select").each(function(){
					
					var input = $(this);
					var savedInput = alternative.find("input[name^='" + input.prop("name") + "_']");
					
					if (savedInput.length > 0) {
						input.val(savedInput.val());
					}
				});
			},

			afterContent: function(){
				var feather = this;
				feather.$content.removeClass("no-sections");
				
				modal.detach();
			},
			
			beforeClose: function(){
				var feather = this;
				
				feather.$content.find("input, select").each(function(){
					
					var input = $(this);
					alternative.find("input[name^='" + input.prop("name") + "_']").val(input.val());
				});
				
				modalParent.append(modal);
			},
	};
	
	$.featherlight(modal, config);
}
