var Alternativesi18n = {
	deleteConfirm: "Are you sure you want to remove the alternative"
};

$(document).ready(function() {
	
	$(".sortable").sortable({ cursor: 'move', update: function(event, ui) {
		
		updateSortOrder($(this));
		
	}}).children().each(function(i) {
		
		var item = $(this);
		var itemSortOrder = item.find('input[type="hidden"].sortorder').val();
		
		if (i != itemSortOrder) {
			
			item.parent().children().each(function(j) {
				
				if (itemSortOrder == j) {
					
					var otherItem = $(this);
					
					if (item[0] !== otherItem[0]) {
						
						otherItem.before(item.detach());
					}
					
					return;
				}
			});
		}
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
	
});

function updateSortOrder(alternatives) {
	
	alternatives.children().each(function(i) {
		var alternative = $(this);
		alternative.find("input.sortorder").val(i);
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
}

function deleteAlternative(button, event) {
	event.preventDefault();
	event.stopPropagation();
	
	if(confirm(Alternativesi18n.deleteConfirm + "?")) {
		
		let alternatives = $(button).closest(".alternatives");
		$(button).closest(".alternative").remove();
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
	
	var modal = $(button).closest(".alternatives-container").find(".alternatives-modal");
	var alternative = $(button).closest(".alternative");
	
	var config = {
			otherClose: '.close',
			
			beforeOpen: function(){
				modal.find(".modal-header").find("h1, h2").text(Alternativesi18n.title + " " + alternative.find("input.name").val());
				modal.find("input[name = 'alternative-value']").val(alternative.find("input.hiddenValue").val());
			},

			afterContent: function(){
				var feather = this;
				feather.$content.removeClass("no-sections");
			},
			
			beforeClose: function(){
				var feather = this;
				alternative.find("input.hiddenValue").val(feather.$content.find("input[name = 'alternative-value']").val());
			},
	};
	
	$.featherlight(modal, config);
}

