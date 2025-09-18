var imagePath;

$(function() {
	var $qloader = $("#qloader");
	
	var template = '<div class="d-flex align-items-center mt-2">';
	template += '<img src="' + $qloader.data("imagepath") + '/floppy.png" class="mr-1">';
	template += '<span/>';
	template += '<img class="delete-file-trigger ml-auto" src="' + $qloader.data("imagepath") + '/delete.png"/></div>';
		
	$qloader.each(function(i){
		var $this = $(this);
		var limit = ($this.hasClass("qloader-limit-1") ? 1 : 0);
		$this.qloader({
			limit: limit,
			filerow_element: template,
			filename_element_index: 1,
			remove_element_index: 2
		}).bind("beforeDelete.qloader", function(event,filerow){
			$("#"+$(filerow).attr("rel")).remove();
		});
	});
});