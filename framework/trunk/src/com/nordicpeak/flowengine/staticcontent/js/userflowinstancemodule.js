$(document).ready(function() {
	
	$("table.coloredtable").each(function() {
		var $this = $(this);
		$this.find("tr:not(:first):visible:even").removeClass("odd").addClass("even");
		$this.find("tr:not(:first):visible:odd").removeClass("even").addClass("odd");
	});
	
	$("#tabs").tabs();

	$("#messages a.open_message").click(function(e) {
		e.preventDefault();
		
		$("#new-message").show();
		scrollToMessages("#new-message");
		$("#message").focus();
	});

	$("#new-message a.close_message").click(function(e) {
		e.preventDefault();
		$("#new-message").hide();
	});
	
	if($("#messages div.info-box.error").length > 0) {
		$("#messages a.open_message").trigger("click");
	}

	if(window.location.hash == "#new-message") {
		scrollToMessages("#messages ul.messages li");
	}
	
});

function scrollToMessages(selector) {
	
	$('html, body').animate({
		scrollTop : ($(selector).last().offset().top - 43)
	}, 'fast');
	
}