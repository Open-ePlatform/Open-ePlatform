$(document).ready(function() {

	var dropdown = $("#shareflow-repositoryindex");
	
	if(dropdown.length > 0){
		
		dropdown.change(function() {
			showHideRepositoryUploadDescription(dropdown);
		});

		showHideRepositoryUploadDescription(dropdown);
	}
});

function showHideRepositoryUploadDescription(dropdown) {

	var descriptions = $("#descriptions > div");

	descriptions.each(function() {
		$(this).hide();
	});

	var selected = dropdown.val();

	if (selected != "") {

		descriptions.filter("[data-repoindex='" + selected + "']").show();
	}
}

function showRepositoryDescription(repositoryID, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var modal = $("#repository-modal-template").children();
	var repository = $("#repository-" + repositoryID);
	
	var config = {
		otherClose: '.close',
		
		beforeOpen: function() {
			modal.find(".modal-header").find("h1, h2").text(repository.find(".name").text());
			modal.find(".modal-body").find(".description").text(repository.find(".description").text());
		},
	
		afterContent: function(){
			var feather = this;
			feather.$content.removeClass("no-sections");
		}
	};
	
	$.featherlight(modal, config);
	
}