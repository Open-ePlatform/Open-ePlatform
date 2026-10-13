
function commentModal(event, button) {
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

function flowApprovalAssignOwner(event, button) {
	event.preventDefault();
	event.stopPropagation();
	
	var modal = $("#flow-approval-assign-owner-modal");
	var modalParent = modal.parent();
	
	button = $(button);
	var activityProgressID = button.data("activity-progress-id");
	var baseConnectorURL = modal.find("input[name='connectorURL']").val();
	
	var config = {
		otherClose: '.close',
		
		beforeOpen: function(){
			modal.find("h1 span").text(button.closest("tr").children("td").first().text());
		},

		afterContent: function(){
			var feather = this;
			feather.$content.removeClass("no-sections");
			
			modal.detach(); // Avoid ID clashes
			
			var form = feather.$content.find("form");
			form.prop("action", form.data("baseurl") + activityProgressID);
			
			$("#assign-user-search").removeData("ui-autocomplete");
			feather.$content.find("input[name='connectorURL']").val(baseConnectorURL + activityProgressID)
			
			feather.$content.find(".usergroup-list").each(function(){
				var list = $(this);
				initUserGroupList(list);
			 });
		},
		
		beforeClose: function() {
			var feather = this;
			
			modalParent.append(modal);
		}
	};
	
	$.featherlight(modal, config);
}