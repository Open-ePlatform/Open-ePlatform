$(function() {
 	$(".messageTemplates").on("change", function() {
			var select = $(this);
			var val = select.val();
			
			$("#comment").val(val).trigger("keyup");
		});
});