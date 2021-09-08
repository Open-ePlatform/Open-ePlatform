$(function() {
	
	//LIST
	
	$("input[name='textTagID']").on("change", function() {
		
		var checked = $("input[name='textTagID']:checked").length > 0;
		
		if(checked){
			
			$(".share-button").removeClass("disabled");
			
		}else{
		
			$(".share-button").addClass("disabled");
		}
		
	});
	
	$("input[name='target']").on("change", function() {
		
		var checked = $("input[name='target']:checked").length > 0;
		
		if(checked){
			
			$("#share-submit-button").removeAttr("disabled");
			
		}else{
		
			$("#share-submit-button").attr("disabled","true");
		}
		
	});
	
	
	// ADD or UPDATE
	
	$("#type").on("change", function(e) {
		
		var $this = $(this);
		
		if($this.val() == "EDITOR") {
			
			var $input = $("#textfield-wrapper").addClass("hidden").find("input[type='text']");
			$input.attr("disabled", "disabled");
			
			var $textarea = $("#editor-wrapper").removeClass("hidden").find("textarea");
			var id = $textarea.attr("id");

			var currentValue = $input.val();
			
			$textarea.removeAttr("disabled");
			
			setCKEditorReadOnly(id, false);
			
			setCKEditorValue(id, currentValue);
			
		} else if($this.val() == "TEXTFIELD") {
			
			var $textarea = $("#editor-wrapper").addClass("hidden").find("textarea");
			var id = $textarea.attr("id");
			
			$textarea.attr("disabled", "disabled");
			
			var $input = $("#textfield-wrapper").removeClass("hidden").find("input[type='text']")
			
			var currentValue = getCKEditorValue(id);
			
			$input.val(currentValue.replace(/(<([^>]+)>)/ig, ""));
			$input.removeAttr("disabled", "disabled");
			
			setCKEditorReadOnly(id, true);
			
		}
		
	});
	
	CKEDITOR.on('instanceReady', function( evt ) {
		$("#type").trigger("change");
	});
	
	$(".modal .close").on("click", function(e) {
		e.preventDefault();
		$(this).parent().fadeOut("fast", function() {
			$(this).remove();
		});
	});
	
	$("#tag-filter-input").keyup(function() {
		
		$.cookie("oep.textTagSearch", $(this).val());
	});

	var tagSearch = $.cookie("oep.textTagSearch");
	
	if (tagSearch) {
		var $input = $("#tag-filter-input").val(tagSearch).keyup();
	}
	
});

function checkState(event){
	
	if($(".share-button").hasClass("disabled")){
		
		event.stopPropagation();
		
		event.preventDefault();
	}
}


function setCKEditorReadOnly(id, readOnly) {
	
	var instance = CKEDITOR.instances[id];
	
	if(!(typeof instance === "undefined")){
		
		instance.setReadOnly(readOnly);
	}
	
}

function updateCKEditorValue(id) {

	var instance = CKEDITOR.instances[id];
	
	if(!(typeof instance === "undefined")){
		instance.updateElement();
	}
	
}

function getCKEditorValue(id) {
	
	var instance = CKEDITOR.instances[id];
	
	if(!(typeof instance === "undefined")){
		
		return instance.getData();
	}
	
	return "";
	
}

function setCKEditorValue(id, value) {
	
	var instance = CKEDITOR.instances[id];
	
	if(!(typeof instance === "undefined")){
		
		instance.setData(value);
	}
	
}