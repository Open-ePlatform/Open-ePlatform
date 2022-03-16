var actionURL;
var currentAjaxOptions;
var currentAjaxRequest;
var setQueryRequiredFunctions = {};
var setQueryValidationErrorFunctions = {};
var initQueryFunctions = {};
var hideQueryFunctions = new Array();
var flowEngineDOMListeners = new Array();

$(function() {
	
	$('#paymentForm').on('submit',function(){
		var submitForm = $('#paymentForm');
		
		var submitButton = submitForm.find( "a.next" );
		
		submitButton.removeClass('btn-green');
		submitButton.addClass('disabled');
		submitButton.attr('onclick','return false;');
	});	
	
	var firstValidationError = $(".validationerrors").first();
	var queryAnchorName = firstValidationError.attr("id");
	
	if (queryAnchorName && !firstValidationError.closest("section").hasClass("testing")) {
		
		window.location.hash = queryAnchorName.split("-")[0];
	}
	
	actionURL = $("#FlowBrowser form").attr("action");
	
	$(".query.mergewithpreviousquery").each(function(){
		
		$(this).prevAll(".query:first").addClass("mergedquery");
	});

	$(".query.mergewithpreviousquery.notitle").each(function(){
		
		$(this).prevAll(".query:first").addClass("beforenotitle");
	});
	
	
	$(".modal .close").on("click", function(e) {
		e.preventDefault();
		$(this).parent().fadeOut("fast", function() {
			$(this).remove();
		});
	});
	
	$(".info-box .close").on("click", function(e) {
		e.preventDefault();
		var $wrapper = $(this).parent().parent();
		$wrapper.next().fadeOut("fast", function() {
			$wrapper.remove();
		});
	});
	
	$("i[title]").tooltip({
		position: {
			my: "right+29 bottom-22",
			collision: "flipfit"
		},
		track: false,
		content: function () {
          return $(this).prop('title') + "<span class=\"marker\"></span>";
      	},
      	show: {
      		effect: 'none'
      	},
      	hide: {
      		effect: 'none'
      	}
	});
	
	$("section .section-full").find("article:first").addClass("first");
	
    $(window).on('resize', function() {
    	helpBoxHeight = $('body').height();
        $('div[data-help-box]').find('> div > div').attr('style', 'max-height: ' + (helpBoxHeight - 80) + 'px !important;');
    });
	
	//initFlowInstanceControlPanel();
	
	var $activeStep = $("section.service .service-navigator.primary li.active");
    
	$activeStep.prev().addClass("latest completed");
    
    var $futureSteps = $activeStep.nextAll();
    $futureSteps.clone().appendTo($("#futureNavigator"));
    $futureSteps.addClass("future");
    
    $(document).on('click', '[data-toggle-menu]', function(e) {
        e.stopPropagation();
        e.preventDefault();
        var menu = $(this).data('toggle-menu');

        $('[data-menu="' + menu + '"]').toggleClass('menu-active');
        
    }).on('click', '[data-menu] li a', function(e) {
        e.stopPropagation();
    }).on('click', function(e) {
        $('[data-menu]').removeClass('menu-active');
    });
    
    var $startPanel = $(".start-flow-panel");
    
    if ($startPanel.length > 0) {
    	
	    $(window).on("scroll", checkStartFlowPanelPosition);
	    $(window).on("resize", checkStartFlowPanelPosition);
	    
    }
	
    $("#show-receipt-btn").on("click", function(e) {
        e.preventDefault();
        $(this).closest("section.service").find(".queries").show();
        $(this).hide();
        $("#hide-receipt-btn").show(); 
    });
    
    $("#hide-receipt-btn").on("click", function(e) {
        e.preventDefault();
        $(this).closest("section.service").find(".queries").hide();
        $(this).hide();
        $("#show-receipt-btn").show();
    });
});

function checkStartFlowPanelPosition(event) {
	
	var win = $(this);
	var $startPanel = $(".start-flow-panel");
	
	var headerHeight = ($("header").height() + 40);
	var bottomSteps = $(".service-navigator-wrap");
	
	if (bottomSteps.length === 0) {
		bottomSteps = $(".about-flow");
	}
	
	if (!$("#flowforms-list-button").hasClass("open") && $(window).width() > 1004 && $(this).scrollTop() >= headerHeight) {
		
		if (win.scrollTop() + $startPanel.height() + parseFloat($($startPanel).css('padding-top')) >= bottomSteps.offset().top) {
			
			$startPanel.removeClass("fixed");
			$startPanel.addClass("absolute");
			$startPanel.css("right", "0px");
			$startPanel.css("padding-bottom", (25 + bottomSteps.height()) + "px");
			
		} else {
			
			var rightMargin = ($(window).width() - $startPanel.closest("section").width()) / 2;
			$startPanel.css("padding-bottom", "");
			$startPanel.removeClass("absolute");
			$startPanel.addClass("fixed");
			$startPanel.css("right", rightMargin);
		}
		
	} else if (win.scrollTop() < headerHeight) {
		
		$startPanel.css("right", "");
		$startPanel.css("padding-bottom", "");
		$startPanel.removeClass("fixed");
		$startPanel.removeClass("absolute");
	}
	 
}

function submitStep(mode, e) {

	if (e.preventDefault) {
		e.preventDefault();
	} else {
		e.returnValue = false;
	}

	if ($("#submitLoadingMessage").length > 0) {
		$.blockUI({
			message : $("#submitLoadingMessage")
		});
	}

	$("#submitmode").attr("name", mode);

	$("section.service form").trigger("submit");

	var target = e.target || e.srcElement;

	if ($(target).hasClass("unblock-submit")) {
		$.unblockUI();
	}

}


function redirectFromPreview(e, parameters) {
	
	if(e.preventDefault) {
		e.preventDefault();
	} else {
		e.returnValue = false;
	}
	
	if($("#submitLoadingMessage").length > 0) {
		$.blockUI({ message: $("#submitLoadingMessage") });
	}
	
	var params = "";
	
	if(parameters) {
		params = "?" + parameters
	}
	
	window.location = $("section.service form").attr("action") + params;
	
}

function toggleQueryHelpText(queryID) {
	
	$("#help_" + queryID).toggle();
	$("#closehelp_" + queryID).toggle();
	$("#openhelp_" + queryID).toggle();
	
}

function toggleFlowDescription(element, showLink, hideLink) {
	
	var $element = $(element);
	
	var $description = $(element).parent().next();
	
	$description.toggle();
	
	if($description.is(":visible")) {
		$element.html("- " + hideLink);
	} else {
		$element.html("+ " + showLink);
	}
	
}

function runQueryEvaluators(queryID, parameters) {
	
	parameters["queryID"] = queryID;
	
	currentAjaxOptions = {
		type: "POST",
		cache: false,
		url: actionURL,
		data: parameters,
		dataType: "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		error: function (xhr, ajaxOptions, thrownError) { 
			showErrorDialog(thrownError);
		},
		beforeSend: function ( xhr ) {  
			setTimeout(function() {
				if(currentAjaxRequest != undefined && currentAjaxRequest.readyState != 4) {
					$.blockUI({ message: $("#ajaxLoadingMessage") });
				}
			}, 1000);
        },
		success: function(response) {
			if(currentAjaxRequest.getResponseHeader("AjaxPostValid") == "true") {
				parseResponse(response);
			} else {
				showErrorDialog();
			}
		}
	};
	
	currentAjaxRequest = $.ajax(currentAjaxOptions);
	
}

function parseResponse(response) {
	
	if(response.QueryModifications) {
		
		$.each(response.QueryModifications, function(i, queryModification) {
			
			var actionHandled = false;
			
			if (queryModification.Links) {
				
				var $head = $("head");
				
				$.each(queryModification.Links, function(i, link) {
					
					if (!linkIsLoaded(link)) {
						
						$("<link/>", {
							href: link.href,
							media: link.media,
							rel: link.rel,
							type: link.type
						}).appendTo($head);
					}
				});
			}
			
			if (queryModification.Scripts) {

				var loadingStatus = loadFlowEngineScripts(queryModification.Scripts);
				
				if (loadingStatus == "LOADED") {
					
					runAction(queryModification);
					
				} else if (loadingStatus == "ERROR") {
					
					showErrorDialog();
					return false;
					
				} else {
					
					setTimeout(function() { waitUntilScriptsAreLoaded(queryModification); }, 5);
				}
				
			} else {
				
				runAction(queryModification);
			}
		});
		
	}
	
	if(response.ValidationErrors) {
		
		if(typeof setQueryValidationErrorFunctions[response.ValidationErrors.queryType] == "function") {
			
			setQueryValidationErrorFunctions[response.ValidationErrors.queryType](response.ValidationErrors.queryID, response.ValidationErrors.errors);
		}
		
	}
	
	$.unblockUI();
	
}

function waitUntilScriptsAreLoaded(queryModification) {
	
	var loadingStatus = loadFlowEngineScripts(queryModification.Scripts);
	
	if (loadingStatus == "LOADED") {
		
		runAction(queryModification);
		
	} else if (loadingStatus == "ERROR") {
		
		showErrorDialog();
		
	} else {
		
		setTimeout(function() { waitUntilScriptsAreLoaded(queryModification); }, 5);
	}
	
}

function runAction(queryModification) {
	
	var action = queryModification.action;
	var queryID = queryModification.queryID;
	
	if(action == "SHOW") {

		showQuery(queryID, queryModification.formHTML);
		
	} else if(action == "MAKE_REQUIRED") {
		
		if($("#query_" + queryID).html().length > 0) {
			
			if(typeof setQueryRequiredFunctions[queryModification.queryType] == "function") {
				
				setQueryRequiredFunctions[queryModification.queryType](queryID);
				
			}
		
		} else {

			showQuery(queryID, queryModification.formHTML);
			
		}
		
	} else if(action == "HIDE") {
		
		hideQuery(queryID);
		
	} else if(action == "RELOAD") {
		
		reloadQuery(queryID, queryModification.formHTML);
		
	}
	
	$(".query.mergedquery").removeClass("mergedquery");
	
	$(".query.mergewithpreviousquery").each(function(){
		
		$(this).prevAll(".query:first").addClass("mergedquery");
	});
	
}

function showErrorDialog(error) {
	
	if (console != undefined && error != undefined){
		console.error(error);
	}
	
	$.blockUI({ message: $("#ajaxErrorMessage") });
	
}

function showQuery(queryID, html) {
	
	if($("#query_" + queryID).length == 0) {
		
		$(".queries").append(html);
		
	} else {
		
		reloadQuery(queryID, html);
		
	}
	
}

function hideQuery(queryID) {
	
	if(hideQueryFunctions.length > 0) {
		
		$.each(hideQueryFunctions, function(index, listener) {
			
			if(typeof listener == "function") {
				
				listener($("#query_" + queryID));
				
			}
			
		});
		
	}
	
	$("#query_" + queryID).empty().removeAttr("class").addClass("hidden");

}

function reloadQuery(queryID, html) {
	
	$("#query_" + queryID).replaceWith(html);
	
	if(flowEngineDOMListeners.length > 0) {
		
		$.each(flowEngineDOMListeners, function(index, listener) {
			
			if(typeof listener == "function") {
				
				listener($("#query_" + queryID));
				
			}
			
		});
		
	}
	
}

function reloadCurrentStep() {
	
	$("form").trigger("submit");
	
}

function retryAjaxPost() {
	
	$.blockUI({ message: $("#ajaxLoadingMessage") });
	
	currentAjaxRequest = $.ajax(currentAjaxOptions);
	
}

function cancelAjaxPost() {
	
	if(currentAjaxRequest != undefined) {
	
		currentAjaxRequest.abort();
	
	}
	
	$.unblockUI();
	
}

function closeErrorDialog() {
	
	$.unblockUI();
	
}

function linkIsLoaded(link) {
	
	if ($("link[href='" + link.href + "']").length == 0) {
		return false;
	} 
	
	return true;
	
}

var dynamicallyLoadingScripts = []
var dynamicallyLoadingScriptsFailed = []
var dynamicallyLoadedScripts = []

function scriptIsLoaded(script) {
	
	if ($("script[src='" + script.src.split("?")[0] + "']").length > 0) {
		return true;
	}
	
	if (dynamicallyLoadedScripts.indexOf(script.src) != -1) {
		return true;
	}
	
	return false;
}

function scriptLoaded(src) {
	
//	console.log("loaded " + src);
	
	// Add to loaded
	dynamicallyLoadedScripts.push(src);
	
	// Remove from loading
	var index = dynamicallyLoadingScripts.indexOf(src);
	dynamicallyLoadingScripts.splice(index, 1);
}

function completeLoadingOfScript(script, parentScript, code) {
	
	// Check if load has been aborted
	if (dynamicallyLoadingScripts.indexOf(script.src) == -1) {
//		console.log("aborted " + script.src);
		return;
	}
	
	if (parentScript != null && !scriptIsLoaded(parentScript)) {
		
		if (dynamicallyLoadingScripts.indexOf(parentScript.src) != -1) {
			
//			console.log("waiting for parent " + script.src + ", parent=" + parentScript.src);
			
			// Wait for parent script to load
			setTimeout(function() { completeLoadingOfScript(script, parentScript, code) }, 1);
			
		} else {
		
//			console.log("parent script load aborted " + script.src + ", parent=" + parentScript.src);
			
			// Remove from loading
			var index = dynamicallyLoadingScripts.indexOf(script.src);
			dynamicallyLoadingScripts.splice(index, 1);
		}
		
	} else {
		
//		console.log("evaluating " + script.src);
		
		try {
			jQuery.globalEval(code + "; scriptLoaded('" + script.src + "');");
			
			if (dynamicallyLoadedScripts.indexOf(script.src) == -1) {
				
				if (console != undefined){
					console.warn("eval completed but loaded not set" + script.src);
				}

				dynamicallyLoadingScriptsFailed.push(script.src);
				
				// Remove from loading
				var index = dynamicallyLoadingScripts.indexOf(script.src);
				dynamicallyLoadingScripts.splice(index, 1);
			}
			
		} catch(error) {
			
			dynamicallyLoadingScriptsFailed.push(script.src);
			
			// Remove from loading
			var index = dynamicallyLoadingScripts.indexOf(script.src);
			dynamicallyLoadingScripts.splice(index, 1);
			
			if (console != undefined && error != undefined){
				console.error(error);
			}
		}
	}
}

function loadFlowEngineScripts(scripts) {

    var loadingComplete = true
    var loadingError = false
    var prevScript = null
    
    $.each(scripts, function(i, script) {
			
			if (!scriptIsLoaded(script)) {
				
				loadingComplete = false
				
				if (dynamicallyLoadingScriptsFailed.indexOf(script.src) != -1) {
					
					if (console != undefined){
						console.warn("loading of script failed " + script.src);
					}
					
//					// Remove from failed
					var index = dynamicallyLoadingScriptsFailed.indexOf(script.src);
					dynamicallyLoadingScriptsFailed.splice(index, 1);
					
					loadingError = true
					return false;
					
				} else if (dynamicallyLoadingScripts.indexOf(script.src) == -1) {
					
					dynamicallyLoadingScripts.push(script.src);
					
					var prevScriptInner = prevScript;
					
					jQuery.ajax({
						url: script.src,
						accepts: script.type,
						async: true,
						cache: false,
						dataType: 'text', //Avoids automatic eval
						
					}).done(function(data, textStatus, jqXHR){
						
						completeLoadingOfScript(script, prevScriptInner, data);
						
					}).fail(function(jqXHR, textStatus, errorThrown){
						
						if (console != undefined){
							console.error("failed to fetch script at " + script.src + ", " + textStatus + ", " + errorThrown);
						}
						
						dynamicallyLoadingScriptsFailed.push(script.src);
						
						// Remove from loading
						var index = dynamicallyLoadingScripts.indexOf(script.src);
						dynamicallyLoadingScripts.splice(index, 1);
					});
				}
			}
			
			prevScript = script;
    });
    
    if (loadingError) {
    	return "ERROR";
    }
    
    if (loadingComplete) {
    	return "LOADED";
    }
    
    return false;
}

function showNotificationDialog(type, delay, msg) {
	var modal = $("<div>").addClass("mini-modal " + type);
	var inner = $("<div>").addClass("inner");
	var span = $("<span>").text(msg);

	inner.append(span);
	modal.append(inner);
	modal.prependTo("body");

	modal.fadeIn("fast").delay(delay).fadeOut("fast", function() {
		modal.remove();
	});
}

function initFlowInstanceControlPanel() {
	
	var $controlPanel = $(".panel-wrapper")
	
	if($controlPanel.length > 0) {
	
		var $window = $(window);
	
		var $header = $("body header");
		
		$window.on("scroll resize", function(e) {
			
			var pos = $window.scrollTop();
			
			var hh = $header.height();
			
			if(pos > hh) {
				
				$controlPanel.css({position: "absolute", top: (pos-hh) + "px"});
				
			} else {
				
				$controlPanel.css({position: "static"});
			}
		});
	}
}

function askForLoginOrContinue(aLink, event) {
	event.preventDefault();
	event.stopPropagation();
	
	var helpBoxHeight = $('body').height();

    $('div[data-help-box]').removeClass('active');

    var helpBox = $("div[data-help-box='askLogin']");
    $("div[data-help-box='askLogin'].help-backdrop").show();
    helpBox.addClass('active').find('> div > div').attr('style', 'max-height: ' + (helpBoxHeight - 80) + 'px !important;');

    $("body > header").css("position", "inherit");
    
    helpBox.find("a.btn").off();
    
    helpBox.find("a.btn.login").on("click", function(){
    	window.location = $(aLink).attr("href") + "?triggerlogin=1";
    });
    
    helpBox.find("a.btn.continue").on("click", function(){
    	window.location = $(aLink).attr("href");
    });
}
