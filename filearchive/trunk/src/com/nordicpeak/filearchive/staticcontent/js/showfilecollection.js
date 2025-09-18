$(function() {
	var $link = $("#link");
	
	$("#copylink").on("click", function() {
		$link.select();
		
		document.execCommand("copy");
		
		alert($(this).data("copied"));
		
		setTimeout(function() {
			document.getSelection().removeAllRanges();
		}, 500);
	});
	
	$("#upload-file-form").on("submit", function(e) {
		e.preventDefault();
		
		$("#too-big-file").hide();
		
		var fileInput = document.getElementById("add-files");
		
		if (!fileInput.files || fileInput.files[0]) {
			var fileSize = fileInput.files[0].size;
			var $button = $("#upload-file");
			
			if ($button.data("maxsize") < fileSize) {
				$("#too-big-file").show();
			}
			else {
				var formData = new FormData(this);
				
				$button.prop("disabled", true);
				$button.find("img").show();
				$button.find("span").html($button.data("uploading"));
				
				var $progress = $("#upload-progress");
				
				$progress.show();
				$progress.html("0%");
				
				$.ajax({
					xhr: function() {
						var xhr = new window.XMLHttpRequest();
						
						xhr.upload.addEventListener("progress", function(evt) {
							if (evt.lengthComputable) {
								var percentComplete = evt.loaded / evt.total;
								percentComplete = parseInt(percentComplete * 100);
								
								console.log(percentComplete);
								
								$progress.html(percentComplete + "%");
								$progress.prop("value", percentComplete);
							}
						}, false);
						
						return xhr;
					},
					url: $button.data("url"),
					type: "POST",
					data: formData,
					dataType: "json",
					cache: false,
					contentType: false,
					processData: false,
					success: function() {
						location.reload();
					}
				});
			}
		}
	});
});