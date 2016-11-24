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
