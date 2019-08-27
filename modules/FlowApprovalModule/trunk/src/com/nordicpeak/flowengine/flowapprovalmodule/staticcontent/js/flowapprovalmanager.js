
function commentModal(event, button){
	event.preventDefault();
	event.stopPropagation();

	var modal = $("#flow-approval-comment-modal");
	
	var comment = $(button).find("span").html();
	
	var config = {
		otherClose: '.close',
		
		beforeOpen: function(){
			modal.find("p").html(comment);
		},

		afterContent: function(){
			var feather = this;
			feather.$content.removeClass("no-sections");
		},
	};
	
	$.featherlight(modal, config);
}