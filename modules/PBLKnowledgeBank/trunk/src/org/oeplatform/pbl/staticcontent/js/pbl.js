var pblProxyModuleURI;
var pblLanguage = {
	"GETTING_WORD_DESCRIPTION": "Getting description",
	"NO_WORD_DESCRIPTION_FOUND": "No word description found for"
};

$(function() {
	
	if(pblProxyModuleURI) {
	
		initPBLLinks($("body"));
	
	}
	
	if(typeof flowEngineDOMListeners != 'undefined') {
		
		flowEngineDOMListeners.push(initPBLLinks);
		
	}
	
});

function initPBLLinks($content) {
	
	var regex = "(\$boverket)[{](.*?)}";
	
	$content.find("a[href^='$boverket{'][href$='}'], a[href^='http://$boverket{'][href$='}']").each(function() {
		
		var href = $(this).attr("href");
		
		var startIdx = href.indexOf("http://") == 0 ? 17 : 10; 
		
		var word = href.substring(startIdx, href.length-1);
		
		if(word != "") {
			
			var $this = $(this);
			
			$this.attr("href", "#");
			$this.wrap("<span class='pbl-word' />");
			$this.hover(function(e) {
				e.preventDefault();
				bindPBLWordEvent($this, word);
			},
			function(e) {
				$this.parent().find(".pbl-help-box").hide();
			});
			$this.on("click", function(e) {
				e.preventDefault();
				bindPBLWordEvent($this, word);
			});
							
		}
		
	});
	
}

function bindPBLWordEvent($this, word) {
	
	if(!$this.hasClass("loaded")) {
		
		var $dialog = $("<div class='pbl-help-box' style='display: block;'><span>" + 
				"<span class='loading-icon'>" + pblLanguage.GETTING_WORD_DESCRIPTION + "</span></span><div class='marker'></div></div>");
		
		$dialog.css("left", ($this.width()/2) - 165);
		
		$this.parent().append($dialog);
		
		$.ajax({
			cache: false,
			url: pblProxyModuleURI,
			data: { 
				word: $.trim(word)			
			},
			dataType: "json",
			error: function (xhr, ajaxOptions, thrownError) { $dialog.find(".loading-icon").replaceWith("<p>" + pblLanguage.NO_WORD_DESCRIPTION_FOUND + "!</p>"); },
			success: function(response) {

				var words = eval(response);
				
				var loaded = false;
				
				$.each(words, function(i, word) {
					
					if(word.Description) {
						
						$dialog.find(".loading-icon").replaceWith("<p>" + word.Description + "</p>");
						
						loaded = true;
						
					}
														
				});
				
				if(!loaded) {
					
					$dialog.find(".loading-icon").replaceWith("<p>" + pblLanguage.NO_WORD_DESCRIPTION_FOUND + "!</p>");
					
				}
				
				$this.addClass("loaded");
				
			}
			
		});
	
	} else {
		
		$this.next(".pbl-help-box").show();			
		
	}
	
}
