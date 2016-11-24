$(document).ready(function() {
	
	$(".sortable").sortable({ cursor: 'move', update: function(event, ui) {
		updateSortOrder($(this));
	}}).children().each(function(i) {
		var item = $(this);
		var itemSortOrder = item.find('input[type="hidden"].sortorder').val();
		if(i != itemSortOrder) {
			item.parent().children().each(function(i) {
				if(itemSortOrder == i) {
					var $this = $(this);
					if(item.attr("id") != $this.attr("id")) {
						$this.before(item.detach());
					}
					return;
				}
			});
		}
	});
	
	$("#useFreeTextAlternative").change(function() {
		var $this = $(this);
		if($this.is(":checked")) {
			$("#freeTextAlternative").removeAttr("disabled");
		} else {
			$("#freeTextAlternative").attr("disabled", "disabled");
		}
	});
	
	$("#useFreeTextAlternative").trigger("change");
	
});

function addAlternative(alternativesSelector, imagePath, size, moveTitle, deleteTitle) {
	var uuid = generateUUID();
	var html = '<div id="alternativeHTML_' + uuid + '" class="full floatleft margintop marginbottom">'; 
	html +=	'<img class="vertical-align-middle marginright cursor-move" src="' + imagePath + '/move.png" title="' + moveTitle + '"/>';
	html += '<input type="hidden" id="alternativeID_' + uuid + '" name="alternativeID" value="' + uuid + '"/>';
	html += '<input type="hidden" id="sortorder_' + uuid + '" name="sortorder_' + uuid + '" value="' + size + '" class="sortorder"/>';
	html += '<input type="text" id="alternative_' + uuid + '" name="alternative_' + uuid + '" style="width: 89%;">';
	html += '<a title="' + deleteTitle + '" href="javascript:void(0);" onclick="deleteAlternative(\'' + alternativesSelector + '\', \'' + uuid + '\',\'' + deleteTitle + '\')"><img src="' + imagePath + '/delete.png" class="vertical-align-middle marginleft"></a>';
	html += '</div>';
	$(alternativesSelector).append(html);
}

function deleteAlternative(alternativesSelector, id, deleteTitle) {
	if(confirm(deleteTitle + "?")) {
		$("#alternativeHTML_"+id).remove();
		updateSortOrder($(alternativesSelector));
	}
}

function updateSortOrder(obj) {
	obj.children().each(function(i) {
		$("#sortorder_"+ $(this).attr("id").split("_")[1]).val(i);
	});
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