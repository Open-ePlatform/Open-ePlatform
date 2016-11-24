var AttachmentsModule;

$(document).ready(function() {
	
	if(window.location.hash == "#new-attachment") {
		scrollToAttachments("#attachments ul.attachments li");
	}
});

function initAttachments(){
	
	$("#attachments a.open_attachment").click(function(e) {
		e.preventDefault();
		
		$("#new-attachment").show();
		scrollToAttachments("#new-attachment");
		$("#attachment").focus();
	});

	$("#new-attachment a.close_attachment").click(function(e) {
		e.preventDefault();
		$("#new-attachment").hide();
	});
	
	if($("#attachments div.info-box.error").length > 0) {
		$("#attachments a.open_attachment").trigger("click");
	}
	
	$('#new-attachments-form').submit( function(e) {
		e.preventDefault();
		
		var container = $("#attachments-container");
		var sub = container.find("form");
		var button = container.find("a.submit");
		
		sub.addClass("pending");
		button.removeClass("btn-green").addClass("btn-light");
		
		$.ajax( {
			url: AttachmentsModule.URL,
			data: new FormData(this),
			type: 'POST',
			cache: false,
			processData: false,
			contentType: false
			
		}).done(function(result){
			  
			if(result.errors){
				
				container.find("div.validationerrors").html(result.errors);
				
			} else if(result.response){
				
				container.html(result.response);
				
				var content = container.children(".tabs-content");
				
				if(content.length > 0){
					
					container.append(content.children());
					content.remove();
				}
				
				$("#tabs ul.tabs li a[href='#attachments']").text(AttachmentsModule.TabTitle + " (" + $("#attachments ul li").length + ")");
			}
			
		}).fail(function(jqXHR){
			  
			alert("Kunde inte skicka data till servern: " + jqXHR.status + " " + jqXHR.statusText);
			    
		}).always(function(){
			
			sub.removeClass("pending");
			button.removeClass("btn-light").addClass("btn-green");
		});
	});
	
	$("#attachments-container input[type='file'].qloader").each(function(i){
		
		var $this = $(this);
		var limit = ($this.hasClass("qloader-limit-1") ? 1 : 0);
		
		$this.qloader({
			limit: limit,
			filerow_element: '<li class="finished"><div class="file"><span class="name"><img src="'+imagePath+'/file.png"/ class=\"vertical-align-middle marginright\"><span class="filename"></span></span><span class="italic"></span><a data-icon-after="t" href="javascript:void(0)" class="progress">' + deleteFile + '</a></div><div class="progressbar"><div style="width: 100%;" class="innerbar"></div></div></li>',
			use_element_index: false,
			filename_selector: 'span.filename',
			remove_selector: 'a.progress',
			file_container_element: $("#" + $this.attr("id") + "-qloader-filelist"),
			filelist_before: null,
			
		}).bind("beforeDelete.qloader", function(event,filerow){
			$("#"+$(filerow).attr("rel")).remove();
		});
	});
}

function scrollToAttachments(selector) {
	
	$('html, body').animate({
		scrollTop : ($(selector).last().offset().top - 43)
	}, 'fast');
}

