var fileuploader = {
	imagePath: "",
	deleteFile: "Delete"
};

function initFileUploader($fileuploader) {
	
	$fileuploader.each(function(i){
		
		var $this = $(this);
		
		$this.fileuploader({
			allowDuplicates: true,
			filerowElementTemplate: '<li class="finished"><div class="file"><span class="name"><img src="' + fileuploader.imagePath + '/file.png"/ class=\"vertical-align-middle marginright\"><span class="filename"></span></span><span class="italic"></span><a data-icon-after="t" href="javascript:void(0)" class="progress">' + fileuploader.deleteFile + '</a></div><div class="progressbar"><div style="width: 100%;" class="innerbar"></div></div></li>',
			containerElementSelector: $this.parent().parent().parent().find("ul.files"),
			filenameElementSelector: 'span.name',
			removeElementSelector: 'a.progress',
			filelistPlacement: null,
			
		}).bind("fileuploader.beforeDelete", function(event,filerow){
			$("#"+$(filerow).attr("rel")).remove();
		});
	});
	
}

function removeFile(e, id, deleteMessage, element) {
	
	if(e.preventDefault) {
		e.preventDefault();
	} else {
		e.returnValue = false;
	}
	
	if(confirm(deleteMessage)) {
		
		var row = $(element).closest("li");
		row.find('span.name').trigger("fileuploader.beforeDeleteInitialFile");
		row.replaceWith("<input type=\"hidden\" name=\"" + id + "\" />");
	}
}