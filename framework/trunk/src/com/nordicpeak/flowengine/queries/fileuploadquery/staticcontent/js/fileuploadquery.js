$(document).ready(function() {
	
	setQueryRequiredFunctions["FileUploadQueryInstance"] = makeFileUploatQueryRequired;
	
	var ua = window.navigator.userAgent;
	var isMSBrowser = ua.indexOf('MSIE ') > -1 || ua.indexOf('Trident/') > -1 || ua.indexOf('Edge/') > -1;
	
	if (isMSBrowser) {

		$('.upload')
			.addClass('no-hover')
			.on('drop dragenter dragover', function(e){ 
			
				e.preventDefault();
		});
		
	} else {
		
		$('.upload').on('drop', function(e){
			
			e.preventDefault();
			$(this).removeClass('active');
			
			var $input = $(this).find('input:last');
			var droppedFiles = e.originalEvent.dataTransfer.files;
			
			$input.prop('files', droppedFiles);

			// Some browsers triggers 'change' on property change, if not we trigger it manually
			if ($input.is(':last-child')) {
				
				$input.trigger('change');
			}
			
		}).on('dragenter dragover', function(e){
			
			e.preventDefault();
			$(this).addClass('active');
			
		}).on('dragleave', function(e){
			
			$(this).removeClass('active');
		});
	}
	
});

function makeFileUploatQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
}

function initFileUploadQuery(queryID) {
	
	initFileUploader($("#query_" + queryID + "_fileuploader"));
}