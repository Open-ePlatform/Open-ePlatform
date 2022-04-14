$(function() {
	
	$('.sortMessages').on("click", function(){
		
		var $list = $(this).siblings('ul');
		var $icon = $(this).find('i');
		
		$list.toggleClass('reversed').append($list.children('li').get().reverse());
		$icon.toggleClass('flipped');
		
		var isReversed = $list.hasClass('reversed');
		
		if (isReversed) {
			
			Cookies.set('reversedMessages.' + this.dataset.type, 'true', { expires: 365, path: '' });
			
		} else {
			
			Cookies.remove('reversedMessages.' + this.dataset.type, { path: '' });
		}
	});
	
	if (Cookies.get('reversedMessages.external') === 'true') $('.sortMessages[data-type="external"]').trigger("click");
	if (Cookies.get('reversedMessages.internal') === 'true') $('.sortMessages[data-type="internal"]').trigger("click");

	if ($("#tabs").hasClass('ui-tabs')) { //Is tabs plugin initialized
		
		initMessageTabs();
		
	} else {
		
		$("#tabs").on('tabscreate', function(){
			
			initMessageTabs();
		});
	}
	
	function initMessageTabs() {
		
		initMessageTab("#messages", "#new-message");
		initMessageTab("#notes", "#new-note");
		
		initReadReceiptModal();
	}
	
});

function initMessageTab(tabID, messagePanelID) {
	
	var $messagePanel = $(messagePanelID);
	
	if (!$(tabID).length || !$messagePanel.length) return;
	
	$(tabID + " a.reply_message").on("click", function(e) {
		e.preventDefault();
		
		var quotedMessage = $(this).closest("li").find(".message");
		
		var quote = quotedMessage.clone();
		quote.find(".files, .quote").remove();
		
		var quoteDiv = $("#message-quote");
		quoteDiv.find(".quote").html(quote.contents());
		quoteDiv.find("input").val(quotedMessage.data("messageid")).prop("disabled", false);
		quoteDiv.show();
		
		$messagePanel.show();
		scrollToMessages(messagePanelID);
		$messagePanel.find("#message").trigger("focus");
	});
	
	$(tabID + " a.stop_quote").on("click", function(e) {
		e.preventDefault();
		
		var quoteDiv = $("#message-quote");
		quoteDiv.hide();
		quoteDiv.find("input").prop("disabled", true);
	});
	
	$(tabID + " a.open_message").on("click", function(e) {
		e.preventDefault();
		
		$messagePanel.show();
		scrollToMessages(messagePanelID);
		$messagePanel.find("#message").trigger("focus");
		
		$(this).hide();
	});

	$messagePanel.find("a.close_message").on("click", function(e) {
		e.preventDefault();
		$(messagePanelID).hide();
		
		$(tabID + " a.open_message").show();
	});

	if ($(tabID + " div.info-box.error").length > 0) {
		
		var $tabs = $("#tabs");
		var idx = $tabs.find(tabID).index();
		$tabs.tabs("option", "active", idx);
		$(tabID + " a.open_message").trigger("click");
		
	} else {
		
		setActiveTab(tabID);
	}
	
	$(window).on('hashchange', function () {
		
		setActiveTab(tabID);
	});
}

function setActiveTab(tabID) {
	
	var hash = window.location.hash;
	
	if(hash) {
		
		var hashArr = hash.split("-");

		if(hashArr[0] == tabID) {
		
			var $tabs = $("#tabs");
			var idx = $tabs.find("li[data-tabid = '" + tabID + "']").index();
			$tabs.tabs("option", "active", idx);
	
			if(hashArr[1]) {
	
				scrollToMessages(hash);
			
			} else {
			
				scrollToMessages(tabID + " ul.messages li");
			}
			
			window.location.hash = "";
		}
	}

}

function scrollToMessages(selector) {
	
	var $element = $(selector);
	
	if ($element.length) {
		
		if ($element.data('messageId')) {
			
			var latestMessageID = 0;
			
			$element.each(function(){
				
				if (parseInt(this.dataset.messageId) > parseInt(latestMessageID)) {
					
					latestMessageID = this.dataset.messageId
				}
			});
			
			var $latestMessage = $element.filter('li[data-message-id="' + latestMessageID + '"]')
			
			if ($latestMessage.length) {
				
				$('html, body').animate({
					scrollTop : ($latestMessage.offset().top - 43)
				}, 'fast');
				
				return;
			}
		}
		
		$('html, body').animate({
			scrollTop : ($element.last().offset().top - 43)
		}, 'fast');
	}
}

function initReadReceiptModal() {
	
	$(".read-receipts").click(function() {
		
		var $wrapper = $(this).closest(".read-receipts-wrapper");
		
		var $modal = $wrapper.find(".read-receipts-modal");

		$.ajax({
			url : $modal.data("url"),
			dataType : "json",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			success : function(data) {
				
				$.featherlight($modal, {
		
					afterContent: function() {
		
						var $modalContent = this.$content;
						
						$modalContent.closest(".featherlight-content").addClass("contentitem");
						
						var $table = $modalContent.find("table");
						
						data.forEach(function(readReceipt) {
							
							var $readReceipt = $wrapper.find(".read-receipt-template").clone();
							
							$readReceipt.find(".user").text(readReceipt.user);
							$readReceipt.find(".read").text(readReceipt.read);
							
							$table.append($readReceipt);
							
							readReceipt.attachmentDownloads.forEach(function(attachmentDownload) {
								
								var $attachmentDownload = $wrapper.find(".attachment-download-template").clone();
								
								$attachmentDownload.find(".user").text(readReceipt.user);
								$attachmentDownload.find(".downloaded").text(attachmentDownload.downloaded);
								$attachmentDownload.find(".attachment-filename").text(attachmentDownload.attachmentFilename);
								
								$table.append($attachmentDownload);
							});
						});
					}
				});
				
			},
			error : function(res) {
				
				console.log(res);
			}
		});
	});
}