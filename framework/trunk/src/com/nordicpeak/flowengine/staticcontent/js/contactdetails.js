$(document).ready(function() {
	
	$( "#attribute-mobilePhone").keyup(function() {
		
		if($(this).val() != "") {
			$("#attribute-contactBySMS").removeAttr("disabled").next("label").removeClass("disabled");
		} else {
			$("#attribute-contactBySMS").removeAttr("checked").attr("disabled", "disabled").next("label").addClass("disabled");
		}
		
	});
	
	$("#attribute-mobilePhone").trigger("keyup");
	
	if($(".user-updated-message").length > 0) {
		
		showNotificationDialog("success", 5000, $(".user-updated-message").text());
		
	}
	
});

function showNotificationDialog(type, delay, msg) {
	
	var modal = $("<div>").addClass("mini-modal " + type);
	var inner = $("<div>").addClass("inner");
	var span = $("<span>").text(msg);

	inner.append(span);
	modal.append(inner);
	modal.prependTo("body");

	modal.fadeIn("fast").delay(delay).fadeOut("fast", function() {
		modal.remove();
	});
	
}